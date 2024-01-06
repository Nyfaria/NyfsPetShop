package com.nyfaria.petshop.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.petshop.block.entity.BirdCageBlockEntity;
import com.nyfaria.petshop.entity.BasePet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

public class BirdCageRenderer implements BlockEntityRenderer<BirdCageBlockEntity> {
    @Override
    public void render(BirdCageBlockEntity birdCageBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        CompoundTag compoundtag = birdCageBlockEntity.getPetTag();
        EntityType.byString(compoundtag.getString("id")).ifPresent((type) -> {
            if (type.create(Minecraft.getInstance().level) instanceof BasePet pet) {
                poseStack.pushPose();
                poseStack.translate(0.5f, 3f / 16f, 0.5F);
                pet.load(compoundtag);
                Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(pet).render(pet, pet.getYRot(), pet.getXRot(), poseStack, multiBufferSource, i);
                poseStack.popPose();
            }
        });
    }

}
