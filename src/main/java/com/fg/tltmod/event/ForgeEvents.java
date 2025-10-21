package com.fg.tltmod.event;

import com.aizistral.enigmaticlegacy.api.capabilities.IPlaytimeCounter;
import com.fg.tltmod.TltCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TltCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (newPlayer instanceof ServerPlayer && oldPlayer instanceof ServerPlayer && !event.isWasDeath()) {
            oldPlayer.revive();
            var oldCounter = IPlaytimeCounter.get(oldPlayer);
            var newCounter = IPlaytimeCounter.get(newPlayer);
            newCounter.deserializeNBT(oldCounter.serializeNBT());
        }

    }
}