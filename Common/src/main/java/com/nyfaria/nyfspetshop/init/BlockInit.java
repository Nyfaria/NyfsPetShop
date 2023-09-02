package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfsmultiloader.registration.RegistrationProvider;
import com.nyfaria.nyfsmultiloader.registration.RegistryObject;
import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class BlockInit {

    public static List<RegistryObject<Block>> pet_bowls = new ArrayList<>();
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);


    public static void loadClass() {
    }
}
