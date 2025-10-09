package com.fg.tltmod.content.hook;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.hooks.ModifyDamageSourceModifierHook;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.hook.modifier.*;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import javax.swing.text.html.parser.Entity;

public class TltCoreModifierHook {
    public static final ModuleHook<UpdateBurstModifierHook> UPDATE_BURST = ModifierHooks.register(TltCore.getResource("update_burst"), UpdateBurstModifierHook.class, UpdateBurstModifierHook.AllMerger::new, new UpdateBurstModifierHook() {});
    public static final ModuleHook<ModifyBurstModifierHook> MODIFY_BURST = ModifierHooks.register(TltCore.getResource("modify_burst"), ModifyBurstModifierHook.class, ModifyBurstModifierHook.AllMerger::new, new ModifyBurstModifierHook() {});
    public static final ModuleHook<BurstDamageModifierHook> BURST_DAMAGE = ModifierHooks.register(TltCore.getResource("burst_damage"), BurstDamageModifierHook.class, BurstDamageModifierHook.AllMerger::new, (tool,modifier, modifierList, owner, target, burst,burstExtra, baseDamage, damage) -> damage);
    public static final ModuleHook<BurstHitModifierHook> BURST_HIT = ModifierHooks.register(TltCore.getResource("burst_hit"), BurstHitModifierHook.class,BurstHitModifierHook.merger::new, new BurstHitModifierHook() {});
    public static final ModuleHook<LensProviderModifierHook> LENS_PROVIDER = ModifierHooks.register(TltCore.getResource("lens_provider"), LensProviderModifierHook.class,LensProviderModifierHook.AllMerger::new, new LensProviderModifierHook() {});
    public static final ModuleHook<ModifyBurstDamageSourceModifierHook> MODIFY_BURST_DAMAGE_SOURCE = ModifierHooks.register(TltCore.getResource("modify_burst_source"), ModifyBurstDamageSourceModifierHook.class,ModifyBurstDamageSourceModifierHook.AllMerger::new, (tool, burst, burstExtra, owner, target, source) -> source);

}
