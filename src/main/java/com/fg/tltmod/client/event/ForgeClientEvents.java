package com.fg.tltmod.client.event;

import com.fg.tltmod.Register.TltCoreKeys;
import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.network.PacketHandler;
import com.ssakura49.sakuratinker.network.c2s.ModifierKeyPressPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.fg.tltmod.Register.TltCoreKeys.*;

@Mod.EventBusSubscriber(
        modid = TltCore.MODID,
        value = {Dist.CLIENT},
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ForgeClientEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null) {
            if (TltCoreKeys.getSwitchKey().consumeClick()) {
                PacketHandler.sendToServer(new ModifierKeyPressPacket(SWITCH_KEY_ID));
            }
        }
    }
}
