package com.nyfaria.nyfspetshop.datagen;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Stream.of(
                        ItemInit.BAG_OF_KIBBLE
                )
                .map(Supplier::get)
                .forEach(this::simpleHandHeldModel);

        Stream.of(
                        ItemInit.TENNIS_BALL
                )
                .map(Supplier::get)
                .forEach(this::simpleGeneratedModel);

        BlockInit.pet_bowls.stream()
                .map(Supplier::get)
                .forEach(this::petBowl);
        Stream.of(
                        BlockInit.GROOMING_STATION
                ).map(Supplier::get)
                .forEach(this::simpleBlockItemModel);
        petItem(ItemInit.PET_ITEM.get());
    }

    protected ItemModelBuilder simpleBlockItemModel(Block block) {
        String name = getName(block);
        return withExistingParent(name, modLoc("block/" + name));
    }

    protected ItemModelBuilder petBowl(Block block) {
        String name = getName(block);
        return withExistingParent(name, modLoc("block/pet_bowl"));
    }

    protected ItemModelBuilder simpleGeneratedModel(Item item) {
        return simpleModel(item, mcLoc("item/generated"));
    }

    protected ItemModelBuilder simpleHandHeldModel(Item item) {
        return simpleModel(item, mcLoc("item/handheld"));
    }

    protected ItemModelBuilder simpleModel(Item item, ResourceLocation parent) {
        String name = getName(item);
        return singleTexture(name, parent, "layer0", modLoc("item/" + name));
    }

    protected String getName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }

    protected ItemModelBuilder petItem(Item item) {
        return withExistingParent(getName(item), mcLoc("item/generated"))
                .override().predicate(new ResourceLocation(Constants.MODID,"type"), 0.1f)
                .model(singleTexture(getName(item) + "_bone", mcLoc("item/generated"),"layer0", modLoc("item/" + getName(item) + "_bone"))).end()
                .override().predicate(new ResourceLocation(Constants.MODID,"type"), 0.2f)
                .model(singleTexture(getName(item)+ "_fish", mcLoc("item/generated"),"layer0", modLoc("item/" + getName(item) + "_fish"))).end()
                .override().predicate(new ResourceLocation(Constants.MODID,"type"), 0.3f)
                .model(singleTexture(getName(item)+ "_seed", mcLoc("item/generated"),"layer0", modLoc("item/" + getName(item) + "_seed"))).end();
    }
}
