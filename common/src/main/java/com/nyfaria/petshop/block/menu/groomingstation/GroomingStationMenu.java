package com.nyfaria.petshop.block.menu.groomingstation;

import com.nyfaria.petshop.init.CosmeticRegistry;
import com.nyfaria.petshop.init.MenuTypeInit;
import com.nyfaria.petshop.item.PetItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GroomingStationMenu extends AbstractContainerMenu {
    private final Player player;

    private final ContainerLevelAccess access;
    private final GroomingContainer craftSlots;
    private final GroomingResultSlot resultSlots;
    private CosmeticRegistry.Type currentType;

    public GroomingStationMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public GroomingStationMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(MenuTypeInit.GROOMING_STATION.get(), pContainerId);
        this.craftSlots = new GroomingContainer(this, pAccess);
        this.access = pAccess;
        this.player = pPlayerInventory.player;
        this.addSlot(new Slot(this.craftSlots, 0, 108 + 18, 16) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.getItem() instanceof PetItem && pStack.getTag().contains("inside") && pStack.getTag().getBoolean("inside");
            }
        });
        this.addSlot(new Slot(this.craftSlots, 1, 108 + 18, 54) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.is(ItemTags.WOOL) || pStack.is(Items.SHEARS) || pStack.getItem() instanceof DyeItem;
            }
        });
        resultSlots = new GroomingResultSlot(this, craftSlots, 2, 108 + 7 * 18, 35);
        this.addSlot(resultSlots);


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 108 + k * 18, 142));
        }

    }

    public Player getPlayer() {
        return player;
    }

    public CosmeticRegistry.Type getCurrentType() {
        return currentType;
    }

    public void setCurrentType(CosmeticRegistry.Type currentType) {
        this.currentType = currentType;
        if (!this.getSlot(1).getItem().isEmpty() && !this.getSlot(0).getItem().isEmpty()) {
            ItemStack stack = PetItem.setCosmetic(this.getSlot(0).getItem().copy(), currentType, this.getSlot(1).getItem(), player.level());
            this.craftSlots.setItem(2, stack);
        }
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        if (!pPlayer.level().isClientSide) {
            this.clearContainer(pPlayer, this.craftSlots);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
