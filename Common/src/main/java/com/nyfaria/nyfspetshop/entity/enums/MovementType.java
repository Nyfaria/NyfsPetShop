package com.nyfaria.nyfspetshop.entity.enums;

import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.network.chat.Component;

public enum MovementType {
    STAY("stay"),
    WANDER("wander"),
    FOLLOW("follow");

    public final String name;

    MovementType(String name) {
        this.name = name;
    }

    public static MovementType fromName(String name) {
        for (MovementType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return STAY;
    }

    public Component getDisplayName() {
        return Component.translatable("movementType." + Constants.MODID + "." + name);
    }

    public MovementType cycle() {
        return values()[(ordinal() + 1) % values().length];
    }
}
