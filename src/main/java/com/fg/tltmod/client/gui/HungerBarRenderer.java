package com.fg.tltmod.client.gui;

import com.fg.tltmod.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.fg.tltmod.TltCore.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HungerBarRenderer {
    public static int foodIconsOffset;
    private static final ResourceLocation MOD_ICONS_TEXTURE = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/fd_icons.png");

    static ResourceLocation FOOD_LEVEL_ELEMENT = ResourceLocation.fromNamespaceAndPath("minecraft","food_level");

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            boolean isMounted = mc.player != null && mc.player.getVehicle() instanceof LivingEntity;
            if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                renderHungerBarOverlay(gui, event.getGuiGraphics());
            }
        }
    }

    public static void renderHungerBarOverlay(ForgeGui gui, GuiGraphics graphics) {
        if (!Config.getGUI()) {
            return;
        }

        foodIconsOffset = gui.rightHeight;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) {
            return;
        }

        FoodData stats = player.getFoodData();
        int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset + 10;
        int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;

        boolean isPlayerHealingWithSaturation =
                player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
                        && player.isHurt()
                        && stats.getFoodLevel() >= 18;

        draw(gui, stats, minecraft, graphics, left, top, isPlayerHealingWithSaturation);

    }

    public static void draw(ForgeGui gui, FoodData stats, Minecraft mc, GuiGraphics graphics, int left, int top, boolean naturalHealing) {
        float saturation = stats.getSaturationLevel();
        int foodLevel = stats.getFoodLevel();
        int ticks = mc.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed(ticks * 312871L);
        int count = foodLevel / 20;
        RenderSystem.enableBlend();

        if (Config.isMultiGui()) {
            // 多排模式
            int firstRowFood = Math.min(foodLevel, 20);
            int secondRowFood = Math.max(foodLevel - 20, 0);

            // 第一排
            for (int j = 0; j < 10; ++j) {
                int x = left - j * 8 - 9;
                int y = top;

                if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
                    y = top + (rand.nextInt(3) - 1);
                }

                graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

                float effectiveHungerOfBar = firstRowFood / 2.0F - j;
                int naturalHealingOffset = naturalHealing ? 18 : 0;

                if (effectiveHungerOfBar >= 1) {
                    graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
                } else if (effectiveHungerOfBar >= .5) {
                    graphics.blit(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
                }
            }

            // 第二排
            if (secondRowFood > 0) {
                int secondRowY = top - 10;
                for (int j = 0; j < 10; ++j) {
                    int x = left - j * 8 - 9;
                    int y = secondRowY;

                    if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
                        y = secondRowY + (rand.nextInt(3) - 1);
                    }

                    graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

                    float effectiveHungerOfBar = secondRowFood / 2.0F - j;
                    int naturalHealingOffset = naturalHealing ? 18 : 0;

                    if (effectiveHungerOfBar >= 1) {
                        graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
                    } else if (effectiveHungerOfBar >= .5) {
                        graphics.blit(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
                    }
                }
            }
        } else {
            // 单排模式
            for (int j = 0; j < 10; ++j) {
                int x = left - j * 8 - 9;
                int y = top;

                if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
                    y = top + (rand.nextInt(3) - 1);
                }

                graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

                float effectiveHungerOfBar = foodLevel / 2.0F - j;
                int naturalHealingOffset = naturalHealing ? 18 : 0;

                if (effectiveHungerOfBar >= 1) {
                    graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
                } else if (effectiveHungerOfBar >= .5) {
                    graphics.blit(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
                }
                int baseX = mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int baseY = mc.getWindow().getGuiScaledHeight() - gui.rightHeight;
                if (count > 0) {
                    graphics.drawString(mc.font, "x" + count, baseX + 7, baseY + 10, 0xFFCC33);
                }
            }
        }

        RenderSystem.disableBlend();
    }

}
