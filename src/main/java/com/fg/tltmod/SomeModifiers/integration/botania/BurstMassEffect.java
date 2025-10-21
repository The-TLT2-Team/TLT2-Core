package com.fg.tltmod.SomeModifiers.integration.botania;

import com.fg.tltmod.SomeModifiers.integration.botania.base.BasicBurstModifier;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.BurstDamageModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class BurstMassEffect extends BasicBurstModifier implements BurstDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, TltCoreModifierHook.BURST_DAMAGE);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getBurstDamage(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst, IManaBurstMixin burstExtra, float baseDamage, float damage) {
        return damage+baseDamage*(float) burst.entity().getDeltaMovement().length();
    }
}
