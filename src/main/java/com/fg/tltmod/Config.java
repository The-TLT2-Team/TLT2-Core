package com.fg.tltmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TltCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue PLAYER_HUNGER;
    private static final ForgeConfigSpec.IntValue MAX_HUNGER;
    private static final ForgeConfigSpec.BooleanValue GUI;
    private static final ForgeConfigSpec.BooleanValue MULTI_GUI;


    public static boolean playerHunger;
    public static int maxHunger;
    public static boolean gui;
    public static boolean multiGui;

    static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("大胃袋（吃不停）相关配置");
        {
            PLAYER_HUNGER = BUILDER
                    .comment("Whether to enable storing hunger")
                    .comment("是否启用存储饥饿")
                    .define("player_hunger", true);
            MAX_HUNGER = BUILDER
                    .comment("Player's maximum hunger value, default is 20, range: (1-10000)")
                    .comment("玩家最大饥饿值, 默认为20,取值范围:(1-10000)")
                    .defineInRange("max_hunger", 20, 1, 10000);
            GUI = BUILDER
                    .comment("Enable modGUI rendering")
                    .comment("开启modGUI渲染")
                    .define("GUI", true);
            MULTI_GUI = BUILDER
                    .comment("Enable multi-gui rendering")
                    .comment("开启多排渲染")
                    .define("MULTI_GUI", true);
        }
        BUILDER.pop();


        SPEC = BUILDER.build();
    }


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        playerHunger = PLAYER_HUNGER.get();
        maxHunger = MAX_HUNGER.get();;
        gui = GUI.get();
        multiGui = MULTI_GUI.get();
    }

    public static boolean isPlayerHungerEnabled() {
        return PLAYER_HUNGER.get();
    }

    public static int getMaxHunger() {
        return MAX_HUNGER.get();
    }

    public static boolean getGUI() {
        return GUI.get();
    }

    public static boolean isMultiGui() {
        return MULTI_GUI.get();
    }
}
