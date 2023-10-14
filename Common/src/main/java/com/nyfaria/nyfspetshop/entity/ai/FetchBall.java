package com.nyfaria.nyfspetshop.entity.ai;

import com.nyfaria.nyfspetshop.entity.ifaces.Fetcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;

public class FetchBall<E extends TamableAnimal & Fetcher> extends FollowEntity<E, Entity> {
    protected ThrowableItemProjectile owner = null;

    public FetchBall() {
        following(this::getBall);
        speedMod(1.5f);
        stopFollowingWithin(1);
    }

    @Override
    protected void stop(E entity) {
        if (owner != null && !owner.isRemoved()) {
            double min = followDistMin.apply(entity, owner);
            if(entity.distanceToSqr(owner) <= min * min) {
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