package com.fg.tltmod.SomeModifiers.sevenCurse;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fg.tltmod.TltCore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class PiglinDefense extends Modifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> PIGLIN_DEFENSE = TltCore.createKey("piglin_defense");
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        Entity target = event.getNewTarget();
        if (target instanceof Player player && SuperpositionHandler.isTheCursedOne(player)) {
            if (entity instanceof AbstractPiglin ||entity instanceof ZombifiedPiglin){
                if (entity.getLastAttacker()!=target){
                    target.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                        if (cap.get(PIGLIN_DEFENSE, 0) > 0) event.setCanceled(true);
                    });
                }
            }
        }
    }
}
