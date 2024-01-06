package com.nyfaria.nyfspetshop.entity;

import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.ai.Beg;
import com.nyfaria.nyfspetshop.entity.ai.FindPOI;
import com.nyfaria.nyfspetshop.entity.ai.GoToBowl;
import com.nyfaria.nyfspetshop.entity.ai.ModAnimalMakeLove;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.entity.ifaces.Hungry;
import com.nyfaria.nyfspetshop.entity.ifaces.ShoulderRider;
import com.nyfaria.nyfspetshop.entity.ifaces.Thirsty;
import com.nyfaria.nyfspetshop.init.BlockStateInit;
import com.nyfaria.nyfspetshop.init.ItemInit;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
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

public class BaseBird extends BasePet implements Thirsty, Hungry, ShoulderRider<BaseBird> {
    private static final String MOVE_CONTROLLER = "move_controller";
    private static final String TAIL_CONTROLLER = "tail_controller";
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public static final EntityDataAccessor<Optional<UUID>> FETCH_TARGET = SynchedEntityData.defineId(BaseBird.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(BaseBird.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(BaseBird.class, EntityDataSerializers.FLOAT);
    public float thirstLevelThreshold = 0.8f;
    public float hungerLevelThreshold = 0.2f;
    private int rideCooldownCounter;

    @Override
    public void tick() {
        ++this.rideCooldownCounter;
        super.tick();
    }

    public BaseBird(EntityType<? extends BasePet> $$0, Level $$1) {
        super($$0, $$1);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
    }


    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseBird>> getSensors() {
        return ObjectArrayList.of(
                new ItemTemptingSensor<BaseBird>().temptedWith((livingEntity, itemStack) -> itemStack == getPetItemStack()),
                new NearbyPlayersSensor<BaseBird>().setRadius(50).setPredicate((player, wolf) -> player.getMainHandItem().is(ItemInit.DOG_TREAT.get()) || player.getOffhandItem().is(ItemInit.DOG_TREAT.get()) || player.is(wolf.getOwner())),
                new NearbyLivingEntitySensor<>()

        );
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FETCH_TARGET, Optional.empty());
        this.entityData.define(THIRST, 1.0f);
        this.entityData.define(HUNGER, 1.0f);
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FirstApplicableBehaviour<BaseBird>(
                        new Beg<>().setBegItem(ItemInit.PEANUT.get())
                                .setController(MOVE_CONTROLLER).setAnimation("walk"),
                        new FindPOI<BaseBird>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.WATER)
                                .startCondition(e -> e.getThirstLevel() <= thirstLevelThreshold && canDoStuff()),
                        new FindPOI<BaseBird>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.KIBBLE)
                                .startCondition(e -> e.getHungerLevel() <= hungerLevelThreshold && canDoStuff()),
                        new FollowTemptation<BaseBird>().startCondition(e -> e.getMovementType() == MovementType.WANDER),
                        new FollowOwner<BasePet>().teleportToTargetAfter(50).startCondition(e -> e.getMainHandItem().isEmpty() && e.getMovementType() == MovementType.FOLLOW)),
                new LookAtTarget<BasePet>().runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new GoToBowl<>(),
                new MoveToWalkTarget<>().startCondition(e -> ((BasePet) e).getMovementType() != MovementType.STAY),
                new ModAnimalMakeLove<>(getType(), 1.0f).startCondition(e -> ((BasePet) e).getMovementType() != MovementType.STAY));                                                                                    // Move to the current walk target
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
    public void performBowlAction(PetBowl.Type type) {
        if (type == PetBowl.Type.WATER) {
            setThirstLevel(getThirstLevel() + 0.2f);
        } else if (type == PetBowl.Type.KIBBLE) {
            setHungerLevel(getHungerLevel() + 0.8f);
        }
    }

    @Override
    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {

    }

    @Override
    public BrainActivityGroup<? extends BasePet> getIdleTasks() {
        return BrainActivityGroup.idleTasks(

                new FirstApplicableBehaviour<BaseBird>(
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<BaseBird>().speedModifier(1).startCondition(e -> e.getOwner() == null || e.getMovementType() == MovementType.WANDER),
                        new Idle<BaseBird>().runFor(entity -> entity.getRandom().nextInt(30, 60)).startCondition(e -> e.getMovementType() == MovementType.WANDER)
                )
        );
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, MOVE_CONTROLLER, this::moveControllerState));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        tickThirst();
        tickHunger();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }
    }


    private PlayState moveControllerState(AnimationState<BaseBird> baseDogAnimationState) {
        if (getMovementType() == MovementType.STAY) {
            return PlayState.STOP;
        }
        if (baseDogAnimationState.isMoving()) {
            baseDogAnimationState.setAnimation(RawAnimation.begin().thenLoop("walk"));
            return PlayState.CONTINUE;
        }

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
            setThirstLevel(getThirstLevel() - 0.01f);
        }
    }
    @Override
    public boolean isTreat(ItemStack stack) {
        return stack.is(ItemInit.PEANUT.get());
    }

    @Override
    public void doPetStuff(Player interactingPlayer, InteractionHand hand) {
        if (!interactingPlayer.level().isClientSide) {
            setEntityOnShoulder(this, (ServerPlayer) interactingPlayer);
        }
    }
    @Override
    public void doTreatStuff(Player player, InteractionHand hand) {
        super.doTreatStuff(player, hand);
        setHungerLevel(getHungerLevel() + 0.1f);
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
    public void tickHunger() {

        if (level().isClientSide) return;
        if (tickCount % 40 == 0 && getHungerLevel() > 0) {
            setHungerLevel(getHungerLevel() - 0.01f);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.2F).add(Attributes.FLYING_SPEED, (double) 0.4F).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean canSitOnShoulder() {
        return rideCooldownCounter > 100;
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }
}
