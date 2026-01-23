package com.nyfaria.petshop.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;

public class ReturnItemToOwner<E extends TamableAnimal> extends FollowEntity<E, LivingEntity> {
    protected LivingEntity owner = null;

    public ReturnItemToOwner() {
        following(this::getOwner);
        stopFollowingWithin(2f);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity) && !entity.getMainHandItem().isEmpty();
    }

    @Override
    protected void stop(E entity) {
        double min = followDistMin.apply(entity, owner);
        if (entity.distanceToSqr(owner) <= min * min) {
            Block.popResource(entity.level(), entity.blockPosition(), entity.getMainHandItem());
            entity.setItemInHand(entity.getUsedItemHand(), ItemStack.EMPTY);
        }
        super.stop(entity);
    }

    protected LivingEntity getOwner(E entity) {
        if (this.owner == null)
            this.owner = entity.getOwner();

        if (this.owner != null && this.owner.isRemoved())
            this.owner = null;

        return this.owner;
    }
}
