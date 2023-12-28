package com.nyfaria.nyfspetshop.entity.ifaces;

import com.nyfaria.nyfspetshop.entity.BasePet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface ShoulderRider<T extends BasePet> {

    default boolean setEntityOnShoulder(T basePet, ServerPlayer pPlayer) {
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
