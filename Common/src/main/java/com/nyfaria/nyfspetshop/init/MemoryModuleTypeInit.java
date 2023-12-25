package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class MemoryModuleTypeInit {
    public static RegistrationProvider<MemoryModuleType<?>> MEMORY_MODULE_TYPES = RegistrationProvider.get(BuiltInRegistries.MEMORY_MODULE_TYPE, Constants.MODID);

    public static RegistryObject<MemoryModuleType<Boolean>> WAIT_TO_SEARCH_AGAIN = MEMORY_MODULE_TYPES.register("wait_to_search_again", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Optional<BlockPos>>> POTENTIAL_FOOD_BOWL = MEMORY_MODULE_TYPES.register("potential_food_bowl", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Optional<BlockPos>>> POTENTIAL_BED = MEMORY_MODULE_TYPES.register("potential_bed", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Optional<BlockPos>>> WATER_BOWL = MEMORY_MODULE_TYPES.register("water_bowl", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Optional<BlockPos>>> FOOD_BOWL = MEMORY_MODULE_TYPES.register("food_bowl", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Optional<BlockPos>>> BED = MEMORY_MODULE_TYPES.register("bed", () -> new MemoryModuleType<>(Optional.empty()));

    public static void loadClass() {}
}