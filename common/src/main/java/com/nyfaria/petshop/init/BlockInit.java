package com.nyfaria.petshop.init;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.block.*;
import com.nyfaria.petshop.block.entity.BirdCageBlockEntity;
import com.nyfaria.petshop.registration.RegistrationProvider;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {

    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);
    public static final RegistryObject<Block> GROOMING_STATION = registerBlock("grooming_station", () -> new GroomingStation(Block.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()));
    public static final RegistryObject<Block> CRATE = registerBlock("crate", () -> new BasicHorizontalBlock(Block.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()));
    public static final RegistryObject<Block> BIG_PET_BED = registerBlock("big_pet_bed", () -> new TBTBlock(Block.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()), (block) -> () -> new TBTHorizontalBlockItem(block.get(), ItemInit.getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Block> PET_BED = registerBlock("pet_bed", () -> new SmolBed(Block.Properties.copy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> BIRD_CAGE = registerBlock("bird_cage", () -> new BirdCage(Block.Properties.copy(Blocks.BROWN_WOOL).noOcclusion()), (block) -> () -> new DoubleHighBlockItem(block.get(), ItemInit.getItemProperties(Rarity.UNCOMMON)));
    public static List<RegistryObject<? extends Block>> pet_bowls = new ArrayList<>();
    public static final RegistryObject<Block> PET_BOWL = registerPetBowl("pet_bowl", () -> new PetBowl(DyeColor.WHITE, Block.Properties.copy(Blocks.RED_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_ORANGE = registerPetBowl("pet_bowl_orange", () -> new PetBowl(DyeColor.ORANGE, Block.Properties.copy(Blocks.ORANGE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_MAGENTA = registerPetBowl("pet_bowl_magenta", () -> new PetBowl(DyeColor.MAGENTA, Block.Properties.copy(Blocks.MAGENTA_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_LIGHT_BLUE = registerPetBowl("pet_bowl_light_blue", () -> new PetBowl(DyeColor.LIGHT_BLUE, Block.Properties.copy(Blocks.LIGHT_BLUE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_YELLOW = registerPetBowl("pet_bowl_yellow", () -> new PetBowl(DyeColor.YELLOW, Block.Properties.copy(Blocks.YELLOW_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_LIME = registerPetBowl("pet_bowl_lime", () -> new PetBowl(DyeColor.LIME, Block.Properties.copy(Blocks.LIME_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_PINK = registerPetBowl("pet_bowl_pink", () -> new PetBowl(DyeColor.PINK, Block.Properties.copy(Blocks.PINK_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_GRAY = registerPetBowl("pet_bowl_gray", () -> new PetBowl(DyeColor.GRAY, Block.Properties.copy(Blocks.GRAY_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_LIGHT_GRAY = registerPetBowl("pet_bowl_light_gray", () -> new PetBowl(DyeColor.LIGHT_GRAY, Block.Properties.copy(Blocks.LIGHT_GRAY_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_CYAN = registerPetBowl("pet_bowl_cyan", () -> new PetBowl(DyeColor.CYAN, Block.Properties.copy(Blocks.CYAN_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_PURPLE = registerPetBowl("pet_bowl_purple", () -> new PetBowl(DyeColor.PURPLE, Block.Properties.copy(Blocks.PURPLE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_BLUE = registerPetBowl("pet_bowl_blue", () -> new PetBowl(DyeColor.BLUE, Block.Properties.copy(Blocks.BLUE_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_BROWN = registerPetBowl("pet_bowl_brown", () -> new PetBowl(DyeColor.BROWN, Block.Properties.copy(Blocks.BROWN_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_GREEN = registerPetBowl("pet_bowl_green", () -> new PetBowl(DyeColor.GREEN, Block.Properties.copy(Blocks.GREEN_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_RED = registerPetBowl("pet_bowl_red", () -> new PetBowl(DyeColor.RED, Block.Properties.copy(Blocks.RED_WOOL).noOcclusion()));
    public static final RegistryObject<Block> PET_BOWL_BLACK = registerPetBowl("pet_bowl_black", () -> new PetBowl(DyeColor.BLACK, Block.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<PetDoorBlock> PET_DOOR = registerBlock("pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));

    public static final RegistryObject<PetDoorBlock> OAK_PET_DOOR = registerBlock("oak_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> SPRUCE_PET_DOOR = registerBlock("spruce_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.SPRUCE_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> BIRCH_PET_DOOR = registerBlock("birch_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.BIRCH_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> JUNGLE_PET_DOOR = registerBlock("jungle_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.JUNGLE_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> ACACIA_PET_DOOR = registerBlock("acacia_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.ACACIA_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> DARK_OAK_PET_DOOR = registerBlock("dark_oak_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.DARK_OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> MANGROVE_PET_DOOR = registerBlock("mangrove_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.MANGROVE_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> CHERRY_PET_DOOR = registerBlock("cherry_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.CHERRY_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> BAMBOO_PET_DOOR = registerBlock("bamboo_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.BAMBOO_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> CRIMSON_PET_DOOR = registerBlock("crimson_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.CRIMSON_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> WARPED_PET_DOOR = registerBlock("warped_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.WARPED_DOOR).noOcclusion()));

    public static final RegistryObject<PetDoorBlock> WHITE_PET_DOOR = registerBlock("white_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> ORANGE_PET_DOOR = registerBlock("orange_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> MAGENTA_PET_DOOR = registerBlock("magenta_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> LIGHT_BLUE_PET_DOOR = registerBlock("light_blue_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> YELLOW_PET_DOOR = registerBlock("yellow_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> LIME_PET_DOOR = registerBlock("lime_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> PINK_PET_DOOR = registerBlock("pink_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> GRAY_PET_DOOR = registerBlock("gray_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> LIGHT_GRAY_PET_DOOR = registerBlock("light_gray_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> CYAN_PET_DOOR = registerBlock("cyan_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> PURPLE_PET_DOOR = registerBlock("purple_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> BLUE_PET_DOOR = registerBlock("blue_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> BROWN_PET_DOOR = registerBlock("brown_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> GREEN_PET_DOOR = registerBlock("green_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> RED_PET_DOOR = registerBlock("red_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));
    public static final RegistryObject<PetDoorBlock> BLACK_PET_DOOR = registerBlock("black_pet_door", () -> new PetDoorBlock(Block.Properties.copy(Blocks.OAK_DOOR).noOcclusion()));

    public static <T extends Block> RegistryObject<T> registerPetBowl(String name, Supplier<T> block) {
        RegistryObject<T> reg = registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties(Rarity.COMMON)));
        pet_bowls.add(reg);
        return reg;
    }    public static final RegistryObject<BlockEntityType<BirdCageBlockEntity>> BIRD_CAGE_BE = BLOCK_ENTITIES.register("bird_cage", () -> BlockEntityType.Builder.of(BirdCageBlockEntity::new, BlockInit.BIRD_CAGE.get()).build(null));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties(Rarity.COMMON)));
    }

    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
        var reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }

    public static void loadClass() {
    }



}
