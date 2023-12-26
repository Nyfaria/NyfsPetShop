package com.nyfaria.nyfspetshop.client;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.menu.groomingstation.GroomingStationMenu;
import com.nyfaria.nyfspetshop.entity.BaseDog;
import com.nyfaria.nyfspetshop.entity.BasePet;
import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.init.CosmeticRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GroomingScreen extends AbstractContainerScreen<GroomingStationMenu> {
    private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation(Constants.MODID, "textures/gui/grooming.png");
    private LivingEntity renderEntity = null;
    private List<Button> cosmeticButtons = new ArrayList<>();

    public GroomingScreen(GroomingStationMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(VILLAGER_LOCATION, i, j, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
    }

    @Override
    protected void slotClicked(Slot $$0, int $$1, int $$2, ClickType $$3) {
        super.slotClicked($$0, $$1, $$2, $$3);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if(renderEntity != null){
            cosmeticButtons.forEach(this::removeWidget);

            cosmeticButtons.clear();
            final Integer[] x = {0};
            CosmeticRegistry.COSMETIC_MAP.get(renderEntity.getType()).forEach(
                    type -> {
                        Button button = Button.builder(Component.literal(type.getName()),(a)->{

                        }).pos(0,x[0]*20).build();
                        cosmeticButtons.add(button);
                        this.addRenderableWidget(button);
                        x[0] = x[0] + 1;
                    }
            );
        } else {
            if(!cosmeticButtons.isEmpty()) {
                cosmeticButtons.forEach(this::removeWidget);
                cosmeticButtons.clear();
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        float f = (float) Math.atan(mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        ItemStack petItem = this.menu.getSlot(0).getItem();
        if (petItem.hasTag() && petItem.getTag().contains("entityType")) {
            BasePet entity = (BasePet) BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(petItem.getTag().getString("entityType"))).create(Minecraft.getInstance().level);
            if (renderEntity == null || renderEntity.getId() != entity.getId()) {
                if(entity instanceof BaseDog dog){
                    dog.setMovementType(MovementType.WANDER);
                }
                renderEntity = entity;
            }
            entity.load(petItem.getTag().getCompound("petData"));
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, this.leftPos + 188, this.topPos + 68, 30, f, f1, renderEntity);
        }
        if(petItem.isEmpty()){
            renderEntity = null;
        }
//        guiGraphics.drawCenteredString(this.font,mouseX + "," + mouseY, mouseX, mouseY, 0xFFFFFF);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
