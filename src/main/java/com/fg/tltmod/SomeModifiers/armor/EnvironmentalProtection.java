package com.fg.tltmod.SomeModifiers.armor;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class EnvironmentalProtection extends EtSTBaseModifier implements DamageBlockModifierHook , ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK,ModifierHooks.MODIFY_HURT);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount) {
        return source.is(DamageTypeTags.IS_FALL)||source.is(DamageTypeTags.IS_DROWNING)||
                source.is(DamageTypeTags.IS_LIGHTNING)||source.is(DamageTypeTags.IS_FIRE)||
                source.is(DamageTypeTags.IS_EXPLOSION)||source.is(DamageTypeTags.IS_FREEZING)||
                source.is(DamageTypeTags.DAMAGES_HELMET);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.getEntity()==null&&source.getDirectEntity()==null) return amount*0.7f;
        return amount;
    }
}
