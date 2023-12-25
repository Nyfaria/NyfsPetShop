package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.block.PetBowl;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockStateInit {
    public static void loadClass() {
    }

    public static final EnumProperty<PetBowl.Type> BOWL_TYPE = EnumProperty.create("bowl_type", PetBowl.Type.class);
}
