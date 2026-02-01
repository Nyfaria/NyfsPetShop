package com.nyfaria.petshop.entity;

import com.nyfaria.petshop.block.*;
import com.nyfaria.petshop.entity.ai.*;
import com.nyfaria.petshop.entity.enums.*;
import com.nyfaria.petshop.entity.ifaces.*;
import com.nyfaria.petshop.init.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.*;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.tslat.smartbrainlib.api.core.*;
import net.tslat.smartbrainlib.api.core.behaviour.*;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.*;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.*;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.*;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.*;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.*;
import net.tslat.smartbrainlib.api.core.sensor.*;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.*;
import software.bernie.geckolib.core.animatable.instance.*;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.*;
import software.bernie.geckolib.util.*;

import java.util.*;

public class BaseDragon extends BasePet implements Thirsty, Hungry {
    public static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(BaseDragon.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(BaseDragon.class, EntityDataSerializers.FLOAT);
    private static final String MOVE_CONTROLLER = "move_controller";
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    public float thirstLevelThreshold = 0.8f;
    public float hungerLevelThreshold = 0.2f;

    public BaseDragon(EntityType<? extends BasePet> $$0, Level $$1) {
        super($$0, $$1);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.2F).add(Attributes.FLYING_SPEED, (double) 0.4F).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public void tick() {
        super.tick();
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
    public List<? extends ExtendedSensor<? extends BaseDragon>> getSensors() {
        return ObjectArrayList.of(
                new ItemTemptingSensor<BaseDragon>().temptedWith((livingEntity, itemStack) -> itemStack == getPetItemStack()),
                new NearbyPlayersSensor<BaseDragon>().setRadius(50).setPredicate((player, wolf) -> player.getMainHandItem().is(ItemInit.CHICKEN_DRUMSTICK.get()) || player.getOffhandItem().is(ItemInit.CHICKEN_DRUMSTICK.get()) || player.is(wolf.getOwner())),
                new NearbyLivingEntitySensor<BaseDragon>().setPredicate((target, entity) -> target instanceof Monster)
        );
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(THIRST, 1.0f);
        this.entityData.define(HUNGER, 1.0f);
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FirstApplicableBehaviour<BaseDragon>(
                        new Beg<>().setBegItem(ItemInit.CHICKEN_DRUMSTICK.get())
                                .setController(MOVE_CONTROLLER).setAnimation("walk"),
                        new FindPOI<BaseDragon>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .withTag(TagInit.PET_BOWLS_POI)
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.WATER)
                                .startCondition(e -> e.getThirstLevel() <= thirstLevelThreshold && canDoStuff()),
                        new FindPOI<BaseDragon>()
                                .withMemory(MemoryModuleTypeInit.BOWL_POS.get())
                                .withTag(TagInit.PET_BOWLS_POI)
                                .checkState((level, pos, state) -> state.hasProperty(BlockStateInit.BOWL_TYPE) && state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.KIBBLE)
                                .startCondition(e -> e.getHungerLevel() <= hungerLevelThreshold && canDoStuff()),
                        new FollowTemptation<BaseDragon>().startCondition(e -> e.getMovementType() == MovementType.WANDER && canDoStuff()),
                        new FollowOwner<BasePet>().teleportToTargetAfter(50).startCondition(e -> e.getMainHandItem().isEmpty() && e.getMovementType() == MovementType.FOLLOW && canDoStuff())),
                new LookAtTarget<BasePet>().startCondition(e -> canDoStuff()).runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new GoToBowl<>(),
                new MoveToWalkTarget<BaseDragon>().startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()),
                new ModAnimalMakeLove<BaseDragon>(getType(), 1.0f).startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()));                                                                                    // Move to the current walk target
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

                new FirstApplicableBehaviour<BaseDragon>(
                        new SetPlayerLookTarget<BaseDragon>().startCondition(e -> e.getMovementType() == MovementType.STAY),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<BaseDragon>().speedModifier(1).walkTargetPredicate(
                                (e,a)-> {
                                    if(e.getOwner() == null) return false;
                                    e.restrictTo(BlockPos.containing(e.getOwner().getEyePosition()),5);
                                    return true;
                                }
                        ).startCondition(e -> canDoStuff()),
                        new Idle<BaseDragon>().runFor(entity -> entity.getRandom().nextInt(30, 60)).startCondition(e -> e.getMovementType() == MovementType.WANDER && canDoStuff())
                ),
                new SetAttackTarget<BaseDragon>(false).targetFinder(entity -> {
                    if (entity.getOwner() instanceof Player player) {
                        LivingEntity lastHurt = player.getLastHurtMob();
                        if (lastHurt instanceof Monster && lastHurt.isAlive()) {
                            return lastHurt;
                        }
                    }
                    return null;
                })
        );
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<BaseDragon>().invalidateIf((entity, target) -> !(target instanceof Monster) || !target.isAlive()),
                new DragonFireballAttack<BaseDragon>(10).cooldown(e -> e.getRandom().nextIntBetweenInclusive(1200, 2400)).attackRadius(16)
        );
    }

    @Override
    public boolean canDoStuff() {
        return getMovementType() != MovementType.STAY && !isBegging();
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

    private PlayState moveControllerState(AnimationState<BaseDragon> baseDogAnimationState) {
//        if (getMovementType() == MovementType.STAY) {
//            return PlayState.STOP;
//        }
//        if (baseDogAnimationState.isMoving()) {
            baseDogAnimationState.setAnimation(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
//        }
//
//        return PlayState.STOP;
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
        return stack.is(ItemInit.CHICKEN_DRUMSTICK.get());
    }

    @Override
    public void doPetStuff(Player interactingPlayer, InteractionHand hand) {

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



    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }


}
