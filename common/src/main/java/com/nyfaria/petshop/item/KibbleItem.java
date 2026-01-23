package com.nyfaria.petshop.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class KibbleItem extends Item {
    private final Type type;

    public KibbleItem(Type type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public static void doKibbleAction(ItemStack stack, Player player) {
        KibbleItem item = (KibbleItem) stack.getItem();
        switch (item.type) {
            case BAG:
                stack.hurtAndBreak(1,
                        player,
                        (p) -> p.broadcastBreakEvent(p.getUsedItemHand())
                );
                break;
            case SCOOP:
                break;
        }
    }

    public enum Type {
        BAG("large_bag"),
        SCOOP("medium_bag");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
