package com.nyfaria.nyfspetshop.init;

import com.google.common.collect.ImmutableSet;
import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.List;
import java.util.function.Supplier;

public class POIInit {
    public static final RegistrationProvider<PoiType> POI_TYPES = RegistrationProvider.get(Registries.POINT_OF_INTEREST_TYPE, Constants.MODID);

    public static final RegistryObject<PoiType> PET_BOWL = POI_TYPES.register("pet_bowl", () -> new PoiType(BlockInit.pet_bowls.stream().map(Supplier::get).flatMap((p_218093_) -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), 1, 1));
    public static final RegistryObject<PoiType> GROOMING_STATION = POI_TYPES.register("grooming_station", () -> new PoiType(List.of(BlockInit.GROOMING_STATION).stream().map(Supplier::get).flatMap((p_218093_) -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), 1, 1));
    public static final RegistryObject<PoiType> CRATES = POI_TYPES.register("crates", () -> new PoiType(List.of(BlockInit.CRATE).stream().map(Supplier::get).flatMap((p_218093_) -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), 1, 1));
    public static final RegistryObject<PoiType> PET_BEDS = POI_TYPES.register("pet_beds", () -> new PoiType(List.of(BlockInit.PET_BED,BlockInit.BIG_PET_BED).stream().map(Supplier::get).flatMap((p_218093_) -> p_218093_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), 1, 1));

    public static void loadClass() {

    }

}
