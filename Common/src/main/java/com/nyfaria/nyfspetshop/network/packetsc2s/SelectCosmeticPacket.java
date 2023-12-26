package com.nyfaria.nyfspetshop.network.packetsc2s;

import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.block.menu.groomingstation.GroomingStationMenu;
import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record SelectCosmeticPacket(PetBowl.Type type) {
    public static final ResourceLocation LOCATION = new ResourceLocation(Constants.MOD_ID, "set_cosmetic");


    public static SelectCosmeticPacket decode(FriendlyByteBuf buf) {
        return new SelectCosmeticPacket(buf.readEnum(PetBowl.Type.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
    }

    public static void handle(PacketContext<SelectCosmeticPacket> ctx) {
        if (!Side.CLIENT.equals(ctx.side())) {
          if(ctx.sender().containerMenu instanceof GroomingStationMenu menu){
          }
        }
    }
}
