package com.nyfaria.nyfspetshop.init;

import com.nyfaria.nyfspetshop.entity.BasePet;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;

public class CosmeticRegistry {
    public static Map<EntityType<? extends BasePet>, List<Type>> COSMETIC_MAP = Map.of(
            EntityInit.SHELTIE.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.SUPER_MUTT.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.BLACK_AND_WHITE_HUSKY.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.ENGLISH_COCKER_SPANIEL.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.SABLE_HUSKY.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.CALICO.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.AMERICAN_SHORTHAIR.get(), List.of(Type.HAT, Type.COLLAR, Type.BOOTS),
            EntityInit.GOLD_DASHED_PARROT.get(), List.of(Type.HAT, Type.BOOTS),
            EntityInit.WHITE_STRIPED_PARROT.get(), List.of(Type.HAT, Type.BOOTS)
    );


    public enum Type {
        HAT("hat",3),
        COLLAR("collar",1),
        BOOTS("boots",4);
        private final String name;
        private final int woolCost;

        Type(String name, int woolCost) {
            this.name = name;
            this.woolCost = woolCost;
        }

        public int getWoolCost() {
            return woolCost;
        }

        public String getName() {
            return name;
        }
    }
}
