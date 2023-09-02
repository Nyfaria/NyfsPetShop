package com.nyfaria.nyfspetshop.init;

import com.google.common.collect.ImmutableSet;
import com.nyfaria.nyfsmultiloader.registration.RegistrationProvider;
import com.nyfaria.nyfsmultiloader.registration.RegistryObject;
import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;

import java.util.function.Supplier;

public class POIInit {
    public static final RegistrationProvider<PoiType> POI_TYPES = RegistrationProvider.get(Registries.POINT_OF_INTEREST_TYPE, Constants.MODID);

    public static final RegistryObject<PoiType> PET_BOWL = POI_TYPES.register("pet_bowl", () -> new PoiType(BlockInit.pet_bowls.stream().map(Supplier::get).flatMap((p_218093_) -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), 0, 1));

    public static void loadClass() {
    }

}
