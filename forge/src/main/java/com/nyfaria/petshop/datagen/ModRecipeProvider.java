package com.nyfaria.petshop.datagen;

import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.TagInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;

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
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockInit.GROOMING_STATION.get())
                .pattern("PWP")
                .pattern("PSP")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('W', ItemTags.WOOL)
                .define('S', Items.SHEARS)
                .unlockedBy("has_shears", has(Items.SHEARS))
                .save(recipeSaver);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BlockInit.CRATE.get())
                .pattern("PWP")
                .pattern("PIP")
                .pattern("PPP")
                .define('I', Items.IRON_BARS)
                .define('P', ItemTags.PLANKS)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(recipeSaver);
    }
}
