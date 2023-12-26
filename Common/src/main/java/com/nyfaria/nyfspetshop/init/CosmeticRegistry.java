package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.entity.BasePet;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;

public class CosmeticRegistry {
    public static Map<EntityType<? extends BasePet>, List<Type>> COSMETIC_MAP = Map.of(
            EntityInit.SHELTIE.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.SUPER_MUTT.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS)
    );


    public enum Type {
        HAT("hat"),
        COLLAR("collar"),
        BOOTS("boots");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
