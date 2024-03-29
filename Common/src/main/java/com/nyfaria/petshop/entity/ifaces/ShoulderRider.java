package com.nyfaria.petshop.entity.ifaces;

import com.nyfaria.petshop.entity.BasePet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface ShoulderRider<T extends BasePet> {

    default boolean setEntityOnShoulder(T basePet, ServerPlayer pPlayer) {
        basePet.getPetItemStack().getTag().putBoolean("onShoulder", true);
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("id", basePet.getPublicEncodeId());
        basePet.saveWithoutId(compoundtag);
        if (pPlayer.setEntityOnShoulder(compoundtag)) {
            basePet.discard();
            return true;
        } else {
            return false;
        }
    }

    boolean canSitOnShoulder();
}
