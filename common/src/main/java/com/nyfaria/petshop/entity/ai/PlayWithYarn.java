package com.nyfaria.petshop.entity.ai;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.entity.ifaces.YarnPlayer;
import com.nyfaria.petshop.init.ItemInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class PlayWithYarn<E extends TamableAnimal & YarnPlayer> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED)
    );

    protected ThrowableItemProjectile yarnProjectile = null;
    protected ItemEntity yarnItemEntity = null;

    private static final int MAX_PLAY_TIME = 20*60*3;
    private static final double MAX_YARN_DISTANCE = 15.0;
    private static final double CARRY_CHANCE = 0.3;
    private static final double INTERACTION_DISTANCE = 2.0;
    private static final double INTERACTION_DISTANCE_SQ = INTERACTION_DISTANCE * INTERACTION_DISTANCE;

    private int carryTimer = 0;
    private int currentCarryDuration = 0;
    private static final int MIN_CARRY_TIME = 60;
    private static final int MAX_CARRY_TIME = 140;

    private int actionCooldown = 0;
    private static final int MIN_ACTION_COOLDOWN = 10;
    private static final int MAX_ACTION_COOLDOWN = 30;

    private int wanderCooldown = 0;
    private static final int MIN_WANDER_COOLDOWN = 20;
    private static final int MAX_WANDER_COOLDOWN = 60;

    public PlayWithYarn() {
        runFor(entity -> Integer.MAX_VALUE);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (entity.getYarnTarget() != null) return true;
        if (entity.isPlayingWithYarn()) return true;
        if (hasYarnInHand(entity)) return true;
        return findNearbyYarnItem(entity) != null;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        if (entity.getYarnPlayTime() >= MAX_PLAY_TIME) {
            return false;
        }
        if (hasYarnInHand(entity)) {
            return true;
        }
        if (yarnItemEntity != null && !yarnItemEntity.isRemoved() && isYarnInRange(entity, yarnItemEntity)) {
            return true;
        }
        if (yarnProjectile != null && !yarnProjectile.isRemoved() && isYarnInRange(entity, yarnProjectile)) {
            return true;
        }
        ItemEntity nearbyYarn = findNearbyYarnItem(entity);
        if (nearbyYarn != null && isYarnInRange(entity, nearbyYarn)) {
            this.yarnItemEntity = nearbyYarn;
            return true;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
        this.yarnProjectile = entity.getYarnTarget();
        if (this.yarnProjectile == null) {
            this.yarnItemEntity = findNearbyYarnItem(entity);
        } else {
            this.yarnItemEntity = null;
        }
        if (!entity.isPlayingWithYarn()) {
            entity.setYarnPlayTime(0);
        }
        carryTimer = 0;
        currentCarryDuration = 0;
        actionCooldown = 0;
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        int playTime = entity.getYarnPlayTime();
        entity.setYarnPlayTime(playTime + 1);

        if (playTime >= MAX_PLAY_TIME) {
            dropYarn(entity);
            resetState(entity);
            return;
        }

        if (hasYarnInHand(entity)) {
            entity.setPlayingWithYarn(true);
            carryTimer++;

            if (carryTimer >= currentCarryDuration) {
                dropYarn(entity);
                carryTimer = 0;
                currentCarryDuration = 0;
                wanderCooldown = 0;
                actionCooldown = MIN_ACTION_COOLDOWN + entity.getRandom().nextInt(MAX_ACTION_COOLDOWN - MIN_ACTION_COOLDOWN);
            } else {
                if (wanderCooldown > 0) {
                    wanderCooldown--;
                } else if (entity.getNavigation().isDone()) {
                    Vec3 wanderTarget = DefaultRandomPos.getPos(entity, 5, 4);
                    if (wanderTarget != null) {
                        entity.getNavigation().moveTo(wanderTarget.x, wanderTarget.y, wanderTarget.z, 0.8f);
                    }
                    wanderCooldown = MIN_WANDER_COOLDOWN + entity.getRandom().nextInt(MAX_WANDER_COOLDOWN - MIN_WANDER_COOLDOWN);
                }
            }
        } else {
            entity.setPlayingWithYarn(false);

            if (actionCooldown > 0) {
                actionCooldown--;
                return;
            }

            Entity target = getActiveTarget(entity);

            if (target == null || target.isRemoved()) {
                ItemEntity nearbyYarn = findNearbyYarnItem(entity);
                if (nearbyYarn != null && isYarnInRange(entity, nearbyYarn)) {
                    this.yarnItemEntity = nearbyYarn;
                    this.yarnProjectile = null;
                    target = nearbyYarn;
                }
            }

            if (target != null && !target.isRemoved()) {
                if (!isYarnInRange(entity, target)) {
                    resetState(entity);
                    return;
                }

                double distSq = entity.distanceToSqr(target);
                if (distSq <= INTERACTION_DISTANCE_SQ) {
                    entity.getNavigation().stop();
                    decideAction(entity, target);
                } else {
                    if (entity.getNavigation().isDone() || entity.getNavigation().getTargetPos() == null) {
                        entity.getNavigation().moveTo(target, 1.3f);
                    }
                }
            }
        }
    }

    private void decideAction(E entity, Entity target) {
        if (entity.getRandom().nextDouble() < CARRY_CHANCE) {
            pickUpYarn(entity, target);
            currentCarryDuration = MIN_CARRY_TIME + entity.getRandom().nextInt(MAX_CARRY_TIME - MIN_CARRY_TIME);
        } else {
            batYarnAway(entity, target);
        }
    }

    private void batYarnAway(E entity, Entity target) {
        ItemStack yarn;
        if (target instanceof ThrowableItemProjectile projectile) {
            yarn = projectile.getItem().copy();
            projectile.discard();
            this.yarnProjectile = null;
        } else if (target instanceof ItemEntity itemEntity) {
            yarn = itemEntity.getItem().copy();
            itemEntity.discard();
            this.yarnItemEntity = null;
        } else {
            return;
        }

        ItemEntity battedYarn = new ItemEntity(
            entity.level(),
            entity.getX(),
            entity.getY() + 0.3,
            entity.getZ(),
            yarn
        );

        double force = 0.15 + entity.getRandom().nextDouble() * 0.35;
        double angle = entity.getRandom().nextDouble() * Math.PI * 2;
        double xVel = Math.cos(angle) * force;
        double zVel = Math.sin(angle) * force;
        double yVel = 0.1 + entity.getRandom().nextDouble() * 0.15;

        battedYarn.setDeltaMovement(xVel, yVel, zVel);
        battedYarn.setPickUpDelay(10);
        entity.level().addFreshEntity(battedYarn);

        this.yarnItemEntity = battedYarn;
        actionCooldown = MIN_ACTION_COOLDOWN + entity.getRandom().nextInt(MAX_ACTION_COOLDOWN - MIN_ACTION_COOLDOWN);
    }

    private boolean hasYarnInHand(E entity) {
        return !entity.getMainHandItem().isEmpty() && entity.getMainHandItem().is(ItemInit.YARN_BALL.get());
    }

    private boolean isYarnInRange(E entity, Entity target) {
        return entity.distanceTo(target) <= MAX_YARN_DISTANCE;
    }

    private Entity getActiveTarget(E entity) {
        if (yarnProjectile != null && !yarnProjectile.isRemoved()) {
            return yarnProjectile;
        }
        if (yarnItemEntity != null && !yarnItemEntity.isRemoved()) {
            return yarnItemEntity;
        }
        ThrowableItemProjectile projectile = entity.getYarnTarget();
        if (projectile != null && !projectile.isRemoved()) {
            this.yarnProjectile = projectile;
            return projectile;
        }
        return null;
    }

    private ItemEntity findNearbyYarnItem(E entity) {
        AABB searchBox = entity.getBoundingBox().inflate(MAX_YARN_DISTANCE);
        List<ItemEntity> items = entity.level().getEntitiesOfClass(ItemEntity.class, searchBox,
            item -> item.getItem().is(ItemInit.YARN_BALL.get()));
        return items.isEmpty() ? null : items.get(0);
    }

    private void pickUpYarn(E entity, Entity target) {
        ItemStack yarn;
        if (target instanceof ThrowableItemProjectile projectile) {
            yarn = projectile.getItem();
            projectile.discard();
            this.yarnProjectile = null;
        } else if (target instanceof ItemEntity itemEntity) {
            yarn = itemEntity.getItem().copy();
            itemEntity.discard();
            this.yarnItemEntity = null;
        } else {
            return;
        }
        entity.setItemSlot(EquipmentSlot.MAINHAND, yarn);
        entity.setPlayingWithYarn(true);
        entity.setYarnTarget(null);
        carryTimer = 0;
    }

    private void resetState(E entity) {
        entity.setPlayingWithYarn(false);
        entity.setYarnPlayTime(0);
        entity.setYarnTarget(null);
        this.yarnProjectile = null;
        this.yarnItemEntity = null;
        this.carryTimer = 0;
        this.currentCarryDuration = 0;
        this.actionCooldown = 0;
        this.wanderCooldown = 0;
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        super.stop(level, entity, gameTime);
        entity.getNavigation().stop();
    }


    private void dropYarn(E entity) {
        if (!entity.getMainHandItem().isEmpty()) {
            ItemStack yarn = entity.getMainHandItem().copy();
            entity.setItemInHand(entity.getUsedItemHand(), ItemStack.EMPTY);

            ItemEntity droppedYarn = new ItemEntity(
                entity.level(),
                entity.getX(),
                entity.getY() + 0.3,
                entity.getZ(),
                yarn
            );
            droppedYarn.setPickUpDelay(10);
            entity.level().addFreshEntity(droppedYarn);
            this.yarnItemEntity = droppedYarn;
        }
        entity.setPlayingWithYarn(false);
        actionCooldown = MIN_ACTION_COOLDOWN + entity.getRandom().nextInt(MAX_ACTION_COOLDOWN - MIN_ACTION_COOLDOWN);
    }
}
