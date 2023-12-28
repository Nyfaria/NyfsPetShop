package com.nyfaria.nyfspetshop.event;

import com.nyfaria.nyfspetshop.server.ServerClass;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ServerClass.addStructures(event.getServer());
    }
}
