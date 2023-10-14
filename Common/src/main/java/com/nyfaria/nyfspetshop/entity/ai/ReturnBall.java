package com.nyfaria.nyfspetshop.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;

/**
 * A movement behaviour for automatically following the owner of a {@link TamableAnimal TameableAnimal}.<br>
 * @param <E> The owner of the brain
 * @param <T> The minimum common class of the entity expected to be following
 */
public class ReturnBall<E extends TamableAnimal> extends FollowEntity<E, LivingEntity> {
	protected LivingEntity owner = null;

	public ReturnBall() {
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
		if(entity.distanceToSqr(owner) <= min * min) {
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
