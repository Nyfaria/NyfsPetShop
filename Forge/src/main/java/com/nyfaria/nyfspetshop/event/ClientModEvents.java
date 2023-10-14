package com.nyfaria.nyfspetshop.event;

import com.nyfaria.nyfspetshop.Constants;
import com.nyfaria.nyfspetshop.init.EntityInit;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityInit.BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.SHELTIE.get(), context -> new GeoEntityRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID,"sheltie"),true)));
        event.registerEntityRenderer(EntityInit.SUPER_MUTT.get(), context -> new GeoEntityRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID,"super_mutt"),true)));
    }
}
