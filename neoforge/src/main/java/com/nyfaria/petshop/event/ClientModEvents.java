package com.nyfaria.petshop.event;

import com.nyfaria.petshop.client.CommonClientClass;
import com.nyfaria.petshop.client.GroomingScreen;
import com.nyfaria.petshop.client.renderers.BirdCageRenderer;
import com.nyfaria.petshop.client.renderers.layer.PetOnShoulderLayer;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.MenuTypeInit;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        CommonClientClass.blockColors.forEach(
                (blockColor, registryObjects) -> {
                    event.getBlockColors().register(blockColor, registryObjects.stream().map(RegistryObject::get).toArray(Block[]::new));
                }

        );
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        CommonClientClass.itemColors.forEach(
                (blockColor, registryObjects) -> {
                    event.getItemColors().register(blockColor, registryObjects.stream().map(RegistryObject::get).map(block -> block.asItem()).toArray(Item[]::new));
                }

        );
    }

    @SubscribeEvent
    public static void onFMLClient(FMLClientSetupEvent event) {
        MenuScreens.register(MenuTypeInit.GROOMING_STATION.get(), GroomingScreen::new);
        CommonClientClass.itemModelProperties();
    }

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CommonClientClass.getRenderers().forEach(
                record -> event.registerEntityRenderer((EntityType) record.type().get(), record.renderer())
        );
        event.registerBlockEntityRenderer(BlockInit.BIRD_CAGE_BE.get(), (context) -> new BirdCageRenderer());
    }

    @SubscribeEvent
    public static void onEntityAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String name : event.getSkins()) {
            PlayerRenderer parent = event.getSkin(name);
            parent.addLayer(new PetOnShoulderLayer<>(parent));
        }
    }
}
