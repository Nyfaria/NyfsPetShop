package com.nyfaria.petshop.entity.ifaces;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public interface Fetcher {

    ThrowableItemProjectile getFetchTarget();

    void setFetchTarget(ThrowableItemProjectile entity);
}
