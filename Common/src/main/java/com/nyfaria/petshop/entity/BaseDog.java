package com.nyfaria.petshop.entity;

import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.entity.ai.Beg;
import com.nyfaria.petshop.entity.ai.Dig;
import com.nyfaria.petshop.entity.ai.FetchBall;
import com.nyfaria.petshop.entity.ai.FindDig;
import com.nyfaria.petshop.entity.ai.FindPOI;
import com.nyfaria.petshop.entity.ai.GoToBed;
import com.nyfaria.petshop.entity.ai.GoToBowl;
import com.nyfaria.petshop.entity.ai.GoToDig;
import com.nyfaria.petshop.entity.ai.ModAnimalMakeLove;
import com.nyfaria.petshop.entity.ai.ReturnItemToOwner;
import com.nyfaria.petshop.entity.ai.Sleep;
import com.nyfaria.petshop.entity.data.Animations;
import com.nyfaria.petshop.entity.enums.MovementType;
import com.nyfaria.petshop.entity.ifaces.Digger;
import com.nyfaria.petshop.entity.ifaces.Fetcher;
import com.nyfaria.petshop.entity.ifaces.Hungry;
import com.nyfaria.petshop.entity.ifaces.Thirsty;
import com.nyfaria.petshop.init.BlockStateInit;
import com.nyfaria.petshop.init.ItemInit;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import com.nyfaria.petshop.init.TagInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BaseDog extends BasePet implements Fetcher, Thirsty, Hungry, Digger {
    public static final EntityDataAccessor<Optional<UUID>> FETCH_TARGET = SynchedEntityData.defineId(BaseDog.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(BaseDog.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(BaseDog.class, EntityDataSerializers.FLOAT);
    private static final String MOVE_CONTROLLER = "move_controller";
    private static final String TAIL_CONTROLLER = "tail_controller";
    private static final String EAR_CONTROLLER = "ear_controller";
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    public float thirstLevelThreshold = 0.8f;
    public float hungerLevelThreshold = 0.2f;
    public float thirstSpeed = 0.001f;
    public float hungerSpeed = 0.001f;


    public BaseDog(EntityType<? extends BasePet> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FETCH_TARGET, Optional.empty());
        this.entityData.define(THIRST, 1.0f);
        this.entityData.define(HUNGER, 1.0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("thirst", getThirstLevel());
        tag.putFloat("hunger", getHungerLevel());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setThirstLevel(tag.getFloat("thirst"));
        setHungerLevel(tag.getFloat("hunger"));
    }


    @Override
    protected void customServerAiStep() {
        tickBrain(this);

    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        return SoundEvents.WOLF_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseDog>> getSensors() {
        return ObjectArrayList.of(
                new ItemTemptingSensor<BaseDog>().temptedWith((livingEntity, itemStack) -> itemStack == getPetItemStack()),
                new NearbyPlayersSensor<BaseDog>().setRadius(50).setPredicate((player, wolf) -> player.is(wolf.getOwner())),
                new NearbyLivingEntitySensor<>(),
                new NearbyBlocksSensor<BaseDog>().setRadius(15, 1).setPredicate((state, dog) -> state.getBlock() instanceof BrushableBlock)
        );
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid(),
                new FirstApplicableBehaviour<>(
                        new Beg<>().setBegItem(ItemInit.DOG_TREAT.get())
                                .setController(MOVE_CONTROLLER).setAnimation("beg")
                                .setController2(TAIL_CONTROLLER).setAnimation2("tail_wag_beg"),
                        new FindDig<>(),
                        new Dig<>(),
                        new FindPOI<>()
                                .withMemory(MemoryModuleTypeInit.BED.get())
                                .withTag(TagInit.PET_BEDS_POI)
                                .movementTypePredicate((e, m) -> m == MovementType.WANDER)
                                .startCondition(e -> canDoStuff() && e.level().isNight()),
                        new FindPOI<BaseDog>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .withTag(TagInit.PET_BOWLS_POI)
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.WATER)
                                .startCondition(e -> e.getThirstLevel() <= thirstLevelThreshold && canDoStuff()),
                        new FindPOI<BaseDog>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .withTag(TagInit.PET_BOWLS_POI)
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.KIBBLE)
                                .startCondition(e -> e.getHungerLevel() <= hungerLevelThreshold && canDoStuff()),
                        new GoToDig<>(),
                        new FollowTemptation<BaseDog>().startCondition(e -> e.getMovementType() == MovementType.WANDER && canDoStuff()),
                        new FetchBall<BaseDog>().startCondition(e -> e.getMainHandItem().isEmpty() && e.getMovementType() != MovementType.STAY && canDoStuff()),
                        new ReturnItemToOwner<BaseDog>().startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()),
                        new FollowOwner<BasePet>().teleportToTargetAfter(50).startCondition(e -> e.getMainHandItem().isEmpty() && e.getMovementType() == MovementType.FOLLOW && canDoStuff())),
                new LookAtTarget<BasePet>().startCondition(e -> canDoStuff()).runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new Sleep<>(),
                new Dig<>(),
                new GoToBowl<>(),
                new GoToBed<>(),
                new MoveToWalkTarget<BaseDog>().startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()),
                new ModAnimalMakeLove<BaseDog>(getType(), 1.0f).startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()));                                                                                    // Move to the current walk target
    }

    @Override
    public boolean isTreat(ItemStack stack) {
        return stack.is(ItemInit.DOG_TREAT.get());
    }

    @Override
    public void doTreatStuff(Player player, InteractionHand hand) {
        super.doTreatStuff(player, hand);
        setHungerLevel(getHungerLevel() + 0.1f);
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getIdleTasks() {
        return BrainActivityGroup.idleTasks(

                new FirstApplicableBehaviour<BaseDog>(
                        new SetPlayerLookTarget<BaseDog>().startCondition(e -> e.getMovementType() == MovementType.STAY),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<BaseDog>().speedModifier(1).startCondition(e -> e.getOwner() == null || e.getMovementType() == MovementType.WANDER),
                        new Idle<BaseDog>().runFor(entity -> entity.getRandom().nextInt(30, 60)).startCondition(e -> e.getMovementType() == MovementType.WANDER)
                )
        );
    }

    @Override
    public void performBowlAction(PetBowl.Type type) {
        if (type == PetBowl.Type.WATER) {
            setThirstLevel(getThirstLevel() + (1.0f - thirstLevelThreshold));
        } else if (type == PetBowl.Type.KIBBLE) {
            setHungerLevel(getHungerLevel() + (1.0f - hungerLevelThreshold));
        }
    }


    @Override
    public ThrowableItemProjectile getFetchTarget() {
        if (level().isClientSide) return null;
        if (this.entityData.get(FETCH_TARGET).isEmpty()) return null;
        return (ThrowableItemProjectile) ((ServerLevel) level()).getEntity(this.entityData.get(FETCH_TARGET).get());
    }

    @Override
    public void setFetchTarget(ThrowableItemProjectile entity) {
        if (level().isClientSide) return;
        this.entityData.set(FETCH_TARGET, Optional.ofNullable(entity).map(Entity::getUUID));
    }


    @Override
    public EntityDimensions getDimensions(Pose $$0) {
        if (isBegging()) {
            return super.getDimensions($$0).scale(1.0f, 1.2f);
        }
        return super.getDimensions($$0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        if (tickCount % 20 == 0 && getRandom().nextFloat() < 0.01) {
            triggerAnim(EAR_CONTROLLER, "ear_wiggle");
        }
        tickThirst();
        tickHunger();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, MOVE_CONTROLLER, this::moveControllerState)
                .triggerableAnim("beg", RawAnimation.begin().thenPlay("beg"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlayXTimes("idle", 1))
                .triggerableAnim("dig", RawAnimation.begin().thenLoop("dig"))
                .triggerableAnim("sleep", RawAnimation.begin().thenPlay("sleep"))
        );
        controllerRegistrar.add(new AnimationController<>(this, TAIL_CONTROLLER, this::tailControllerState)
                .triggerableAnim("tail_wag_beg", RawAnimation.begin().thenPlay("tail_wag_beg"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlayXTimes("idle", 1))
                .triggerableAnim("tail_wag_sit", RawAnimation.begin().thenPlay("tail_wag_sit"))
        );
        controllerRegistrar.add(new AnimationController<>(this, EAR_CONTROLLER, this::earControllerState)
                .triggerableAnim("ear_wiggle", Animations.EAR_WIGGLE)
                .triggerableAnim("ear_wiggle3", Animations.EAR_WIGGLE3)
        );
    }

    private PlayState earControllerState(AnimationState<BaseDog> baseDogAnimationState) {
        baseDogAnimationState.setAnimation(Animations.EAR_IDLE);
        return PlayState.CONTINUE;
    }

    private PlayState tailControllerState(AnimationState<BaseDog> baseDogAnimationState) {

        if (getMovementType() == MovementType.STAY) {
            if (getOwner() != null && distanceToSqr(getOwner()) < 17) {
                baseDogAnimationState.setAnimation(Animations.TAIL_WAG_SIT);
            } else {
                baseDogAnimationState.setAnimation(Animations.TAIL_SIT_IDLE);
            }
        } else {
            if (getOwner() != null && distanceToSqr(getOwner()) < 17 && canDoStuff()) {
                baseDogAnimationState.setAnimation(Animations.TAIL_WAG_STAND);
            } else {
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    private PlayState moveControllerState(AnimationState<BaseDog> baseDogAnimationState) {
        if (getMovementType() == MovementType.STAY) {
            baseDogAnimationState.setAnimation(Animations.SIT);
            return PlayState.CONTINUE;
        }
        if (baseDogAnimationState.isMoving()) {
            baseDogAnimationState.setAnimation(Animations.WALK);
            return PlayState.CONTINUE;
        }
//        baseDogAnimationState.setAnimation(RawAnimation.begin().thenLoop("dig"));
//        return PlayState.CONTINUE;
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }

    @Override
    public float getThirstLevel() {
        return this.entityData.get(THIRST);
    }

    @Override
    public void setThirstLevel(float thirstLevel) {
        this.entityData.set(THIRST, thirstLevel);
    }

    @Override
    public void tickThirst() {
        if (level().isClientSide) return;
        if (tickCount % 40 == 0 && getThirstLevel() > 0) {
            setThirstLevel(getThirstLevel() - thirstSpeed);
        }
    }

    @Override
    public float getHungerLevel() {
        float hungerLevel = this.entityData.get(HUNGER);
        return hungerLevel;
    }

    @Override
    public void setHungerLevel(float hungerLevel) {
        this.entityData.set(HUNGER, hungerLevel);
    }

    @Override
    public void doPetStuff(Player player, InteractionHand hand) {
        triggerAnim(EAR_CONTROLLER, "ear_wiggle3");
    }

    @Override
    public void tickHunger() {

        if (level().isClientSide) return;
        if (tickCount % 40 == 0 && getHungerLevel() > 0) {
            setHungerLevel(getHungerLevel() - hungerSpeed);
        }
    }

    @Override
    public boolean isDigging() {
        return BrainUtils.hasMemory(this, MemoryModuleTypeInit.DIG_POS.get());
    }

    @Override
    public boolean canDoStuff() {
        boolean isDigging = isDigging();
        boolean isSleeping = isPetSleeping();
        return !isDigging && !isSleeping && getMovementType() != MovementType.STAY;
    }


}
