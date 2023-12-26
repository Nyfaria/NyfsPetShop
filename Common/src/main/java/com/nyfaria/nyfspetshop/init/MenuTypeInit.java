package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.menu.groomingstation.GroomingStationMenu;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeInit {
    public static void loadClass(){
    }
    public static RegistrationProvider<MenuType<?>> MENU_TYPES = RegistrationProvider.get(Registries.MENU, Constants.MODID);
    public static RegistryObject<MenuType<GroomingStationMenu>> GROOMING_STATION = MENU_TYPES.register("grooming_station", () -> new MenuType<>(GroomingStationMenu::new, FeatureFlags.VANILLA_SET));
}
