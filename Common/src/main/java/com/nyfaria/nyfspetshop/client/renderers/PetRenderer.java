package com.nyfaria.nyfspetshop.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nyfaria.nyfspetshop.entity.BasePet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PetRenderer<T extends BasePet> extends GeoEntityRenderer<T> {
    public PetRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        boolean useColor = false;
        float r = 0;
        float g = 0;
        float b = 0;
        if(bone.getName().contains("hat")){
            bone.setHidden(!animatable.hasHat());
            useColor = animatable.hasHat();
            r = animatable.getHatColor().x();
            g = animatable.getHatColor().y();
            b = animatable.getHatColor().z();
        } else if(bone.getName().contains("collar")){
            bone.setHidden(!animatable.hasCollar());
            useColor = animatable.hasCollar();
            r = animatable.getCollarColor().x();
            g = animatable.getCollarColor().y();
            b = animatable.getCollarColor().z();
        } else if(bone.getName().contains("boot")){
            bone.setHidden(!animatable.hasBoots());
            useColor = animatable.hasBoots();
            r = animatable.getBootsColor().x();
            g = animatable.getBootsColor().y();
            b = animatable.getBootsColor().z();
        }

        if(useColor){
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r,g,b, alpha);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }

    }

    public void renderOnShoulder(BasePet pet, PoseStack pMatrixStack, VertexConsumer vertexconsumer, int pPackedLight, int noOverlay, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, int tickCount) {

    }
}
