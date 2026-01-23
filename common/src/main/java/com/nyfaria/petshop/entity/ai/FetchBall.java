package com.nyfaria.petshop.entity.ai;

import com.nyfaria.petshop.entity.ifaces.Fetcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;

public class FetchBall<E extends TamableAnimal & Fetcher> extends FollowEntity<E, Entity> {
    protected ThrowableItemProjectile owner = null;
    private static final double PICKUP_DISTANCE = 2.0;
    private static final double PICKUP_DISTANCE_SQ = PICKUP_DISTANCE * PICKUP_DISTANCE;

    public FetchBall() {
        following(this::getBall);
        speedMod(1.5f);
        stopFollowingWithin(1.5f);
    }

    @Override
    protected void stop(E entity) {
        if (owner != null && !owner.isRemoved()) {
            if (entity.distanceToSqr(owner) <= PICKUP_DISTANCE_SQ) {
                entity.setItemSlot(EquipmentSlot.MAINHAND, owner.getItem());
                owner.discard();
            }
        }
        super.stop(entity);
    }

    protected Entity getBall(E entity) {
        if (this.owner == null)
            this.owner = entity.getFetchTarget();

        if (this.owner != null && this.owner.isRemoved())
            this.owner = null;

        return this.owner;
    }
}