package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagInit {
    public static TagKey<PoiType> PET_BOWLS_POI = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(Constants.MODID, "pet_bowls"));
    public static TagKey<Block> PET_BOWLS = TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MODID, "pet_bowls"));
    public static TagKey<Item> PET_BOWLS_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MODID, "pet_bowls"));
}
