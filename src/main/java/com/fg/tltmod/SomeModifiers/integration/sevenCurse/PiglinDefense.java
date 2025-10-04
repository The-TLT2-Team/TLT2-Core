package com.fg.tltmod.SomeModifiers.integration.sevenCurse;

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
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class PiglinDefense extends Modifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> PIGLIN_DEFENSE = TltCore.createKey("piglin_defense");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(PIGLIN_DEFENSE,false, TinkerTags.Items.MODIFIABLE));
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        Entity target = event.getNewTarget();
        if (target instanceof Player player && SuperpositionHandler.isTheCursedOne(player)) {
            if (entity instanceof AbstractPiglin ||entity instanceof ZombifiedPiglin){
                target.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                    if (cap.get(PIGLIN_DEFENSE, 0) > 0) event.setCanceled(true);
                });
            }
        }
    }
}
