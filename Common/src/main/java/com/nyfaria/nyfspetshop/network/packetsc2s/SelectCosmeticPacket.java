package com.nyfaria.nyfspetshop.network.packetsc2s;

import com.nyfaria.nyfspetshop.block.menu.groomingstation.GroomingStationMenu;
import com.nyfaria.nyfspetshop.init.CosmeticRegistry;
import com.nyfaria.nyfspetshop.item.PetItem;
import commonnetwork.Constants;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record SelectCosmeticPacket(CosmeticRegistry.Type type) {
    public static final ResourceLocation LOCATION = new ResourceLocation(Constants.MOD_ID, "set_cosmetic");


    public static SelectCosmeticPacket decode(FriendlyByteBuf buf) {
        return new SelectCosmeticPacket(buf.readEnum(CosmeticRegistry.Type.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
    }

    public static void handle(PacketContext<SelectCosmeticPacket> ctx) {
        if (!Side.CLIENT.equals(ctx.side())) {
            if (ctx.sender().containerMenu instanceof GroomingStationMenu menu) {
                menu.setCurrentType(ctx.message().type());
            }
        }
    }
}