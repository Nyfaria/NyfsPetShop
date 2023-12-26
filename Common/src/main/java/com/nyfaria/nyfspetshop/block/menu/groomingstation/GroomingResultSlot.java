package com.nyfaria.nyfspetshop.block.menu.groomingstation;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class GroomingResultSlot extends Slot {
    private int removeCount;
    private final GroomingContainer slots;
    public GroomingResultSlot(GroomingContainer container, int slot, int xPos, int yPos) {
        super(container, slot, xPos, yPos);
        this.slots = container;
    }

    @Override
    public boolean mayPlace(ItemStack $$0) {
        return false;
    }
    public ItemStack remove(int pAmount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(pAmount, this.getItem().getCount());
        }

        return super.remove(pAmount);
    }
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }
    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        this.slots.setItem(0, ItemStack.EMPTY);
        this.slots.setItem(1, ItemStack.EMPTY);

    }
}
