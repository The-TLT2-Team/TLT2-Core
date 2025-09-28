package com.fg.tltmod.SomeModifiers.melee;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class Stop extends NoLevelsModifier implements MeleeHitModifierHook {

    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() instanceof Mob mob){
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            mob.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
            mob.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            mob.setAggressive(false);
            mob.getNavigation().stop();
            mob.setNoActionTime(40);
        }
    }
}
