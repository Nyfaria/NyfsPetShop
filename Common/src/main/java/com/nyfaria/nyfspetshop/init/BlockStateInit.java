package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.block.TBTBlock;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockStateInit {
    public static void loadClass() {
    }

    public static final EnumProperty<PetBowl.Type> BOWL_TYPE = EnumProperty.create("bowl_type", PetBowl.Type.class);
    public static final IntegerProperty FULLNESSITY = IntegerProperty.create("fullnessity", 0,3);
    public static final EnumProperty<TBTBlock.Corner> CORNER = EnumProperty.create("corner", TBTBlock.Corner.class);
}
