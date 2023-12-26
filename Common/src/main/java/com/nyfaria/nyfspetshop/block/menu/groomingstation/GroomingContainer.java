package com.nyfaria.nyfspetshop.block.menu.groomingstation;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GroomingContainer implements Container {
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = this.items.get(pIndex);
        if (pIndex == 2 && !itemstack.isEmpty()) {
            return ContainerHelper.removeItem(this.items, pIndex, itemstack.getCount());
        } else {
            ItemStack itemstack1 = ContainerHelper.removeItem(this.items, pIndex, pCount);
            if (!itemstack1.isEmpty() && this.isPaymentSlot(pIndex)) {
                this.updateResultItem();
            }

            return itemstack1;
        }
    }

    private boolean isPaymentSlot(int pIndex) {
        return pIndex == 1;
    }

    private void updateResultItem() {

    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (items != null && !items.get(i).isEmpty()) {
            ItemStack itemstack = items.get(i);
            items.set(i, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.items.set(i, itemStack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
