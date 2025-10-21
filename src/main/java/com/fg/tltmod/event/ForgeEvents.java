package com.fg.tltmod.event;

import com.aizistral.enigmaticlegacy.api.capabilities.EnigmaticCapabilities;
import com.aizistral.enigmaticlegacy.api.capabilities.IPlaytimeCounter;
import com.aizistral.enigmaticlegacy.api.capabilities.PlayerPlaytimeCounter;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TltCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final Map<UUID, CompoundTag> CLONE_CACHE = new HashMap<>();
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

//    @SubscribeEvent
//    public void playerClone(PlayerEvent.Clone event) {
//        Player newPlayer = event.getEntity();
//        Player oldPlayer = event.getOriginal();
//        if (newPlayer instanceof ServerPlayer && oldPlayer instanceof ServerPlayer) {
//            oldPlayer.reviveCaps();
//            oldPlayer.getCapability(EnigmaticCapabilities.PLAYTIME_COUNTER).ifPresent(oldCap -> {
//                newPlayer.getCapability(EnigmaticCapabilities.PLAYTIME_COUNTER).ifPresent(newCap -> {
//                    newCap.deserializeNBT(oldCap.serializeNBT());
//                });
//            });
//
//            oldPlayer.invalidateCaps();
//        }
//    }


}