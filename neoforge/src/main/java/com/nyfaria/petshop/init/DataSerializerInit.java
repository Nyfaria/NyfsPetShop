package com.nyfaria.petshop.init;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.entity.enums.MovementType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializerInit {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Constants.MODID);

    public static final RegistryObject<EntityDataSerializer<MovementType>> MOVEMENT_TYPE = ENTITY_DATA_SERIALIZERS.register("movement_type", () -> createEnumDataSerializer(MovementType.class));


    public static <E extends Enum<E>> EntityDataSerializer<E> createEnumDataSerializer(Class<E> enumClass) {
        return new EntityDataSerializer<>() {
            @Override
            public void write(FriendlyByteBuf buffer, E value) {
                buffer.writeEnum(value);
            }

            @Override
            public E read(FriendlyByteBuf buffer) {
                return buffer.readEnum(enumClass);
            }

            @Override
            public E copy(E value) {
                return value;
            }
        };
    }
}
