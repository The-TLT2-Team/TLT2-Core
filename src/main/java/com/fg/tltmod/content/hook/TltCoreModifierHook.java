package com.fg.tltmod.content.hook;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.hook.modifier.*;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class TltCoreModifierHook {
    public static final ModuleHook<UpdateBurstModifierHook> UPDATE_BURST = ModifierHooks.register(TltCore.getResource("update_burst"), UpdateBurstModifierHook.class, UpdateBurstModifierHook.AllMerger::new, new UpdateBurstModifierHook() {});
    public static final ModuleHook<ModifyBurstModifierHook> MODIFY_BURST = ModifierHooks.register(TltCore.getResource("modify_burst"), ModifyBurstModifierHook.class, ModifyBurstModifierHook.AllMerger::new, new ModifyBurstModifierHook() {});
    public static final ModuleHook<BurstDamageModifierHook> BURST_DAMAGE = ModifierHooks.register(TltCore.getResource("burst_damage"), BurstDamageModifierHook.class, BurstDamageModifierHook.AllMerger::new, (tool,modifier, modifierList, owner, target, burst, baseDamage, damage) -> damage);
    public static final ModuleHook<BurstHitModifierHook> BURST_HIT = ModifierHooks.register(TltCore.getResource("burst_hit"), BurstHitModifierHook.class,BurstHitModifierHook.merger::new, new BurstHitModifierHook() {});
    public static final ModuleHook<LensProviderModifierHook> LENS_PROVIDER = ModifierHooks.register(TltCore.getResource("lens_provider"), LensProviderModifierHook.class,LensProviderModifierHook.AllMerger::new, new LensProviderModifierHook() {});

}
