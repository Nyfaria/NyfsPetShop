package com.nyfaria.nyfspetshop.client;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.ItemInit;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
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


    public static void itemModelProperties() {
        ClampedItemPropertyFunction clampeditempropertyfunction = (itemStack, level, livingEntity, i) -> {
            if(itemStack.getTag().contains("pet_type")){
                if(itemStack.getTag().getString("pet_type").equals("dog")){
                    return 0.1F;
                } else if (itemStack.getTag().getString("pet_type").equals("cat")){
                    return 0.2F;
                } else if (itemStack.getTag().getString("pet_type").equals("bird")){
                    return 0.3F;
                }
            }
            return 0.1f;
        };
        ItemProperties.register(ItemInit.PET_ITEM.get(), new ResourceLocation(Constants.MODID,"type"), clampeditempropertyfunction);
    }

}
