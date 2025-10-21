package com.fg.tltmod.SomeModifiers.integration.botania.base;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.BurstHitModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.content.hook.modifier.UpdateBurstModifierHook;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BasicBurstModifier extends EtSTBaseModifier implements ModifyBurstModifierHook, BurstHitModifierHook, UpdateBurstModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST,TltCoreModifierHook.BURST_HIT,TltCoreModifierHook.UPDATE_BURST);
    }

    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        if (tool.getModifierLevel(TltCoreModifiers.FAR_SIGHTS.get())>0) source = this.modifySourceWhenTriggerTool(tool,entry,attacker
                ,hand,target,sourceSlot,isFullyCharged
                ,isExtraAttack,isCritical,source);
        return source;
    }

    public LegacyDamageSource modifySourceWhenTriggerTool(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source){
        return source;
    }
}
