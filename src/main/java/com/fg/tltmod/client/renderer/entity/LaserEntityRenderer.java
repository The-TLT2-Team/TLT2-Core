package com.fg.tltmod.client.renderer.entity;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.client.renderer.RenderType.TLTRenderType;
import com.fg.tltmod.content.entity.LaserEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.GlowRenderLayer;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
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

import static com.ssakura49.sakuratinker.render.RendererUtils.beam;

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
        renderBeam( rot,length, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, frame, poseStack, ivertexbuilder,source, packedLightIn);
        poseStack.pushPose();
        poseStack.translate(collidePosX - posX, collidePosY - posY, collidePosZ - posZ);
        renderEnd(frame, 180f / (float) Math.PI * yaw, 180f / (float) Math.PI * pitch, laser.blockSide, poseStack, ivertexbuilder, packedLightIn);
        poseStack.popPose();
    }

    private void renderRegular(float rot,float yaw,float pitch,PoseStack poseStack,MultiBufferSource source) {
        //MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        //Tesselator.getInstance().getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        RenderSystem.enableDepthTest();
//        RenderSystem.depthMask(true);
        poseStack.pushPose();

        //1
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 1F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot)));
        renderRegularPolygon(poseStack, source, 1.0F, 3F, 0.3F, 1024, 1.0F, 1.0F, 1.0F, 1.0F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        renderRegularPolygon(poseStack, source, 1.0F, 3, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        renderRing(poseStack, source, (double)0.0F, 90.0F, 1.1F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //2
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 2F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot)));
        renderRegularPolygon(poseStack, source, 1.5F, 3.0F, 0.3F, 1024, 1.0F, 1.0F, 1.0F, 1.0F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        renderRegularPolygon(poseStack, source, 1.5F, 3, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*2.4936)));
        renderRing(poseStack, source, (double)0.0F, 90.0F, 1.3F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //3
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 3F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*1.2)));
        renderRegularPolygon(poseStack, source, 2.0F, 4.0F, 0.3F, 1024, 0.6F, 0.6F, 1.0F, 0.8F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        renderRegularPolygon(poseStack, source, 2.1F, 4, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        renderRing(poseStack, source, (double)0.0F, 90.0F, 2.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //4
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 4F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*1.2)));
        renderRegularPolygon(poseStack, source, 2.5F, 4, 0.3F, 1024, 0.6F, 0.6F, 1.0F, 0.8F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        renderRegularPolygon(poseStack, source, 2.6F, 4, 0.22F, 1024, 0.6F, 0.8F, 0.7F, 1.0F);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(-rot*2.4936)));
        renderRing(poseStack, source, (double)0.0F, 90.0F, 2.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //5
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 5F);
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot)));
        renderRegularPolygon(poseStack, source, 3.0F, 5.0F, 0.4F, 1024, 0.4F, 0.4F, 0.4F, 0.4F, STRenderType.END_PORTAL(RendererUtils.cosmic), 1.0F);
        poseStack.translate(0.0F, 0.0F, -0.01F);
        renderRegularPolygon(poseStack, source, 3.2F, 5, 0.16F, 1024, 0.6F, 0.8F, 0.7F, 1.0F);

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(new Quaternionf().rotationZ((float)Math.toRadians(rot*2.4936)));
        renderRing(poseStack, source, (double)0.0F, 90.0F, 3.2F, 0.19296484F, 64, 128, 128, 0.6F, 0.6F, 0.6F, 0.7F, ring);
        poseStack.popPose();

        poseStack.popPose();
        //
        poseStack.popPose();

//        RenderSystem.disableDepthTest();
//        source.endBatch();
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

    private void renderBeam(float rot, float length, float yaw, float pitch, int frame, PoseStack poseStack, VertexConsumer builder,MultiBufferSource source, int packedLightIn) {
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
            renderRegular(rot,yaw,pitch,poseStack,source);
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

    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource source, float radius, float sides, float width, int packedLight, float r, float g, float b, float a, RenderType renderType, float percentage) {
        float PI = 3.1415925F;
        sides /= 2.0F;
        VertexConsumer bb = source.getBuffer(renderType);
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        Vector4f color = new Vector4f(r, g, b, a);

        for(float alpha = 0.0F; alpha < 2.0F * PI && (!(percentage < 1.0F) || !(alpha / (2.0F * PI) >= percentage)); alpha += PI / sides) {
            double cos = (double)MathUtils.cos(alpha);
            double sin = (double)MathUtils.sin(alpha);
            double cos_ = (double)MathUtils.cos(alpha + PI / sides);
            double sin_ = (double)MathUtils.sin(alpha + PI / sides);
            float x = (float)((double)radius * cos);
            float y = (float)((double)radius * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0.0F, 0.0F, color);
            x = (float)((double)radius * cos_);
            y = (float)((double)radius * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0.0F, 0.0F, color);
            x = (float)((double)(radius - width) * cos_);
            y = (float)((double)(radius - width) * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0.0F, 0.0F, color);
            x = (float)((double)(radius - width) * cos);
            y = (float)((double)(radius - width) * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0.0F, 0.0F, color);
        }
    }

    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource source, float radius, int sides, float width, int packedLight, float r, float g, float b, float a) {
        renderRegularPolygon(stack, source, radius, (float)sides, width, packedLight, r, g, b, a, STRenderType.glow(beam), 1.0F);
    }


    public static void vertexRP(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float x, float y, float u, float v, Vector4f color) {
        vertexConsumer.vertex(matrix4f, x, y, 0.0F).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void renderRing(PoseStack matrix, MultiBufferSource buffer, double cy, float flatness, float radius, float height, int num_segments, int lx, int ly, float r, float g, float b, float a, RenderType type) {
        Matrix4f positionMatrix = matrix.last().pose();
        VertexConsumer bb = buffer.getBuffer(type);
        double theta = 6.2831852 / (double)num_segments;
        double q = (double)height * MathUtils.sin(Math.toRadians((double)flatness));
        double p = (double)radius + (double)height * MathUtils.cos(Math.toRadians((double)flatness));
        double x = (double)0.0F;
        double y = (double)0.0F;
        double z = (double)0.0F;
        double xb = (double)0.0F;
        double yb = (double)0.0F;
        double zb = (double)0.0F;
        float squeeze = 3.0F;
        float texx = 0.0F;

        for(int i = 0; i < num_segments + 1; ++i) {
            texx += squeeze / (float)(num_segments + 1);
            if (texx >= 1.0F) {
                texx = squeeze / (float)(num_segments + 1);
                bb.vertex(positionMatrix, (float)x, (float)y, (float)z).color(r, g, b, a).uv(0.0F, 0.0F).uv2(lx, ly).endVertex();
                bb.vertex(positionMatrix, (float)xb, (float)yb, (float)zb).color(r, g, b, a).uv(0.0F, 1.0F).uv2(lx, ly).endVertex();
            }

            double tt = (double)i * theta;
            x = (double)(-radius) * MathUtils.sin(tt);
            y = cy;
            z = (double)radius * MathUtils.cos(tt);
            xb = -p * MathUtils.sin(tt);
            yb = cy - q;
            zb = p * MathUtils.cos(tt);
            bb.vertex(positionMatrix, (float)x, (float)cy, (float)z).color(r, g, b, a).uv(texx, 0.0F).uv2(lx, ly).endVertex();
            bb.vertex(positionMatrix, (float)xb, (float)yb, (float)zb).color(r, g, b, a).uv(texx, 1.0F).uv2(lx, ly).endVertex();
        }
    }
}

