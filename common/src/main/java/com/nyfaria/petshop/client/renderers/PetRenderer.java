package com.nyfaria.petshop.client.renderers;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.*;
import com.nyfaria.petshop.client.renderers.layer.*;
import com.nyfaria.petshop.entity.BasePet;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.joml.*;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.*;

import java.lang.Math;

public class PetRenderer<T extends BasePet> extends GeoEntityRenderer<T> {
    public PetRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.addRenderLayer(new PetCarryItemLayer<T>(this));
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        boolean useColor = false;
        float r = 0;
        float g = 0;
        float b = 0;
        if (bone.getName().contains("hat")) {
            bone.setHidden(!animatable.hasHat());
            useColor = animatable.hasHat();
            r = animatable.getHatColor().x();
            g = animatable.getHatColor().y();
            b = animatable.getHatColor().z();
        } else if (bone.getName().contains("collar")) {
            bone.setHidden(!animatable.hasCollar());
            useColor = animatable.hasCollar();
            r = animatable.getCollarColor().x();
            g = animatable.getCollarColor().y();
            b = animatable.getCollarColor().z();
        } else if (bone.getName().contains("boot")) {
            bone.setHidden(!animatable.hasBoots());
            useColor = animatable.hasBoots();
            r = animatable.getBootsColor().x();
            g = animatable.getBootsColor().y();
            b = animatable.getBootsColor().z();
        }

        if (useColor) {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r, g, b, alpha);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }


    }

    @Override
    public void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);



        animatable.getCustomLeashHolder().ifPresent(
                leashHolder -> this.renderCustomLeash(animatable, partialTick, poseStack, bufferSource, Minecraft.getInstance().level.getPlayerByUUID(leashHolder), animatable.getLeashColor())
        );
    }

    public void renderOnShoulder(BasePet pet, PoseStack pMatrixStack, VertexConsumer vertexconsumer, int pPackedLight, int noOverlay, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, int tickCount) {

    }



    public <E extends Entity> void renderCustomLeash(T mob, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, E leashHolder, int color) {
        double lerpBodyAngle = (double)(Mth.lerp(partialTick, mob.yBodyRotO, mob.yBodyRot) * ((float)Math.PI / 180F) + ((float)Math.PI / 2F));
        Vec3 leashOffset = mob.getLeashOffset();
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = Mth.lerp((double)partialTick, mob.xo, mob.getX()) + xAngleOffset;
        double lerpOriginY = Mth.lerp((double)partialTick, mob.yo, mob.getY()) + leashOffset.y;
        double lerpOriginZ = Mth.lerp((double)partialTick, mob.zo, mob.getZ()) + zAngleOffset;
        Vec3 ropeGripPosition = leashHolder.getRopeHoldPosition(partialTick);
        float xDif = (float)(ropeGripPosition.x - lerpOriginX);
        float yDif = (float)(ropeGripPosition.y - lerpOriginY);
        float zDif = (float)(ropeGripPosition.z - lerpOriginZ);
        float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.025F / 2.0F;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        BlockPos entityEyePos = BlockPos.containing(mob.getEyePosition(partialTick));
        BlockPos holderEyePos = BlockPos.containing(leashHolder.getEyePosition(partialTick));
        int entityBlockLight = this.getBlockLightLevel(mob, entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.level().getBrightness(LightLayer.BLOCK, holderEyePos);
        int entitySkyLight = mob.level().getBrightness(LightLayer.SKY, entityEyePos);
        int holderSkyLight = mob.level().getBrightness(LightLayer.SKY, holderEyePos);
        poseStack.pushPose();
        poseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);
        Matrix4f posMatrix = new Matrix4f(poseStack.last().pose());

        for(int segment = 0; segment <= 24; ++segment) {
            renderCustomLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.025F, xOffset, zOffset, segment, false, color);
        }

        for(int segment = 24; segment >= 0; --segment) {
            renderCustomLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.0F, xOffset, zOffset, segment, true, color);
        }

        poseStack.popPose();
    }

    private static void renderCustomLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif, float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight, int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot, int color) {
        float piecePosPercent = (float)segment / 24.0F;
        int lerpBlockLight = (int)Mth.lerp(piecePosPercent, (float)entityBlockLight, (float)holderBlockLight);
        int lerpSkyLight = (int)Mth.lerp(piecePosPercent, (float)entitySkyLight, (float)holderSkyLight);
        int packedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7F : 1.0F;
        float red = ((color >> 16) & 0xFF) / 255.0F * knotColourMod;
        float green = ((color >> 8) & 0xFF) / 255.0F * knotColourMod;
        float blue = (color & 0xFF) / 255.0F * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0F ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0F - piecePosPercent) * (1.0F - piecePosPercent);
        float z = zDif * piecePosPercent;
        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1.0F).uv2(packedLight).endVertex();
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1.0F).uv2(packedLight).endVertex();
    }


}
