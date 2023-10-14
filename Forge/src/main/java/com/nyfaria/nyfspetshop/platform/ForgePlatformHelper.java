package com.nyfaria.nyfspetshop.platform;

import com.nyfaria.nyfspetshop.entity.enums.MovementType;
import com.nyfaria.nyfspetshop.init.DataSerializerInit;
import com.nyfaria.nyfspetshop.init.EntityInit;
import com.nyfaria.nyfspetshop.platform.services.IPlatformHelper;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public EntityDataSerializer<MovementType> getMovementTypeSerializer() {
        return DataSerializerInit.MOVEMENT_TYPE.get();
    }
}