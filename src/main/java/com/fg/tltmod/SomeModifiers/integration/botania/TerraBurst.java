package com.fg.tltmod.SomeModifiers.integration.botania;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class TerraBurst extends NoLevelsModifier implements ModifyBurstModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST);
        hookBuilder.addModule(new ModifierTraitModule(TltCoreModifiers.MANA_BURST_HANDLER.getId(),1,true));
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
        burstExtras.addBaseDamage(7*modifier.getLevel());
    }
}
