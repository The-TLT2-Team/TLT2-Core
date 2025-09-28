package com.fg.tltmod.client.renderer.entity;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.client.renderer.RenderType.TLTRenderType;
import com.fg.tltmod.content.entity.LaserEntity;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.GlowRenderLayer;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class LaserEntityRenderer extends EntityRenderer<LaserEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TltCore.MODID, "textures/entity/laser.png");
    private static final RenderType ring = new GlowRenderLayer(STRenderType.createSphereRenderType2(RendererUtils.runes), (float[])null, 1.0F, false);
    private static final float TEXTURE_WIDTH = 256;
    private static final float TEXTURE_HEIGHT = 32;
    private static final float START_RADIUS = 0.75f;
    private static final float BEAM_RADIUS = 0.75F;
    private boolean clearerView = false;
    private int frameCounter = 0;
    private float[] getRainbowColor(float t, float alpha) {
        float r = 0.5f + 0.5f * (float)Math.sin(t + 0);
        float g = 0.5f + 0.5f * (float)Math.sin(t + 2);
        float b = 0.5f + 0.5f * (float)Math.sin(t + 4);
        return new float[]{r, g, b, alpha};
    }

    public LaserEntityRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public ResourceLocation getTextureLocation(LaserEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(LaserEntity laser, float entityYaw, float delta, PoseStack poseStack, MultiBufferSource source, int packedLightIn) {

        clearerView = laser.caster instanceof Player && Minecraft.getInstance().player == laser.caster && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;

        double collidePosX = laser.prevCollidePosX + (laser.collidePosX - laser.prevCollidePosX) * delta;
        double collidePosY = laser.prevCollidePosY + (laser.collidePosY - laser.prevCollidePosY) * delta;
        double collidePosZ = laser.prevCollidePosZ + (laser.collidePosZ - laser.prevCollidePosZ) * delta;
        double posX = laser.xo + (laser.getX() - laser.xo) * delta;
        double posY = laser.yo + (laser.getY() - laser.yo) * delta;
        double posZ = laser.zo + (laser.getZ() - laser.zo) * delta;
        float yaw = laser.prevYaw + (laser.renderYaw - laser.prevYaw) * delta;
        float pitch = laser.prevPitch + (laser.renderPitch - laser.prevPitch) * delta;

        float length = (float) Math.sqrt(Math.pow(collidePosX - posX, 2) + Math.pow(collidePosY - posY, 2) + Math.pow(collidePosZ - posZ, 2));
        int frame = Mth.floor((laser.appear.getTimer() - 1 + delta) * 2);
        if (frame < 0) {
            frame = 6;
        }
        float rot = laser.tickCount * 10f;
        VertexConsumer ivertexbuilder = source.getBuffer(TLTRenderType.getGlowingEffect(getTextureLocation(laser)));

        renderStart(frame, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, poseStack, ivertexbuilder, packedLightIn);
        renderBeam( rot,length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, poseStack, ivertexbuilder, packedLightIn);
        poseStack.pushPose();
        poseStack.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, laser.blockSide, poseStack, ivertexbuilder, packedLightIn);
        poseStack.popPose();
    }

    private void renderRegular(float rot,float yaw,float pitch,PoseStack poseStack) {
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        //RenderSystem.enableDepthTest();
        //RenderSystem.depthMask(true);
        poseStack.pushPose();
        //1
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 1F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot)));
        RendererUtils.renderRegularPolygon(poseStack, source, 1.0F, 3F, 0.3F, 1024, 1.0F, 1.0F, 1.0F, 1.0F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F, true);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        RendererUtils.renderRegularPolygon(poseStack, source, 1.0F, 3, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F, true);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        RendererUtils.renderRing(poseStack, source, (double)0.0F, 90.0F, 1.1F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //2
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 2F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot)));
        RendererUtils.renderRegularPolygon(poseStack, source, 1.5F, 3.0F, 0.3F, 1024, 1.0F, 1.0F, 1.0F, 1.0F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F, true);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        RendererUtils.renderRegularPolygon(poseStack, source, 1.5F, 3, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F, true);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*2.4936)));
        RendererUtils.renderRing(poseStack, source, (double)0.0F, 90.0F, 1.3F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //3
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 3F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*1.2)));
        RendererUtils.renderRegularPolygon(poseStack, source, 2.0F, 4.0F, 0.3F, 1024, 0.6F, 0.6F, 1.0F, 0.8F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F, true);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        RendererUtils.renderRegularPolygon(poseStack, source, 2.1F, 4, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F, true);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        RendererUtils.renderRing(poseStack, source, (double)0.0F, 90.0F, 2.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //4
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 4F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*1.2)));
        RendererUtils.renderRegularPolygon(poseStack, source, 2.5F, 4, 0.3F, 1024, 0.6F, 0.6F, 1.0F, 0.8F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F, true);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        RendererUtils.renderRegularPolygon(poseStack, source, 2.6F, 4, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F, true);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*2.4936)));
        RendererUtils.renderRing(poseStack, source, (double)0.0F, 90.0F, 2.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //5
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 5F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot)));
        RendererUtils.renderRegularPolygon(poseStack, source, 3.0F, 5.0F, 0.4F, 1024, 0.4F, 0.4F, 0.4F, 0.4F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F, true);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        RendererUtils.renderRegularPolygon(poseStack, source, 3.2F, 5, 0.16F, 1024, 0.6F, 0.8F, 0.7F, 1.0F, true);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        RendererUtils.renderRing(poseStack, source, (double)0.0F, 90.0F, 3.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //
        poseStack.popPose();
        //RenderSystem.disableDepthTest();
        source.endBatch();
    }

    private void renderFlatQuad(int frame,float yaw,float pitch, PoseStack poseStack, VertexConsumer builder, int packedLightIn) {
        float minU = 0 + 16F / TEXTURE_WIDTH * frame;
        float minV = 0;
        float maxU = minU + 16F / TEXTURE_WIDTH;
        float maxV = minV + 16F / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = poseStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, -START_RADIUS, 0, minU, minV, 1,1,1,1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, -START_RADIUS, START_RADIUS, 0, minU, maxV, 1,1,1,1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, START_RADIUS, 0, maxU, maxV, 1,1,1,1, packedLightIn);
        drawVertex(matrix4f, matrix3f, builder, START_RADIUS, -START_RADIUS, 0, maxU, minV, 1,1,1,1, packedLightIn);
    }

    private void renderStart(int frame,float yaw,float pitch, PoseStack poseStack, VertexConsumer builder, int packedLightIn) {
        if (clearerView) {
            return;
        }
        poseStack.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(quat);
        renderFlatQuad(frame,yaw,pitch, poseStack, builder, packedLightIn);
        poseStack.popPose();
    }

    private void renderEnd(int frame,float yaw,float pitch, Direction side, PoseStack poseStack, VertexConsumer builder, int packedLightIn) {
        poseStack.pushPose();
        Quaternionf quat = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(quat);
        renderFlatQuad(frame,yaw,pitch, poseStack, builder, packedLightIn);
        poseStack.popPose();
        if (side == null) {
            return;
        }
        poseStack.pushPose();
        Quaternionf sideQuat = side.getRotation();
        sideQuat.mul((new Quaternionf()).rotationX(90 * ((float)Math.PI / 180F)));
        poseStack.mulPose(sideQuat);
        poseStack.translate(0, 0, -0.01f);
        renderFlatQuad(frame,yaw,pitch, poseStack, builder, packedLightIn);
        poseStack.popPose();


    }

    private void drawBeam(float length,float yaw,float pitch,float rot, int frame, PoseStack poseStack, VertexConsumer builder, int packedLightIn) {
        float minU = 0;
        float minV = 16 / TEXTURE_HEIGHT + 1 / TEXTURE_HEIGHT * frame;
        float maxU = minU + 20 / TEXTURE_WIDTH;
        float maxV = minV + 1 / TEXTURE_HEIGHT;
        PoseStack.Pose matrixstack$entry = poseStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        float offset = clearerView ? -1 : 0;

        // 每个顶点用不同的渐变颜色
        float[] color0 = getRainbowColor(0 + frameCounter * 0.1f, 0.5f);
        float[] color1 = getRainbowColor(0.25f + frameCounter * 0.1f, 0.5f);
        float[] color2 = getRainbowColor(0.5f + frameCounter * 0.1f, 0.5f);
        float[] color3 = getRainbowColor(0.75f + frameCounter * 0.1f, 0.5f);
        builder
                .vertex(matrix4f, -BEAM_RADIUS, offset, 0)
                .color(color0[0], color0[1], color0[2], color0[3])
                .uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn)
                .normal(matrix3f, 0f, 1f, 0f).endVertex();

        builder
                .vertex(matrix4f, -BEAM_RADIUS, length, 0)
                .color(color1[0], color1[1], color1[2], color1[3])
                .uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn)
                .normal(matrix3f, 0f, 1f, 0f).endVertex();

        builder
                .vertex(matrix4f, BEAM_RADIUS, length, 0)
                .color(color2[0], color2[1], color2[2], color2[3])
                .uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn)
                .normal(matrix3f, 0f, 1f, 0f).endVertex();

        builder
                .vertex(matrix4f, BEAM_RADIUS, offset, 0)
                .color(color3[0], color3[1], color3[2], color3[3])
                .uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn)
                .normal(matrix3f, 0f, 1f, 0f).endVertex();

        frameCounter += 1;

    }

    private void renderBeam(float rot, float length, float yaw, float pitch, int frame, PoseStack poseStack, VertexConsumer builder, int packedLightIn) {
        poseStack.pushPose();
        poseStack.mulPose((new Quaternionf()).rotationX(90 * ((float)Math.PI / 180F)));
        poseStack.mulPose((new Quaternionf()).rotationZ((yaw - 90f) * ((float)Math.PI / 180F) ));
        poseStack.mulPose((new Quaternionf()).rotationX(-pitch * ((float)Math.PI / 180F)));
        poseStack.pushPose();
        if (!clearerView) {
            poseStack.mulPose((new Quaternionf()).rotationY((Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() + 90)));
        }
        drawBeam(length,yaw,pitch,rot, frame, poseStack, builder, packedLightIn);
        poseStack.popPose();

        if (!clearerView) {
            poseStack.pushPose();
            poseStack.mulPose((new Quaternionf()).rotationY((-Minecraft.getInstance().gameRenderer.getMainCamera().getXRot() - 90) * ((float)Math.PI / 180F)));
            drawBeam(length,yaw,pitch,rot, frame, poseStack, builder, packedLightIn);
            poseStack.popPose();


            poseStack.pushPose();
            poseStack.mulPose((new Quaternionf()).rotationX((float)Math.toRadians(-90)));
            renderRegular(rot,yaw,pitch,poseStack);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY,float r,float g, float b, float alpha, int packedLightIn) {
        vertexBuilder
                .vertex(matrix, offsetX, offsetY, offsetZ)
                .color(r, g, b, 1 * alpha)
                .uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLightIn)
                .normal(normals, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}

