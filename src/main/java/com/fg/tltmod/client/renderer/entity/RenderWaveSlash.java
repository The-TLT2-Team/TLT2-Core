package com.fg.tltmod.client.renderer.entity;

import com.c2h6s.tinkers_advanced.TinkersAdvanced;
import com.c2h6s.tinkers_advanced.util.RenderUtil;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Random;

public class RenderWaveSlash extends EntityRenderer<WaveSlashEntity> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TinkersAdvanced.MODID,"textures/entity/plasma_beam/white.png");
    public RenderWaveSlash(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(WaveSlashEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(WaveSlashEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.rotDeg==0){
            Random random = new Random();
            pEntity.rotDeg=random.nextFloat()*60-30;
        }
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot()));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-pEntity.getXRot()));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(pEntity.rotDeg));
        PoseStack.Pose pose = pPoseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(TEXTURE));
        Random random = new Random(pEntity.hashCode());
        float colTick = (pEntity.tickCount+pPartialTick+random.nextFloat()*40)%30;
        float tick = pEntity.tickCount+pPartialTick;
        float percent = Mth.clamp((5-(pEntity.tickCount-8))/5f,0,1);

        float radius = 2.5f*percent;
        double deg = 26;
        float d = Math.min(tick*2.5F,5);
        for (int i =0;i<16;i++){
            float z0 = (float) Math.sin(Math.toRadians(deg))*radius;
            float z1 = (float) Math.sin(Math.toRadians(deg+8))*radius;
            float x0 = (float) Math.cos(Math.toRadians(deg))*radius;
            float x1 = (float) Math.cos(Math.toRadians(deg+8))*radius;
            float a0 = (float) (1-Math.pow((90-deg)/70,2))*percent;
            float a1 = (float) (1-Math.pow((82-deg)/70,2))*percent;
            float a0h = Math.min(a0+0.4F,1);
            float a1h = Math.min(a1+0.4F,1);
            renderRhomboid(0.4f,x0,x1,z0+0.05f,z1+0.05f,0.05f,1,1,1,
                    getR(colTick),getG(colTick),getB(colTick),a0h,0,0,a1h,
                    normalMatrix,poseMatrix,consumer);
            renderRhomboid(0.4f,x0,x1,z0,z1,0.05f,1,1,1,
                    1,1,1,a0h,a0h,a1h,a1h,
                    normalMatrix,poseMatrix,consumer);
            renderRhomboid(0.4f,x0,x1,z0,z1,-0.1f,1,1,1,
                    getR(colTick),getG(colTick),getB(colTick),a0h,a0,a1,a1h,
                    normalMatrix,poseMatrix,consumer);
            renderRhomboid(0.4f,x0,x1,z0-0.1f,z1-0.1f,-d,getR(colTick),getG(colTick),getB(colTick),
                    getR(colTick),getG(colTick),getB(colTick),a0,0,0,a1,
                    normalMatrix,poseMatrix,consumer);
            deg+=8;
        }
        pPoseStack.popPose();
    }

    public static void renderRhomboid(float y,float x0,float x1,float z0,float z1,float d,int r,int g,int b,int r1,int g1,int b1,float a00,float a01,float a11,float a10,
                                       Matrix3f normalMatrix,Matrix4f poseMatrix,VertexConsumer consumer){
        consumer.vertex(poseMatrix,x0,y,z0).color(r,g,b,a00).uv(0,0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, -1, 0).endVertex();
        consumer.vertex(poseMatrix,x0,y,z0+d).color(r1,g1,b1,a01).uv(0,1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, -1, 0).endVertex();
        consumer.vertex(poseMatrix,x1,y,z1+d).color(r1,g1,b1,a11).uv(1,1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, -1, 0).endVertex();
        consumer.vertex(poseMatrix,x1,y,z1).color(r,g,b,a10).uv(1,0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, -1, 0).endVertex();
    }

    public static int getB(float tick){
        if (tick>=15&&tick<25) return 255;
        if (tick>=10&&tick<15) return (int) (55+40*(tick-10));
        if (tick>25) return (int) (255-40*(tick-25));
        return 55;
    }
    public static int getG(float tick){
        if (tick>=15&&tick<20) return (int) (255-40*(tick-15));
        if (tick>=5&&tick<15) return 255;
        if (tick<5) return (int) (55+40*tick);
        return 55;
    }
    public static int getR(float tick){
        if (tick<5||tick>=25) return 255;
        if (tick>=5&&tick<10) return (int) (255-40*(tick-5));
        if (tick>=20) return (int) (55+40*(tick-20));
        return 55;
    }
}
