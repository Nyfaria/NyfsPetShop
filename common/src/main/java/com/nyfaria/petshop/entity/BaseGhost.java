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
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.*;
import net.minecraft.world.entity.ai.navigation.*;
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
import org.joml.*;
import software.bernie.geckolib.core.animatable.instance.*;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.*;
import software.bernie.geckolib.util.*;

import java.util.*;

public class BaseGhost extends BasePet {

    private static final String MOVE_CONTROLLER = "move_controller";
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public BaseGhost(EntityType<? extends BasePet> $$0, Level $$1) {
        super($$0, $$1);
        this.moveControl = new FlyingMoveControl(this, 10, true);
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
    public List<? extends ExtendedSensor<? extends BaseGhost>> getSensors() {
        return ObjectArrayList.of(
                new ItemTemptingSensor<BaseGhost>().temptedWith((livingEntity, itemStack) -> itemStack == getPetItemStack()),
                new NearbyPlayersSensor<BaseGhost>().setRadius(50).setPredicate((player, wolf) -> player.getMainHandItem().is(ItemInit.PANCAKES.get()) || player.getOffhandItem().is(ItemInit.PANCAKES.get()) || player.is(wolf.getOwner())),
                new NearbyLivingEntitySensor<>()

        );
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public BrainActivityGroup<? extends BasePet> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FirstApplicableBehaviour<BaseGhost>(
                        new Beg<>().setBegItem(ItemInit.PANCAKES.get())
                                .setController(MOVE_CONTROLLER),
                        new FollowTemptation<BaseGhost>().startCondition(e -> e.getMovementType() == MovementType.WANDER && canDoStuff()),
                        new FollowOwner<BasePet>().teleportToTargetAfter(50).startCondition(e -> e.getMainHandItem().isEmpty() && e.getMovementType() == MovementType.FOLLOW && canDoStuff())),
                new LookAtTarget<BasePet>().startCondition(e -> canDoStuff()).runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
                new GoToBowl<>(),
                new MoveToWalkTarget<BaseGhost>().startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()),
                new ModAnimalMakeLove<BaseGhost>(getType(), 1.0f).startCondition(e -> e.getMovementType() != MovementType.STAY && canDoStuff()));                                                                                    // Move to the current walk target
    }



    @Override
    public void performBowlAction(PetBowl.Type type) {

    }

    @Override
    protected void checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3) {

    }

    @Override
    public BrainActivityGroup<? extends BasePet> getIdleTasks() {
        return BrainActivityGroup.idleTasks(

                new FirstApplicableBehaviour<BaseGhost>(
                        new SetPlayerLookTarget<BaseGhost>().startCondition(e -> e.getMovementType() == MovementType.STAY),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<BaseGhost>().speedModifier(1).walkTargetPredicate(
                                (e,a)-> {
                                    if(e.getOwner() == null) return false;
                                    e.restrictTo(BlockPos.containing(e.getOwner().getEyePosition()),5);
                                    return true;
                                }
                        ).startCondition(e -> canDoStuff()),
                        new Idle<BaseGhost>().runFor(entity -> entity.getRandom().nextInt(30, 60)).startCondition(e -> e.getMovementType() == MovementType.WANDER && canDoStuff())
                )
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
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    private PlayState moveControllerState(AnimationState<BaseGhost> baseDogAnimationState) {
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
    public Vector3f getEyePatchColor() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean isTreat(ItemStack stack) {
        return stack.is(ItemInit.PANCAKES.get());
    }


    @Override
    public Vector3f getHatColor() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }


}
