package com.nyfaria.petshop.entity.ifaces;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public interface YarnPlayer {

    ThrowableItemProjectile getYarnTarget();

    void setYarnTarget(ThrowableItemProjectile entity);

    boolean isPlayingWithYarn();

    void setPlayingWithYarn(boolean playing);

    int getYarnPlayTime();

    void setYarnPlayTime(int time);
}
