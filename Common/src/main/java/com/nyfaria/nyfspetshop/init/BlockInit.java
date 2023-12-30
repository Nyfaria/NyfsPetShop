package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.block.BasicHorizontalBlock;
import com.nyfaria.nyfspetshop.block.GroomingStation;
import com.nyfaria.nyfspetshop.block.PetBowl;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
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

    public static List<RegistryObject<? extends Block>> pet_bowls = new ArrayList<>();
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);

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
    public static final RegistryObject<Block> GROOMING_STATION = registerBlock("grooming_station", () -> new GroomingStation(Block.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()));
    public static final RegistryObject<Block> CRATE = registerBlock("crate", () -> new BasicHorizontalBlock(Block.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()));

    public static <T extends Block> RegistryObject<T> registerPetBowl(String name, Supplier<T> block) {
        RegistryObject<T> reg = registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties(Rarity.COMMON)));
        pet_bowls.add(reg);
        return reg;
    }
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
