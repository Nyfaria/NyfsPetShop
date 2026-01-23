package com.nyfaria.petshop.client.renderers.layer;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import com.nyfaria.petshop.client.renderers.*;
import com.nyfaria.petshop.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.renderer.*;
import software.bernie.geckolib.renderer.layer.*;

public class PetCarryItemLayer<T extends BasePet> extends BlockAndItemGeoLayer<T> {
    public PetCarryItemLayer(PetRenderer<T> renderer) {
        super(renderer, (bone, pet)-> bone.getName().contains("carried_item") ? pet.getCarriedItemStack() : null, (bone, pet)-> null);
    }

    @Override
    protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
