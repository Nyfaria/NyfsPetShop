package com.nyfaria.nyfspetshop.init;

import com.google.common.collect.ImmutableSet;
import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillagerInit {
    public static RegistrationProvider<VillagerProfession> VILLAGER_PROFESSIONS = RegistrationProvider.get(Registries.VILLAGER_PROFESSION, Constants.MODID);

    public static RegistryObject<VillagerProfession> PET_ITEMS = VILLAGER_PROFESSIONS.register("pet_items", () -> new VillagerProfession("pet_items",
                    (block) -> block.value() == POIInit.GROOMING_STATION.get(),
                    (block) -> block.value() == POIInit.GROOMING_STATION.get(),
                    ImmutableSet.of(ItemInit.TENNIS_BALL.get()),
                    ImmutableSet.of(BlockInit.GROOMING_STATION.get()),
                    null
            )
    );
    public static RegistryObject<VillagerProfession> PETS = VILLAGER_PROFESSIONS.register("pets", () -> new VillagerProfession("pets",
                    (block) -> block.value() == POIInit.CRATES.get(),
                    (block) -> block.value() == POIInit.CRATES.get(),
                    ImmutableSet.of(ItemInit.PET_ITEM.get()),
                    ImmutableSet.of(BlockInit.CRATE.get()),
                    null
            )
    );

    public static void loadClass() { }
    public static void loadTrades() {
        List<VillagerTrades.ItemListing> trades = new ArrayList<>();
        EntityInit.SPECIES_MAP.forEach((species,entity)-> {
            ItemStack stack = new ItemStack(ItemInit.PET_ITEM.get());
            stack.getOrCreateTag().putString("entityType", BuiltInRegistries.ENTITY_TYPE.getKey(entity.get()).toString());
            stack.getTag().putString("pet_type", species.getName());
            trades.add(new ItemsForItems(new ItemStack(Items.EMERALD,15),ItemStack.EMPTY,stack , 1, 1, 1));
        });
        Map<Integer,VillagerTrades.ItemListing[]> theMap = Map.of(
                1, trades.toArray(new VillagerTrades.ItemListing[trades.size()]),
                2,trades.toArray(new VillagerTrades.ItemListing[trades.size()]),
                3,trades.toArray(new VillagerTrades.ItemListing[trades.size()]),
                4,trades.toArray(new VillagerTrades.ItemListing[trades.size()]),
                5,trades.toArray(new VillagerTrades.ItemListing[trades.size()])
        );
        VillagerTrades.TRADES.put(PETS.get(), new Int2ObjectOpenHashMap<>(theMap));
        List<VillagerTrades.ItemListing> trades2 = new ArrayList<>();
        trades2.add(new ItemsForItems(new ItemStack(Items.EMERALD,1),ItemStack.EMPTY,new ItemStack(ItemInit.TENNIS_BALL.get()) , 20, 1, 1));
        trades2.add(new ItemsForItems(new ItemStack(Items.EMERALD,5),ItemStack.EMPTY,new ItemStack(ItemInit.BAG_OF_KIBBLE.get()) , 20, 1, 1));
        trades2.add(new ItemsForItems(new ItemStack(Items.EMERALD,1),ItemStack.EMPTY,new ItemStack(ItemInit.DOG_TREAT.get()) , 20, 1, 1));
        trades2.add(new ItemsForItems(new ItemStack(Items.EMERALD,1),ItemStack.EMPTY,new ItemStack(ItemInit.TUNA_TREAT.get()) , 20, 1, 1));
        trades2.add(new ItemsForItems(new ItemStack(Items.EMERALD,1),ItemStack.EMPTY,new ItemStack(ItemInit.PEANUT.get()) , 20, 1, 1));
        Map<Integer,VillagerTrades.ItemListing[]> theSecondMap = Map.of(
                1, trades2.toArray(new VillagerTrades.ItemListing[trades2.size()]),
                2,trades2.toArray(new VillagerTrades.ItemListing[trades2.size()]),
                3,trades2.toArray(new VillagerTrades.ItemListing[trades2.size()]),
                4,trades2.toArray(new VillagerTrades.ItemListing[trades2.size()]),
                5,trades2.toArray(new VillagerTrades.ItemListing[trades2.size()])
        );
        VillagerTrades.TRADES.put(PET_ITEMS.get(), new Int2ObjectOpenHashMap<>(theSecondMap));


    }
    static class ItemsForItems implements VillagerTrades.ItemListing {
        private final ItemStack priceA;
        private final ItemStack priceB;
        private final ItemStack stack;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public ItemsForItems(ItemStack priceA, ItemStack priceB, ItemStack stack, int maxUses, int experience, float multiplier) {
            this.priceA = priceA;
            this.priceB = priceB;
            this.stack = stack;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            return new MerchantOffer(priceA, priceB, stack, maxUses, experience, multiplier);
        }
    }
}
