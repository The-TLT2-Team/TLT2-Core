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

        player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
            int lvl = cap.get(BlessingFromFoxGod.KEY_BLESS, 0);
            if (lvl > 0) {
                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();

                Camera camera = mc.gameRenderer.getMainCamera();
                double camX = camera.getPosition().x;
                double camY = camera.getPosition().y;
                double camZ = camera.getPosition().z;

                float yaw = player.getYRot();

                double distance = 0.5;
                double offsetX = Math.sin(Math.toRadians(yaw)) * distance;
                double offsetZ = -Math.cos(Math.toRadians(yaw)) * distance;

                poseStack.translate(
                        player.getX() - camX + offsetX,
                        player.getY() - camY + player.getBbHeight() * 0.5, // 背后高度中间
                        player.getZ() - camZ + offsetZ
                );

                poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));

                RenderEffects.RegularPolygon.render(
                        poseStack,
                        mc.renderBuffers().bufferSource()
                );

                poseStack.popPose();
            }
        });
    }

}
