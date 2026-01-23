package com.nyfaria.petshop.init;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.block.menu.groomingstation.GroomingStationMenu;
import com.nyfaria.petshop.registration.RegistrationProvider;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeInit {
    public static RegistrationProvider<MenuType<?>> MENU_TYPES = RegistrationProvider.get(Registries.MENU, Constants.MODID);

    public static void loadClass() {
    }
    public static RegistryObject<MenuType<GroomingStationMenu>> GROOMING_STATION = MENU_TYPES.register("grooming_station", () -> new MenuType<>(GroomingStationMenu::new, FeatureFlags.VANILLA_SET));
}
