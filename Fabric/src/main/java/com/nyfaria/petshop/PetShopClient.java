package com.nyfaria.petshop;

import com.nyfaria.petshop.client.CommonClientClass;
import com.nyfaria.petshop.client.GroomingScreen;
import com.nyfaria.petshop.client.renderers.BirdCageRenderer;
import com.nyfaria.petshop.client.renderers.layer.PetOnShoulderLayer;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.MenuTypeInit;
import com.nyfaria.petshop.registration.RegistryObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PetShopClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommonClientClass.getRenderers().forEach(
                record -> EntityRendererRegistry.register((EntityType) record.type().get(), record.renderer())
        );
        BlockEntityRenderers.register(BlockInit.BIRD_CAGE_BE.get(), (context) -> new BirdCageRenderer());
        CommonClientClass.itemColors.forEach(
                (itemColor, registryObjects) -> ColorProviderRegistry.ITEM.register(itemColor, registryObjects.stream().map(RegistryObject::get).map(block -> block.asItem()).toArray(Item[]::new)));
        CommonClientClass.blockColors.forEach(
                (itemColor, registryObjects) -> ColorProviderRegistry.BLOCK.register(itemColor, registryObjects.stream().map(RegistryObject::get).toArray(Block[]::new)));
        MenuScreens.register(MenuTypeInit.GROOMING_STATION.get(), GroomingScreen::new);
        CommonClientClass.itemModelProperties();
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType == EntityType.PLAYER) {
                registrationHelper.register(new PetOnShoulderLayer<>((PlayerRenderer) entityRenderer));
            }
        });
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.BIRD_CAGE.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CRATE.get(), RenderType.cutout());

    }
}
