package com.fg.tltmod.SomeModifiers.armor;

import com.fg.tltmod.TltCore;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.armor.EffectImmunityModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class GravityManipulate extends NoLevelsModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_GRAVITY_MANIPULATE = TinkerDataCapability.TinkerDataKey.of(TltCore.getResource("gravity_manipulate"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_GRAVITY_MANIPULATE,false,null));
        hookBuilder.addModule(new EffectImmunityModule(LHEffects.GRAVITY.get()));
        hookBuilder.addModule(new EffectImmunityModule(LHEffects.MOONWALK.get()));
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event){
        if (event.getEntity().getAbilities().flying) {
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                int level = cap.get(KEY_GRAVITY_MANIPULATE, 0);
                if (level > 0&&event.getNewSpeed()<event.getOriginalSpeed()*4) event.setNewSpeed(event.getOriginalSpeed()*4);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if (event.getEntity() instanceof Player player&&player.getAbilities().flying) {
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                int level = cap.get(KEY_GRAVITY_MANIPULATE, 0);
                if (level > 0) event.setAmount(event.getAmount() * 0.8f);
            });
        }
        if (event.getSource().getEntity() instanceof Player player&&player.getAbilities().flying){
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                int level = cap.get(KEY_GRAVITY_MANIPULATE, 0);
                if (level > 0) {
                    event.setAmount(event.getAmount() * 1.2f);
                    if (!event.getEntity().onGround()) event.setAmount(event.getAmount() * 1.2f);
                }
            });
        }
    }
}
