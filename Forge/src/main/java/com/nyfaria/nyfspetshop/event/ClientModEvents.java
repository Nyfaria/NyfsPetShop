package com.nyfaria.nyfspetshop.event;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.client.CommonClientClass;
import com.nyfaria.nyfspetshop.client.GroomingScreen;
import com.nyfaria.nyfspetshop.client.renderers.BirdCageRenderer;
import com.nyfaria.nyfspetshop.client.renderers.PetRenderer;
import com.nyfaria.nyfspetshop.client.renderers.layer.PetOnShoulderLayer;
import com.nyfaria.nyfspetshop.entity.BaseBird;
import com.nyfaria.nyfspetshop.entity.BaseCat;
import com.nyfaria.nyfspetshop.entity.BaseDog;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.EntityInit;
import com.nyfaria.nyfspetshop.init.MenuTypeInit;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

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
                    event.getItemColors().register(blockColor, registryObjects.stream().map(RegistryObject::get).map(block->block.asItem()).toArray(Item[]::new));
                }

        );
    }
    @SubscribeEvent
    public static void onFMLClient(FMLClientSetupEvent event){
        MenuScreens.register(MenuTypeInit.GROOMING_STATION.get(), GroomingScreen::new);
        CommonClientClass.itemModelProperties();
    }

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.SHELTIE.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "sheltie"), true)));
        event.registerEntityRenderer(EntityInit.ENGLISH_COCKER_SPANIEL.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "english_cocker_spaniel"), true)));
        event.registerEntityRenderer(EntityInit.SUPER_MUTT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "super_mutt"), true)));
        event.registerEntityRenderer(EntityInit.SABLE_HUSKY.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseDog>(new ResourceLocation(Constants.MODID, "husky"), true).withAltTexture(new ResourceLocation(Constants.MODID, "sable_husky"))));
        event.registerEntityRenderer(EntityInit.BLACK_AND_WHITE_HUSKY.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseDog>(new ResourceLocation(Constants.MODID, "husky"), true).withAltTexture(new ResourceLocation(Constants.MODID, "black_and_white_husky"))));

        event.registerEntityRenderer(EntityInit.CALICO.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "calico"))));
        event.registerEntityRenderer(EntityInit.AMERICAN_SHORTHAIR.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "american_shorthair"))));
        event.registerEntityRenderer(EntityInit.BLACK_TUXEDO.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "black_tuxedo"))));

        event.registerEntityRenderer(EntityInit.GOLD_DASHED_PARROT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "gold_dashed_parrot"))));
        event.registerEntityRenderer(EntityInit.WHITE_STRIPED_PARROT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "white_striped_parrot"))));
        event.registerEntityRenderer(EntityInit.RED_ACCENT_ALBINO_PARROT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "red_accent_albino_parrot"))));
        event.registerEntityRenderer(EntityInit.TROPICAL_PARROT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "tropical_parrot"))));

        event.registerBlockEntityRenderer(BlockInit.BIRD_CAGE_BE.get(), (context)->new BirdCageRenderer());
    }

    @SubscribeEvent
    public static void onEntityAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String name : event.getSkins()) {
            PlayerRenderer parent = event.getSkin(name);
            parent.addLayer(new PetOnShoulderLayer<>(parent));
        }
    }
}
