package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class TagInit {
    public static TagKey<PoiType> PET_BOWLS = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(Constants.MODID, "pet_bowls"));
}
