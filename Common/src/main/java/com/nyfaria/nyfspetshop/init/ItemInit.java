package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.entity.data.Species;
import com.nyfaria.nyfspetshop.item.BallItem;
import com.nyfaria.nyfspetshop.item.PetItem;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<RegistryObject<Item>> DOG_COLLARS = new ArrayList<>();
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(BlockInit.PET_BOWL.get()))
            .displayItems(
                    (itemDisplayParameters, output) -> {
//                        ITEMS.getEntries().forEach((registryObject) -> output.accept(new ItemStack(registryObject.get())));
                        output.accept(new ItemStack(ItemInit.TENNIS_BALL.get()));
                        DOG_COLLARS.forEach((item) -> {
                            EntityInit.SPECIES_MAP.get(Species.DOG).forEach( (entityType) -> {
                                        ItemStack stack = new ItemStack(item.get());
                                        stack.getOrCreateTag().putString("entityType", BuiltInRegistries.ENTITY_TYPE.getKey(entityType.get()).toString());
                                        output.accept(stack);
                                    }
                            );
                        });
                        BlockInit.pet_bowls.forEach(block->output.accept(block.get()));
                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());

    public static final RegistryObject<Item> DOG_COLLAR = registerDogCollar("dog_collar", getItemProperties(Rarity.COMMON));
    public static final RegistryObject<Item> TENNIS_BALL = ITEMS.register("tennis_ball", () -> new BallItem(getItemProperties(Rarity.COMMON).stacksTo(1)));
    public static final RegistryObject<Item> KIBBLE = ITEMS.register("kibble", () -> new Item(getItemProperties(Rarity.COMMON).stacksTo(16)));

    public static RegistryObject<Item> registerDogCollar(String name, Item.Properties properties) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new PetItem(properties));
        DOG_COLLARS.add(item);
        return item;
    }
    public static Item.Properties getItemProperties(Rarity rarity) {
        return new Item.Properties().fireResistant().rarity(rarity);
    }

    public static void loadClass() {
    }
}
