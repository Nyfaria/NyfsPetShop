package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.item.BallItem;
import com.nyfaria.nyfspetshop.item.KibbleItem;
import com.nyfaria.nyfspetshop.item.PetItem;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<RegistryObject<Item>> PET_ITEMS = new ArrayList<>();
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
                        output.accept(new ItemStack(ItemInit.TENNIS_BALL.get()));
                        output.accept(ItemInit.BAG_OF_KIBBLE.get());
                        output.accept(ItemInit.DOG_TREAT.get());
                        output.accept(ItemInit.TUNA_TREAT.get());
                        output.accept(ItemInit.PEANUT.get());


                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());

    public static final RegistryObject<Item> PET_ITEM = registerDogCollar("pet_item", getItemProperties(Rarity.COMMON).stacksTo(1));
    public static final RegistryObject<Item> TENNIS_BALL = ITEMS.register("tennis_ball", () -> new BallItem(getItemProperties(Rarity.COMMON).stacksTo(1)));
    public static final RegistryObject<Item> BAG_OF_KIBBLE = ITEMS.register("bag_of_kibble", () -> new KibbleItem(KibbleItem.Type.BAG, getItemProperties(Rarity.COMMON).durability(3)));
    public static final RegistryObject<Item> DOG_TREAT = ITEMS.register("dog_treat", () -> new Item(getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Item> TUNA_TREAT = ITEMS.register("tuna_treat", () -> new Item(getItemProperties(Rarity.COMMON)));
    public static final RegistryObject<Item> PEANUT = ITEMS.register("peanut", () -> new Item(getItemProperties(Rarity.COMMON)));

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
