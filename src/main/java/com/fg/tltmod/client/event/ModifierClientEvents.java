package com.fg.tltmod.client.event;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.SomeModifiers.armor.BlessingFromFoxGod;
import com.fg.tltmod.TltCore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.api.entity.render.RenderEffects;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = TltCore.MODID,value = Dist.CLIENT)
public class ModifierClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        float partialTick = event.getPartialTick();
        player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
            int lvl = cap.get(BlessingFromFoxGod.KEY_BLESS, 0);
            if (lvl > 0) {
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                double px = Mth.lerp(partialTick, player.xo, player.getX());
                double py = Mth.lerp(partialTick, player.yo, player.getY());
                double pz = Mth.lerp(partialTick, player.zo, player.getZ());

                Camera camera = mc.gameRenderer.getMainCamera();
                double camX = camera.getPosition().x;
                double camY = camera.getPosition().y;
                double camZ = camera.getPosition().z;

                float yaw = Mth.lerp(partialTick, player.yRotO, player.getYRot());

                double distance = 0.5;
                double rad = Math.toRadians(yaw + 180);
                double offsetX = Math.sin(rad) * distance;
                double offsetZ = -Math.cos(rad) * distance;

                poseStack.translate(
                        px - camX + offsetX,
                        py - camY + player.getBbHeight() * 0.5,
                        pz - camZ + offsetZ
                );

                poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                poseStack.translate(0,0,0.2);

                RenderEffects.RegularPolygon.render(
                        poseStack,
                        mc.renderBuffers().bufferSource()
                );

                poseStack.popPose();
            }
        });
    }

}
