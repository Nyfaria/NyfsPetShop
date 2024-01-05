package com.nyfaria.nyfspetshop;

import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.init.BlockInit;
import com.nyfaria.nyfspetshop.init.BlockStateInit;
import com.nyfaria.nyfspetshop.init.EntityInit;
import com.nyfaria.nyfspetshop.init.ItemInit;
import com.nyfaria.nyfspetshop.init.MemoryModuleTypeInit;
import com.nyfaria.nyfspetshop.init.MenuTypeInit;
import com.nyfaria.nyfspetshop.init.POIInit;
import com.nyfaria.nyfspetshop.init.VillagerInit;
import com.nyfaria.nyfspetshop.network.PacketInit;
import com.nyfaria.nyfspetshop.registration.EntityDataSerializerHelper;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {
    public static EntityDataSerializer<MovementType> MOVEMENT_TYPE = EntityDataSerializer.simpleEnum(MovementType.class);


    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        ItemInit.loadClass();
        BlockInit.loadClass();
        EntityInit.loadClass();
        POIInit.loadClass();
        VillagerInit.loadClass();
        MemoryModuleTypeInit.loadClass();
        BlockStateInit.loadClass();
        MenuTypeInit.loadClass();
        PacketInit.loadClass();
        EntityDataSerializerHelper.INSTANCE.register(new ResourceLocation(Constants.MODID,"movement_type"),MOVEMENT_TYPE);
    }
}