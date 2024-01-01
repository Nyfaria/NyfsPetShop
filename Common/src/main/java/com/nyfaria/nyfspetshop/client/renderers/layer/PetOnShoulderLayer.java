package com.nyfaria.nyfspetshop.client.renderers.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.nyfspetshop.entity.BasePet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class PetOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {

    public PetOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> $$0) {
        super($$0);
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, true);
        this.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, false);
    }

    private void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, boolean pLeftShoulder) {
        CompoundTag compoundtag = pLeftShoulder ? pLivingEntity.getShoulderEntityLeft() : pLivingEntity.getShoulderEntityRight();
        EntityType.byString(compoundtag.getString("id")).ifPresent((type) -> {
            if(type.create(Minecraft.getInstance().level) instanceof BasePet pet) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(pLeftShoulder ? 0.4F : -0.4F, pLivingEntity.isCrouching() ? -1.3F : -1.5F, 0.0F);
                pet.load(compoundtag);
                pMatrixStack.scale(1, -1, 1);
                pMatrixStack.translate(0, -1.5, 0);
                Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(pet).render(pet, pet.getYRot(), pet.getXRot(), pMatrixStack, pBuffer, pPackedLight);
                pMatrixStack.popPose();
            }
        });
    }
}