package com.fg.tltmod.SomeModifiers.integration.botania;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.SomeModifiers.integration.botania.base.BasicBurstModifier;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstDamageSourceModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.google.common.hash.HashCode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

public class SourceObfuscate extends BasicBurstModifier implements ModifyBurstDamageSourceModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST_DAMAGE_SOURCE);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public @NotNull LegacyDamageSource modifyBurstSource(IToolStackView tool, ManaBurst burst, IManaBurstMixin burstExtra, @NotNull Entity owner, @NotNull Entity target, LegacyDamageSource source) {
        return source.setMsgId(source.getMsgId() + HashCode.fromInt(RANDOM.nextInt()));
    }

    @Override
    public LegacyDamageSource modifySourceWhenTriggerTool(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        return source.setMsgId(source.getMsgId() + HashCode.fromInt(RANDOM.nextInt()));
    }
}
