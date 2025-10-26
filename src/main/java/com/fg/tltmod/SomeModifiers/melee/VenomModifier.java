package com.fg.tltmod.SomeModifiers.melee;

import com.fg.tltmod.Register.TltCoreEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class VenomModifier extends Modifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity entity = context.getLivingTarget();
        int lvl = modifier.getLevel();
        if (entity!=null) {
            MobEffectInstance cur = entity.getEffect(TltCoreEffects.venom.get());
            if (cur == null || cur.getAmplifier() < lvl) {
                entity.addEffect(new MobEffectInstance(
                        TltCoreEffects.venom.get(),
                        200,
                        lvl - 1,
                        true,
                        true,
                        true
                ));
            }
        }
    }
}
