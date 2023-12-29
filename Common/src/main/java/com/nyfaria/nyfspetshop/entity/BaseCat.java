package com.nyfaria.nyfspetshop.entity;

import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.entity.ai.FetchBall;
import com.nyfaria.nyfspetshop.entity.ai.FindBowl;
import com.nyfaria.nyfspetshop.entity.ai.GoToBowl;
import com.nyfaria.nyfspetshop.entity.ai.ModAnimalMakeLove;
import com.nyfaria.nyfspetshop.entity.ai.ReturnBall;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.entity.ifaces.Fetcher;
import com.nyfaria.nyfspetshop.entity.ifaces.Hungry;
import com.nyfaria.nyfspetshop.entity.ifaces.Thirsty;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
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

public class BaseCat extends BasePet implements Fetcher, Thirsty, Hungry {
    private static final String MOVE_CONTROLLER = "move_controller";
    private static final String TAIL_CONTROLLER = "tail_controller";
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public static final EntityDataAccessor<Optional<UUID>> FETCH_TARGET = SynchedEntityData.defineId(BaseCat.class, EntityDataSerializers.OPTIONAL_UUID);
    public static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(BaseCat.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(BaseCat.class, EntityDataSerializers.FLOAT);
    public float thirstLevelThreshold = 0.8f;
    public float hungerLevelThreshold = 0.2f;


    public BaseCat(EntityType<? extends BasePet> $$0, Level $$1) {
        super($$0, $$1);
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
    public List<? extends ExtendedSensor<? extends BaseCat>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<BaseCat>().setRadius(50).setPredicate((player, wolf) -> player.is(wolf.getOwner())),
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
                new FirstApplicableBehaviour<BaseCat>(
                        new FindBowl<BaseCat>(PetBowl.Type.WATER).startCondition(e -> e.getThirstLevel() <= thirstLevelThreshold),
                        new FindBowl<BaseCat>(PetBowl.Type.KIBBLE).startCondition(e -> e.getHungerLevel() <= hungerLevelThreshold),
                        new FetchBall<>().startCondition(e -> e.getMainHandItem().isEmpty() && ((BasePet) e).getMovementType() != MovementType.STAY),
                        new ReturnBall<>().startCondition(e -> ((BasePet) e).getMovementType() != MovementType.STAY),
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
    public BrainActivityGroup<? extends BasePet> getIdleTasks() {
        return BrainActivityGroup.idleTasks(

                new FirstApplicableBehaviour<BaseCat>(
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<BaseCat>().speedModifier(1).startCondition(e -> e.getOwner() == null || e.getMovementType() == MovementType.WANDER),
                        new Idle<BaseCat>().runFor(entity -> entity.getRandom().nextInt(30, 60)).startCondition(e -> e.getMovementType() == MovementType.WANDER)
                )
        );
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
        if(random.nextInt(3) == 0) return;
        this.entityData.set(FETCH_TARGET, Optional.ofNullable(entity).map(Entity::getUUID));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, MOVE_CONTROLLER, this::moveControllerState));
        controllerRegistrar.add(new AnimationController<>(this, TAIL_CONTROLLER, this::tailControllerState)
                .triggerableAnim("wag_tail", RawAnimation.begin().thenPlay(getMovementType() == MovementType.STAY ? "tail_wag_sit" : "tail_wag_idle")));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        tickThirst();
        tickHunger();
    }

    private PlayState tailControllerState(AnimationState<BaseCat> baseDogAnimationState) {

        if (getMovementType() == MovementType.STAY) {
            if (getOwner() != null && distanceToSqr(getOwner()) < 17) {
                baseDogAnimationState.setAnimation(RawAnimation.begin().thenPlay("tail_wag_sit"));
            } else {
                baseDogAnimationState.setAnimation(RawAnimation.begin().thenPlay("tail_sit_idle"));
            }
        } else {
            if (getOwner() != null && distanceToSqr(getOwner()) < 17) {
                baseDogAnimationState.setAnimation(RawAnimation.begin().thenPlay("tail_wag_stand"));
            } else {
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    private PlayState moveControllerState(AnimationState<BaseCat> baseDogAnimationState) {
        if (getMovementType() == MovementType.STAY) {
            baseDogAnimationState.setAnimation(RawAnimation.begin().thenLoop("sit"));
            return PlayState.CONTINUE;
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


}
