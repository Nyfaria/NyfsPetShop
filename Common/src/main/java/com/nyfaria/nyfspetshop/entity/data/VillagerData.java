package com.nyfaria.nyfspetshop.entity.data;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class VillagerData {

//    int petShopTrades = toIntMap(
//            ImmutableMap.of(
//                    1, new VillagerTrades.ItemListing[]
//                            {
//                                    new VillagerTrades.EmeraldForItems(Items.WHEAT, 20, 16, 2),
//                                    new VillagerTrades.EmeraldForItems(Items.POTATO, 26, 16, 2),
//                                    new VillagerTrades.EmeraldForItems(Items.CARROT, 22, 16, 2),
//                                    new VillagerTrades.EmeraldForItems(Items.BEETROOT, 15, 16, 2),
//                                    new VillagerTrades.ItemsForEmeralds(Items.BREAD, 1, 6, 16, 1)},
//                    2, new VillagerTrades.ItemListing[]
//                            {
//                                    new VillagerTrades.EmeraldForItems(Blocks.PUMPKIN, 6, 12, 10),
//                                    new VillagerTrades.ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5),
//                                    new VillagerTrades.ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5)},
//                    3, new VillagerTrades.ItemListing[]
//                            {
//                                    new VillagerTrades.ItemsForEmeralds(Items.COOKIE, 3, 18, 10),
//                                    new VillagerTrades.EmeraldForItems(Blocks.MELON, 4, 12, 20)
//                            },
//                    4, new VillagerTrades.ItemListing[]
//                            {
//                                    new VillagerTrades.ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.JUMP, 160, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.WEAKNESS, 140, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.BLINDNESS, 120, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.POISON, 280, 15),
//                                    new VillagerTrades.SuspiciousStewForEmerald(MobEffects.SATURATION, 7, 15)
//                            },
//                    5, new VillagerTrades.ItemListing[]
//                            {
//                                    new VillagerTrades.ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30),
//                                    new VillagerTrades.ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30)
//                            }
//            )
//    );


    private static Int2ObjectMap<VillagerTrades.ItemListing[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> pMap) {
        return new Int2ObjectOpenHashMap<>(pMap);
    }
}
