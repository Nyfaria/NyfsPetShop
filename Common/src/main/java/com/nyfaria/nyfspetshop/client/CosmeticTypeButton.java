package com.nyfaria.nyfspetshop.client;

import com.nyfaria.nyfspetshop.init.CosmeticRegistry;
import com.nyfaria.nyfspetshop.network.packetsc2s.SelectCosmeticPacket;
import commonnetwork.api.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CosmeticTypeButton extends AbstractButton {

    private CosmeticRegistry.Type type;
    private GroomingScreen screen;

    public CosmeticTypeButton(GroomingScreen screen,int pX, int pY, CosmeticRegistry.Type type, Component pMessage) {
        super(pX, pY, 88, 27, pMessage);
        this.type = type;
        this.screen = screen;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        List<Holder<Item>> bop = BuiltInRegistries.ITEM.getTag(ItemTags.WOOL).get().stream().toList();
        int size = bop.size();
        int ticksPerFrame = 10;
        long gameTime = Minecraft.getInstance().level.getGameTime();
        int frame = (int) ((gameTime % (ticksPerFrame * size)) / ticksPerFrame);
        ItemStack stack = new ItemStack(bop.get(frame), type.getWoolCost());
        guiGraphics.renderItem(stack, getX() + 67 ,getY() + 5);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font,stack, getX() + 67 ,getY() + 5);
    }

    @Override
    public void onPress() {
        this.screen.getMenu().setCurrentType(type);
        Network.getNetworkHandler().sendToServer(new SelectCosmeticPacket(type));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
