package com.nyfaria.nyfspetshop.event;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.client.CommonClientClass;
import com.nyfaria.nyfspetshop.client.GroomingScreen;
import com.nyfaria.nyfspetshop.client.renderers.PetRenderer;
import com.nyfaria.nyfspetshop.init.EntityInit;
import com.nyfaria.nyfspetshop.init.MenuTypeInit;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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
import software.bernie.geckolib.renderer.GeoEntityRenderer;

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
    }

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.SHELTIE.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "sheltie"), true)));
        event.registerEntityRenderer(EntityInit.SUPER_MUTT.get(), context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "super_mutt"), true)));
    }
}
