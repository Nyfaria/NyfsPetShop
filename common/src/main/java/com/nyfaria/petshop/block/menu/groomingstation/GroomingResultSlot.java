package com.nyfaria.petshop.block.menu.groomingstation;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GroomingResultSlot extends Slot {
    private final GroomingContainer slots;
    private final GroomingStationMenu parent;
    private int removeCount;

    public GroomingResultSlot(GroomingStationMenu parent, GroomingContainer container, int slot, int xPos, int yPos) {
        super(container, slot, xPos, yPos);
        this.slots = container;
        this.parent = parent;

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
        if (parent.getCurrentType() != null) {
            if (this.slots.getItem(1).is(Items.SHEARS)) {
                pPlayer.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
                this.slots.getItem(1).hurtAndBreak(parent.getCurrentType().getWoolCost(), pPlayer, (p_150569_) -> {
                    p_150569_.broadcastBreakEvent(pPlayer.getUsedItemHand());
                });
            } else {
                this.slots.getItem(1).shrink(parent.getCurrentType().getWoolCost());
            }
        }
    }
}
