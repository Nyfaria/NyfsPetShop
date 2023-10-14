package com.nyfaria.nyfspetshop.entity;

import com.nyfaria.nyfspetshop.entity.ifaces.Fetcher;
import com.nyfaria.nyfspetshop.init.EntityInit;
import com.nyfaria.nyfspetshop.init.ItemInit;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownBall extends ThrowableItemProjectile {
    public ThrownBall(EntityType<? extends ThrownBall> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ThrownBall(Level $$0, LivingEntity $$1) {
        super(EntityInit.BALL.get(), $$1, $$0);
    }

    public ThrownBall(Level $$0, double $$1, double $$2, double $$3) {
        super(EntityInit.BALL.get(), $$1, $$2, $$3, $$0);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        Vec3 bop = getDeltaMovement();
        if (pResult.getDirection() == Direction.UP || pResult.getDirection() == Direction.DOWN) {
            setDeltaMovement(bop.x * 0.5, -bop.y * 0.5, bop.z * 0.5);
        } else {
            setDeltaMovement(-bop.x * 0.5, bop.y * 0.5, -bop.z * 0.5);
        }
        this.level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(30)).forEach(fetcher -> {
            if (fetcher instanceof Fetcher fetcher1)
                fetcher1.setFetchTarget(this);
        });
    }

    public void playerTouch(Player pEntity) {
        if (!this.level().isClientSide && this.tickCount > 5) {
            pEntity.getInventory().add(getItem());
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof Player player) {
            player.getInventory().add(getItem());
            discard();
        }
    }

    protected Item getDefaultItem() {
        return ItemInit.TENNIS_BALL.get();
    }
}
