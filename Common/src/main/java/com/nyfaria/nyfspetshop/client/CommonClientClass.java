package com.nyfaria.nyfspetshop.client;

import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public class CommonClientClass {
    public static Map<BlockColor, List<RegistryObject<? extends Block>>> blockColors = Map.of(
            (state, world, pos, tintIndex) -> {
                if(((PetBowl)state.getBlock()).getColor() == null){
                    return -1;
                }
                return ((PetBowl) state.getBlock()).getColor().getTextColor();
            }, BlockInit.pet_bowls
    );
    public static Map<ItemColor, List<RegistryObject<? extends Block>>> itemColors = Map.of(
            (state, tintIndex) -> {
                if(((PetBowl)((BlockItem)state.getItem()).getBlock()).getColor() == null){
                    return -1;
                }
                return ((PetBowl) ((BlockItem) state.getItem()).getBlock()).getColor().getTextColor();
            }, BlockInit.pet_bowls
    );


    public static void blockColors() {
    }

}
