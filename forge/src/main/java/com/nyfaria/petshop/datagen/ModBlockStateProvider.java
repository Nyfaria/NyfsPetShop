package com.nyfaria.petshop.datagen;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.block.PetDoorBlock;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.BlockStateInit;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        simpleBlock(BlockInit.GROOMING_STATION.get(), blockSidedModel(BlockInit.GROOMING_STATION.get()).texture("particle", modLoc("block/grooming_station_side_1")));
//        petBowl(BlockInit.PET_BOWL.get());
        horizontalBlock(BlockInit.CRATE.get(), directionallySidedModel(BlockInit.CRATE.get()).renderType("cutout").texture("particle", modLoc("block/crate_side_e")));

        getVariantBuilder(BlockInit.BIG_PET_BED.get()).forAllStatesExcept(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/big_pet_bed_" + state.getValue(BlockStateInit.CORNER).getSerializedName())))
                        .build());
        getVariantBuilder(BlockInit.BIRD_CAGE.get()).forAllStatesExcept(state ->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/bird_cage_" + state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF).getSerializedName())))
                        .build());
        customModelBlock(BlockInit.PET_BED.get());


        petDoor(BlockInit.OAK_PET_DOOR.get());
        petDoor(BlockInit.SPRUCE_PET_DOOR.get());
        petDoor(BlockInit.BIRCH_PET_DOOR.get());
        petDoor(BlockInit.JUNGLE_PET_DOOR.get());
        petDoor(BlockInit.ACACIA_PET_DOOR.get());
        petDoor(BlockInit.DARK_OAK_PET_DOOR.get());
        petDoor(BlockInit.MANGROVE_PET_DOOR.get());
        petDoor(BlockInit.CHERRY_PET_DOOR.get());
        petDoor(BlockInit.BAMBOO_PET_DOOR.get());
        petDoor(BlockInit.CRIMSON_PET_DOOR.get());
        petDoor(BlockInit.WARPED_PET_DOOR.get());
        petDoor(BlockInit.WHITE_PET_DOOR.get());
        petDoor(BlockInit.ORANGE_PET_DOOR.get());
        petDoor(BlockInit.MAGENTA_PET_DOOR.get());
        petDoor(BlockInit.LIGHT_BLUE_PET_DOOR.get());
        petDoor(BlockInit.YELLOW_PET_DOOR.get());
        petDoor(BlockInit.LIME_PET_DOOR.get());
        petDoor(BlockInit.PINK_PET_DOOR.get());
        petDoor(BlockInit.GRAY_PET_DOOR.get());
        petDoor(BlockInit.LIGHT_GRAY_PET_DOOR.get());
        petDoor(BlockInit.CYAN_PET_DOOR.get());
        petDoor(BlockInit.PURPLE_PET_DOOR.get());
        petDoor(BlockInit.BLUE_PET_DOOR.get());
        petDoor(BlockInit.BROWN_PET_DOOR.get());
        petDoor(BlockInit.GREEN_PET_DOOR.get());
        petDoor(BlockInit.RED_PET_DOOR.get());
        petDoor(BlockInit.BLACK_PET_DOOR.get());


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

    protected void petDoor(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean open = state.getValue(PetDoorBlock.OPEN);
            return ConfiguredModel.builder()
                    .modelFile(models().withExistingParent(getName(block) + (open ? "_open" : "_closed")  ,modLoc("block/pet_door" + (open ? "_open" : "_closed")))
                            .texture("0", modLoc("block/" + getName(block)))
                            .texture("particle", modLoc("block/" + getName(block)))
                    )
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}
