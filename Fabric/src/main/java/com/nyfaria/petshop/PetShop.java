package com.nyfaria.petshop;

import com.nyfaria.petshop.init.EntityInit;
import com.nyfaria.petshop.init.POIInit;
import com.nyfaria.petshop.init.VillagerInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class PetShop implements ModInitializer {


    @Override
    public void onInitialize() {
        CommonClass.init();
        EntityInit.attributeSuppliers.forEach(
                p -> FabricDefaultAttributeRegistry.register(p.entityTypeSupplier().get(), p.factory().get().build())
        );
        PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(POIInit.PET_BEDS.get()), POIInit.PET_BEDS.get().matchingStates());
        PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(POIInit.PET_BOWL.get()), POIInit.PET_BOWL.get().matchingStates());
        PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(POIInit.GROOMING_STATION.get()), POIInit.GROOMING_STATION.get().matchingStates());
        PoiTypes.registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(POIInit.CRATES.get()), POIInit.CRATES.get().matchingStates());
        VillagerInit.loadTrades();
    }

}
