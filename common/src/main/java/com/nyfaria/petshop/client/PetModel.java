package com.nyfaria.petshop.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nyfaria.petshop.entity.BasePet;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PetModel<T extends BasePet> extends DefaultedEntityGeoModel<T> {
    public PetModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    public PetModel(ResourceLocation assetSubpath, boolean turnsHead) {
        super(assetSubpath, turnsHead);
    }

    public void renderOnShoulder(PoseStack pMatrixStack, VertexConsumer vertexconsumer, int pPackedLight, int noOverlay, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, int tickCount) {
    }
}
