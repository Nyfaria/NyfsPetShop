package com.nyfaria.petshop.datagen;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.POIInit;
import com.nyfaria.petshop.init.TagInit;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModTagProvider {

    public static class Items extends TagsProvider<Item> {

        public Items(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_256596_, Registries.ITEM, p_256513_, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }

        public void populateTag(TagKey<Item> tag, Supplier<Item>... items) {
            for (Supplier<Item> item : items) {
                tag(tag).add(ForgeRegistries.ITEMS.getResourceKey(item.get()).get());
            }
        }
    }

    public static class Blocks extends TagsProvider<Block> {

        public Blocks(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Registries.BLOCK, p_256513_, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            populateTag(TagInit.PET_BOWLS, BlockInit.pet_bowls);

        }

        public <T extends Block> void populateTag(TagKey<Block> tag, Supplier<?>... items) {
            for (Supplier<?> item : items) {
                tag(tag).add(ForgeRegistries.BLOCKS.getResourceKey((Block) item.get()).get());
            }
        }

        public <T extends Block> void populateTag(TagKey<Block> tag, List<RegistryObject<? extends Block>> items) {
            for (Supplier<?> item : items) {
                tag(tag).add(ForgeRegistries.BLOCKS.getResourceKey((Block) item.get()).get());
            }
        }
    }

    public static class POITypes extends TagsProvider<PoiType> {

        public POITypes(PackOutput pGenerator, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(pGenerator, Registries.POINT_OF_INTEREST_TYPE, p_256513_, Constants.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            populateTag(TagInit.PET_BOWLS_POI, POIInit.PET_BOWL);
            populateTag(TagInit.PET_BEDS_POI, POIInit.PET_BEDS);
            populateTag(PoiTypeTags.ACQUIRABLE_JOB_SITE, POIInit.CRATES);
            populateTag(PoiTypeTags.ACQUIRABLE_JOB_SITE, POIInit.GROOMING_STATION);
        }

        public void populateTag(TagKey<PoiType> tag, RegistryObject<PoiType>... items) {
            for (RegistryObject<PoiType> item : items) {
                tag(tag).add(item.getResourceKey());
            }
        }
    }
}
