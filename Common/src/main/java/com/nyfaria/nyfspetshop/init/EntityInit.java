package com.nyfaria.nyfspetshop.init;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nyfaria.nyfspetshop.entity.BaseDog;
import com.nyfaria.nyfspetshop.entity.ThrownBall;
import com.nyfaria.nyfspetshop.entity.data.Species;
import com.nyfaria.nyfspetshop.registration.RegistrationProvider;
import com.nyfaria.nyfspetshop.registration.RegistryObject;
import com.nyfaria.nyfspetshop.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final Multimap<Species,Supplier<? extends EntityType<? extends LivingEntity>>> SPECIES_MAP = HashMultimap.create();
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<BaseDog>> SHELTIE = registerEntity(Species.DOG,"sheltie", ()->EntityType.Builder.of(BaseDog::new, MobCategory.CREATURE).sized(0.4f,0.6f), BaseDog::createAttributes);
    public static final RegistryObject<EntityType<BaseDog>> SUPER_MUTT = registerEntity(Species.DOG,"super_mutt", ()->EntityType.Builder.of(BaseDog::new, MobCategory.CREATURE).sized(0.6f,0.8f), BaseDog::createAttributes);
    public static final RegistryObject<EntityType<ThrownBall>> BALL = registerEntity("ball", ()->EntityType.Builder.<ThrownBall>of(ThrownBall::new, MobCategory.MISC).sized(0.25F, 0.25F));
    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(Constants.MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(@Nullable Species species, String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                         Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        if(species != null){
            SPECIES_MAP.put(species, entityTypeSupplier);
        }
        return entityTypeSupplier;
    }

    public static void loadClass() {
    }


    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
