package com.nyfaria.petshop.server;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.mixin.StructureTemplatePoolAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;


public class ServerClass {
    public static final ResourceLocation PET_SHOP = new ResourceLocation(Constants.MODID, "pet_shop");
    public static final ResourceLocation PET_SHOP_ROAD = new ResourceLocation(Constants.MODID, "pet_shop_road");

    public static void addStructures(final MinecraftServer server) {
        Registry<StructureTemplatePool> templatePoolRegistry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

        SinglePoolElement petShop = StructurePoolElement.single(PET_SHOP.toString()).apply(StructureTemplatePool.Projection.RIGID);
        SinglePoolElement petShopRoad = StructurePoolElement.single(PET_SHOP_ROAD.toString()).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING);

        addStructureToPool(templatePoolRegistry.get(new ResourceLocation("village/plains/streets")), petShopRoad, 5);
//        addStructureToPool(templatePoolRegistry.get(new ResourceLocation("village/plains/houses")), petShop, 100);
    }

    private static <T extends StructurePoolElement> void addStructureToPool(StructureTemplatePool templatePool, T poolElement, int weight) {
        if (templatePool == null) {
            return;
        }

        StructureTemplatePoolAccessor accessor = (StructureTemplatePoolAccessor) templatePool;
        for (int i = 0; i < weight; i++) {
            accessor.getTemplates().add(poolElement);
        }

        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(accessor.getRawTemplates());
        rawTemplates.add(Pair.of(poolElement, weight));
        accessor.setRawTemplates(rawTemplates);
    }
}
