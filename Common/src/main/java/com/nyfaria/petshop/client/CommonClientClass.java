package com.nyfaria.petshop.client;

import com.nyfaria.petshop.Constants;
import com.nyfaria.petshop.block.PetBowl;
import com.nyfaria.petshop.client.renderers.PetRenderer;
import com.nyfaria.petshop.entity.BaseBird;
import com.nyfaria.petshop.entity.BaseCat;
import com.nyfaria.petshop.entity.BaseDog;
import com.nyfaria.petshop.init.BlockInit;
import com.nyfaria.petshop.init.EntityInit;
import com.nyfaria.petshop.init.ItemInit;
import com.nyfaria.petshop.registration.RegistryObject;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CommonClientClass {
    public static Map<BlockColor, List<RegistryObject<? extends Block>>> blockColors = Map.of(
            (state, world, pos, tintIndex) -> {
                if (((PetBowl) state.getBlock()).getColor() == null) {
                    return -1;
                }
                return ((PetBowl) state.getBlock()).getColor().getTextColor();
            }, BlockInit.pet_bowls
    );
    public static Map<ItemColor, List<RegistryObject<? extends Block>>> itemColors = Map.of(
            (state, tintIndex) -> {
                if (((PetBowl) ((BlockItem) state.getItem()).getBlock()).getColor() == null) {
                    return -1;
                }
                return ((PetBowl) ((BlockItem) state.getItem()).getBlock()).getColor().getTextColor();
            }, BlockInit.pet_bowls
    );

    public static <T extends Entity> List<Renderers<?>> getRenderers() {
        return List.of(
                new Renderers(EntityInit.BALL, ThrownItemRenderer::new),
                new Renderers(EntityInit.SHELTIE, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "sheltie"), true))),
                new Renderers(EntityInit.ENGLISH_COCKER_SPANIEL, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "english_cocker_spaniel"), true))),
                new Renderers(EntityInit.SUPER_MUTT, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<>(new ResourceLocation(Constants.MODID, "super_mutt"), true))),
                new Renderers(EntityInit.SABLE_HUSKY, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseDog>(new ResourceLocation(Constants.MODID, "husky"), true).withAltTexture(new ResourceLocation(Constants.MODID, "sable_husky")))),
                new Renderers(EntityInit.BLACK_AND_WHITE_HUSKY, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseDog>(new ResourceLocation(Constants.MODID, "husky"), true).withAltTexture(new ResourceLocation(Constants.MODID, "black_and_white_husky")))),

                new Renderers(EntityInit.CALICO, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "calico")))),
                new Renderers(EntityInit.AMERICAN_SHORTHAIR, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "american_shorthair")))),
                new Renderers(EntityInit.BLACK_TUXEDO, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "base_cat"), true).withAltTexture(new ResourceLocation(Constants.MODID, "black_tuxedo")))),
                new Renderers(EntityInit.BROWN_TUXEDO_MUNCHKIN, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseCat>(new ResourceLocation(Constants.MODID, "munchkin"), true).withAltTexture(new ResourceLocation(Constants.MODID, "brown_tuxedo_munchkin")))),

                new Renderers(EntityInit.GOLD_DASHED_PARROT, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "gold_dashed_parrot")))),
                new Renderers(EntityInit.WHITE_STRIPED_PARROT, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "white_striped_parrot")))),
                new Renderers(EntityInit.RED_ACCENT_ALBINO_PARROT, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "red_accent_albino_parrot")))),
                new Renderers(EntityInit.TROPICAL_PARROT, context -> new PetRenderer<>(context, new DefaultedEntityGeoModel<BaseBird>(new ResourceLocation(Constants.MODID, "base_bird"), true).withAltTexture(new ResourceLocation(Constants.MODID, "tropical_parrot"))))
        );
    }

    public static void itemModelProperties() {
        ClampedItemPropertyFunction clampeditempropertyfunction = (itemStack, level, livingEntity, i) -> {
            if (itemStack.hasTag() && itemStack.getTag().contains("pet_type")) {
                if (itemStack.getTag().getString("pet_type").equals("dog")) {
                    return 0.1F;
                } else if (itemStack.getTag().getString("pet_type").equals("cat")) {
                    return 0.2F;
                } else if (itemStack.getTag().getString("pet_type").equals("bird")) {
                    return 0.3F;
                }
            }
            return 0.1f;
        };
        ItemProperties.register(ItemInit.PET_ITEM.get(), new ResourceLocation(Constants.MODID, "type"), clampeditempropertyfunction);
    }

    public record Renderers<T extends Entity>(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderer) {
    }
}
