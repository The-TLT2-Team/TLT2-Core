package com.fg.tltmod.client.renderer.entity;

import com.c2h6s.tinkers_advanced.TinkersAdvanced;
import com.c2h6s.tinkers_advanced.util.RenderUtil;
import com.fg.tltmod.content.entity.IonizedArrowEntity;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import com.fg.tltmod.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderIonizedArrow extends EntityRenderer<IonizedArrowEntity> {
    public final int rgbInner;
    public final int rgbOuter;
    public final float alphaInner;
    public final float alphaOuter;
    public RenderIonizedArrow(EntityRendererProvider.Context pContext, int rgbInner, int rgbOuter, float alphaInner, float alphaOuter) {
        super(pContext);
        this.rgbInner = rgbInner;
        this.rgbOuter = rgbOuter;
        this.alphaInner = alphaInner;
        this.alphaOuter = alphaOuter;
    }
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TinkersAdvanced.MODID,"textures/entity/plasma_beam/white.png");
    @Override
    public ResourceLocation getTextureLocation(IonizedArrowEntity pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(IonizedArrowEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount>1){
            float velocity = pEntity.getVelocity();
            pPoseStack.pushPose();
            if (pEntity.tickCount<=10&&pEntity.getOwner() instanceof LivingEntity living) {
                Vec3 offset = living.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(0.5-0.05*pEntity.tickCount);
                if (pEntity.isOffhand()) offset.reverse();
                pPoseStack.translate(offset.x,offset.y,offset.z);
            }
            pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot()));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-pEntity.getXRot()));
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            VertexConsumer consumer = pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(TEXTURE));
            RenderUtils.renderReducingCylindrical(0.1f,0f,new Vector3f(0,0,0),velocity*0.15f,4,rgbInner,alphaInner,rgbInner,alphaInner,consumer,pPoseStack,poseMatrix,normalMatrix);
            RenderUtils.renderReducingCylindrical(0.1f,0f,new Vector3f(0,0,0),-velocity*0.2f,4,rgbInner,alphaInner,rgbInner,alphaInner,consumer,pPoseStack,poseMatrix,normalMatrix);
            RenderUtils.renderReducingCylindrical(0.125f,0f,new Vector3f(0,0,0),velocity*0.175f,4,rgbOuter,alphaOuter,rgbOuter,alphaOuter,consumer,pPoseStack,poseMatrix,normalMatrix);
            RenderUtils.renderReducingCylindrical(0.125f,0.1f,new Vector3f(0,0,0),-velocity*0.3f,4,rgbOuter,alphaOuter,rgbOuter,alphaOuter,consumer,pPoseStack,poseMatrix,normalMatrix);
            RenderUtils.renderReducingCylindrical(0.1f,0.05f,new Vector3f(0,0,-velocity*0.3f),-velocity*0.9f,4,rgbOuter,alphaOuter,rgbOuter,0,consumer,pPoseStack,poseMatrix,normalMatrix);
            pPoseStack.popPose();
        }
    }
}
