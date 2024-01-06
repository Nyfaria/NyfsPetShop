package com.nyfaria.petshop.datagen;

import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.TagInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.DyeItem;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput generator) {
        super(generator);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeSaver) {
        BlockInit.pet_bowls.forEach(
                block -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, block.get(), 1)
                        .requires(TagInit.PET_BOWLS_ITEM)
                        .requires(DyeItem.byColor(((PetBowl) block.get()).getColor()))
                        .unlockedBy("has_item", has(TagInit.PET_BOWLS_ITEM))
                        .save(recipeSaver, getItemName(block.get()))
        );
    }
}
