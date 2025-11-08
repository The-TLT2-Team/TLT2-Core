package com.fg.tltmod.client.renderer.entity;

import com.github.L_Ender.cataclysm.client.model.CMModelLayers;
import com.github.L_Ender.cataclysm.client.model.entity.Laser_Beam_Model;
import com.github.L_Ender.cataclysm.client.render.CMRenderTypes;
import com.github.L_Ender.cataclysm.entity.projectile.Laser_Beam_Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;

public class RenderLaserBeamArrow extends EntityRenderer<AbstractArrow> {
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation("cataclysm", "textures/entity/harbinger/laser_beam.png");
    private static final RenderType RENDER_TYPE_RED= CMRenderTypes.CMEyes(TEXTURE_RED);;
    public Laser_Beam_Model model;

    public RenderLaserBeamArrow(EntityRendererProvider.Context mgr) {
        super(mgr);
        this.model = new Laser_Beam_Model(mgr.bakeLayer(CMModelLayers.LASER_BEAM_MODEL));
    }

    public void render(AbstractArrow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        Vec3 vec3 = entity.getDeltaMovement();
        float yRot = (float)(Mth.atan2(vec3.z, vec3.x) * (180D / Math.PI)) + 90.0F;
        float xRot = (float)(-(Mth.atan2(vec3.y, Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z)) * (180D / Math.PI)));
        this.model.setupAnim(yRot,xRot);
        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE_RED);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(AbstractArrow entity) {
        return TEXTURE_RED;
    }
}
