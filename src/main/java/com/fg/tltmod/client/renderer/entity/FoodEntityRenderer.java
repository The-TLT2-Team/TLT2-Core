package com.fg.tltmod.client.renderer.entity;

import com.fg.tltmod.content.entity.FoodEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class FoodEntityRenderer extends EntityRenderer<FoodEntity> {
    public ItemRenderer itemRenderer;
    public FoodEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.itemRenderer = p_174008_.getItemRenderer();
    }

    @Override
    public void render(FoodEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.tickCount >= 0 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0, entity.getBbHeight() * 0.5F, 0);
            float rotationSpeed = 24f;
            float rotationAngle = (entity.tickCount + partialTicks) * rotationSpeed;
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotationAngle));
            matrixStackIn.scale(1F,1F,1F);
            this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.level(),1);
            matrixStackIn.popPose();
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }
    protected int getSkyLightLevel(FoodEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(FoodEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(FoodEntity meteorEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
