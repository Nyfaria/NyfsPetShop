package com.nyfaria.petshop;

import com.nyfaria.petshop.entity.enums.MovementType;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.BlockStateInit;
import com.nyfaria.petshop.init.EntityInit;
import com.nyfaria.petshop.init.ItemInit;
import com.nyfaria.petshop.init.MemoryModuleTypeInit;
import com.nyfaria.petshop.init.MenuTypeInit;
import com.nyfaria.petshop.init.POIInit;
import com.nyfaria.petshop.init.VillagerInit;
import com.nyfaria.petshop.network.PacketInit;
import com.nyfaria.petshop.registration.EntityDataSerializerHelper;
import net.minecraft.network.syncher.EntityDataSerializer;
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
        EntityDataSerializerHelper.INSTANCE.register(new ResourceLocation(Constants.MODID, "movement_type"), MOVEMENT_TYPE);
    }
}