package com.nyfaria.petshop.datagen;

import com.google.common.collect.ImmutableMap;
import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.EntityInit;
import com.nyfaria.petshop.init.ItemInit;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {
    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", ""
    );

    public ModLangProvider(PackOutput gen) {
        super(gen, Constants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().forEach(this::itemLang);
        EntityInit.ENTITIES.getEntries().forEach(this::entityLang);
        BlockInit.BLOCKS.getEntries().forEach(this::blockLang);
        add("itemGroup." + Constants.MODID + ".tab", Constants.MOD_NAME);
        add("movementType." + Constants.MODID + ".stay", "Stay");
        add("movementType." + Constants.MODID + ".follow", "Follow");
        add("movementType." + Constants.MODID + ".wander", "Wander");
        add("player_message." + Constants.MODID + ".movementType", "%s is now commanded to %s");
        add("container." + Constants.MODID + ".grooming", "Grooming Station");
        add("entity.minecraft.villager.pets", "Pet Supplier");
        add("entity.minecraft.villager." + Constants.MODID + ".pets", "Pet Supplier");
        add("entity.minecraft.villager.pet_items", "Pet Groomer");
        add("entity.minecraft.villager." + Constants.MODID + ".pet_items", "Pet Groomer");
    }

    protected void itemLang(RegistryObject<Item> entry) {
        if (!(entry.get() instanceof BlockItem) || entry.get() instanceof ItemNameBlockItem) {
            addItem(entry, checkReplace(entry));
        }
    }

    protected void blockLang(RegistryObject<Block> entry) {
        addBlock(entry, checkReplace(entry));
    }

    protected void entityLang(RegistryObject<EntityType<?>> entry) {
        addEntityType(entry, checkReplace(entry));
    }

    protected String checkReplace(RegistryObject<?> registryObject) {
        return Arrays.stream(registryObject.getId().getPath().split("_"))
                .map(this::checkReplace)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String checkReplace(String string) {
        return REPLACE_LIST.containsKey(string) ? REPLACE_LIST.get(string) : StringUtils.capitalize(string);
    }
}
