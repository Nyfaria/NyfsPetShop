package com.nyfaria.nyfspetshop;

import com.nyfaria.nyfspetshop.datagen.ModBlockStateProvider;
import com.nyfaria.nyfspetshop.datagen.ModItemModelProvider;
import com.nyfaria.nyfspetshop.datagen.ModLangProvider;
import com.nyfaria.nyfspetshop.datagen.ModLootTableProvider;
import com.nyfaria.nyfspetshop.datagen.ModRecipeProvider;
import com.nyfaria.nyfspetshop.datagen.ModSoundProvider;
import com.nyfaria.nyfspetshop.datagen.ModTagProvider;
import com.nyfaria.nyfspetshop.init.DataSerializerInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NyfsPetShop {
    
    public NyfsPetShop() {
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
        DataSerializerInit.ENTITY_DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();
        generator.addProvider(includeServer, new ModRecipeProvider(packOutput));
        generator.addProvider(includeServer, new ModLootTableProvider(packOutput));
        generator.addProvider(includeServer, new ModSoundProvider(packOutput, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Blocks(packOutput,event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Items(packOutput,event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.POITypes(packOutput,event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(packOutput));
    }
}