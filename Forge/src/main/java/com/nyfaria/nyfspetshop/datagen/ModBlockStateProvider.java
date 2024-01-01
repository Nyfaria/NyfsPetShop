package com.nyfaria.nyfspetshop.datagen;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.BlockStateInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Stream.of(
        //
        //         )
        //         .map(Supplier::get)
        //         .forEach(this::simpleCubeBottomTopBlockState);
        //
        // Stream.of(
        //
        // ).map(Supplier::get)
        //         .forEach(this::simpleBlock);
        BlockInit.pet_bowls.forEach(
                block -> petBowl(block.get())
        );
        simpleBlock(BlockInit.GROOMING_STATION.get(),blockSidedModel(BlockInit.GROOMING_STATION.get()).texture("particle", modLoc("block/grooming_station_side_1")));
//        petBowl(BlockInit.PET_BOWL.get());
        horizontalBlock(BlockInit.CRATE.get(),directionallySidedModel(BlockInit.CRATE.get()).renderType("cutout").texture("particle", modLoc("block/crate_side_e")));

        getVariantBuilder(BlockInit.BIG_PET_BED.get()).forAllStatesExcept(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/big_pet_bed_" + state.getValue(BlockStateInit.CORNER).getSerializedName())))
                        .build());
        customModelBlock(BlockInit.PET_BED.get());
    }

    protected void simpleCubeBottomTopBlockState(Block block) {
        simpleBlock(block, blockCubeTopModel(block));
    }

    protected BlockModelBuilder blockCubeTopModel(Block block) {
        String name = getName(block);
        return models().cubeBottomTop(name, modLoc("block/" + name + "_side"), modLoc("block/" + name + "_bottom"), modLoc("block/" + name + "_top"));
    }

    protected BlockModelBuilder blockSidedModel(Block block) {
        String name = getName(block);
        return models().cube(name,
                modLoc("block/" + name + "_bottom"),
                modLoc("block/" + name + "_top"),
                modLoc("block/" + name + "_side_1"),
                modLoc("block/" + name + "_side_2"),
                modLoc("block/" + name + "_side_3"),
                modLoc("block/" + name + "_side_4")
        );
    }

    protected BlockModelBuilder directionallySidedModel(Block block) {
        String name = getName(block);
        return models().cube(name,
                modLoc("block/" + name + "_bottom"),
                modLoc("block/" + name + "_top"),
                modLoc("block/" + name + "_front"),
                modLoc("block/" + name + "_back"),
                modLoc("block/" + name + "_side_e"),
                modLoc("block/" + name + "_side_w")
        );
    }

    protected void customModelBlock(Block block) {
        simpleBlock(block, models().getExistingFile(modLoc("block/" + getName(block))));
    }

    protected void petBowl(Block block) {
        getVariantBuilder(block).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/" + ((state.getValue(BlockStateInit.FULLNESSITY) == 0 || state.getValue(BlockStateInit.BOWL_TYPE) == PetBowl.Type.EMPTY) ? "pet_bowl_empty" : "pet_bowl_" + state.getValue(BlockStateInit.BOWL_TYPE).getSerializedName() + "_" + state.getValue(BlockStateInit.FULLNESSITY)))))
                        .build());
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}
