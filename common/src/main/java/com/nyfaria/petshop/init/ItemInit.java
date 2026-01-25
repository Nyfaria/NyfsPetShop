package com.nyfaria.petshop.init;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.item.BallItem;
import com.nyfaria.petshop.item.KibbleItem;
import com.nyfaria.petshop.item.LeashItem;
import com.nyfaria.petshop.item.PetItem;
import com.nyfaria.petshop.registration.RegistrationProvider;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<RegistryObject<Item>> PET_ITEMS = new ArrayList<>();
    public static final List<RegistryObject<Item>> LEASH_ITEMS = new ArrayList<>();
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(BlockInit.PET_BOWL.get()))
            .displayItems(
                    (itemDisplayParameters, output) -> {
//                        ITEMS.getEntries().forEach((registryObject) -> output.accept(new ItemStack(registryObject.get())));
                        PET_ITEMS.forEach((item) -> {
                            EntityInit.SPECIES_MAP.forEach((type, entityType) -> {
                                        ItemStack stack = new ItemStack(item.get());
                                        stack.getOrCreateTag().putString("entityType", BuiltInRegistries.ENTITY_TYPE.getKey(entityType.get()).toString());
                                        stack.getTag().putString("pet_type", type.getName());
                                        output.accept(stack);
                                    }
                            );
                        });
                        BlockInit.pet_bowls.forEach(block -> output.accept(block.get()));
                        output.accept(BlockInit.GROOMING_STATION.get());
                        output.accept(BlockInit.CRATE.get());
                        output.accept(BlockInit.BIRD_CAGE.get());
                        output.accept(ItemInit.TENNIS_BALL.get());
                        output.accept(ItemInit.YARN_BALL.get());
                        output.accept(ItemInit.BAG_OF_KIBBLE.get());
                        output.accept(ItemInit.DOG_TREAT.get());
                        output.accept(ItemInit.TUNA_TREAT.get());
                        output.accept(ItemInit.PEANUT.get());
                        output.accept(BlockInit.PET_BED.get());
                        output.accept(BlockInit.BIG_PET_BED.get());
                        output.accept(BlockInit.PET_DOOR.get());
                        LEASH_ITEMS.forEach(item -> output.accept(item.get()));


                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());
    public static final RegistryObject<Item> PET_ITEM = registerDogCollar("pet_item", getItemProperties(Rarity.COMMON).stacksTo(1));
    public static final RegistryObject<Item> TENNIS_BALL = ITEMS.register("tennis_ball", () -> new BallItem(getItemProperties(Rarity.COMMON).stacksTo(1)));
    public static final RegistryObject<Item> YARN_BALL = ITEMS.register("yarn_ball", () -> new BallItem(getItemProperties(Rarity.COMMON).stacksTo(1)));
    public static final RegistryObject<Item> BAG_OF_KIBBLE = ITEMS.register("bag_of_kibble", () -> new KibbleItem(KibbleItem.Type.BAG, getItemProperties(Rarity.COMMON).durability(3)));
    public static final RegistryObject<Item> DOG_TREAT = ITEMS.register("dog_treat", () -> new Item(getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Item> TUNA_TREAT = ITEMS.register("tuna_treat", () -> new Item(getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Item> PEANUT = ITEMS.register("peanut", () -> new Item(getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Item> PANCAKES = ITEMS.register("pancakes", () -> new Item(getItemProperties(Rarity.COMMON)));

    public static final RegistryObject<Item> WHITE_LEASH = registerLeash("white_leash", DyeColor.WHITE);
    public static final RegistryObject<Item> ORANGE_LEASH = registerLeash("orange_leash", DyeColor.ORANGE);
    public static final RegistryObject<Item> MAGENTA_LEASH = registerLeash("magenta_leash", DyeColor.MAGENTA);
    public static final RegistryObject<Item> LIGHT_BLUE_LEASH = registerLeash("light_blue_leash", DyeColor.LIGHT_BLUE);
    public static final RegistryObject<Item> YELLOW_LEASH = registerLeash("yellow_leash", DyeColor.YELLOW);
    public static final RegistryObject<Item> LIME_LEASH = registerLeash("lime_leash", DyeColor.LIME);
    public static final RegistryObject<Item> PINK_LEASH = registerLeash("pink_leash", DyeColor.PINK);
    public static final RegistryObject<Item> GRAY_LEASH = registerLeash("gray_leash", DyeColor.GRAY);
    public static final RegistryObject<Item> LIGHT_GRAY_LEASH = registerLeash("light_gray_leash", DyeColor.LIGHT_GRAY);
    public static final RegistryObject<Item> CYAN_LEASH = registerLeash("cyan_leash", DyeColor.CYAN);
    public static final RegistryObject<Item> PURPLE_LEASH = registerLeash("purple_leash", DyeColor.PURPLE);
    public static final RegistryObject<Item> BLUE_LEASH = registerLeash("blue_leash", DyeColor.BLUE);
    public static final RegistryObject<Item> BROWN_LEASH = registerLeash("brown_leash", DyeColor.BROWN);
    public static final RegistryObject<Item> GREEN_LEASH = registerLeash("green_leash", DyeColor.GREEN);
    public static final RegistryObject<Item> RED_LEASH = registerLeash("red_leash", DyeColor.RED);
    public static final RegistryObject<Item> BLACK_LEASH = registerLeash("black_leash", DyeColor.BLACK);

    public static RegistryObject<Item> registerLeash(String name, DyeColor color) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new LeashItem(getItemProperties(Rarity.COMMON).stacksTo(1), color));
        LEASH_ITEMS.add(item);
        return item;
    }

    public static RegistryObject<Item> registerDogCollar(String name, Item.Properties properties) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new PetItem(properties));
        PET_ITEMS.add(item);
        return item;
    }

    public static Item.Properties getItemProperties(Rarity rarity) {
        return new Item.Properties().fireResistant().rarity(rarity);
    }

    public static void loadClass() {
    }
}
