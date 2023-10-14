package com.nyfaria.nyfspetshop.init;

import com.google.common.collect.ImmutableSet;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerInit {
    public static RegistrationProvider<VillagerProfession> VILLAGER_PROFESSIONS = RegistrationProvider.get(Registries.VILLAGER_PROFESSION, Constants.MODID);

    public static RegistryObject<VillagerProfession> PET_SHOP = VILLAGER_PROFESSIONS.register("pet_shop", () -> new VillagerProfession("pet_shop",
                    (block) -> block.is(POIInit.PET_BOWL.getResourceKey()),
                    (block) -> block.is(POIInit.PET_BOWL.getResourceKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    null
            )
    );

    public static void loadClass() {
    }
}
