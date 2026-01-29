package com.nyfaria.petshop.entity;

import com.nyfaria.petshop.init.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DragonFireball extends Fireball {
    private static final EntityDataAccessor<String> DRAGON_TYPE = SynchedEntityData.defineId(DragonFireball.class, EntityDataSerializers.STRING);

    public DragonFireball(EntityType<? extends DragonFireball> entityType, Level level) {
        super(entityType, level);
    }

    public DragonFireball(Level level, LivingEntity owner, double dx, double dy, double dz) {
        super(EntityInit.DRAGON_FIREBALL.get(), owner, dx, dy, dz, level);
        if (owner != null) {
            String typeName = EntityType.getKey(owner.getType()).getPath();
            setDragonType(typeName);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DRAGON_TYPE, "aurora_dragon");
    }

    public String getDragonType() {
        return this.entityData.get(DRAGON_TYPE);
    }

    public void setDragonType(String dragonType) {
        this.entityData.set(DRAGON_TYPE, dragonType);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("DragonType", getDragonType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DragonType")) {
            setDragonType(tag.getString("DragonType"));
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!this.level().isClientSide) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            DamageSource damageSource = this.damageSources().fireball(this, owner);
            entity.hurt(damageSource, 1.0F);
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 0.95F;
    }
}
