package com.fg.tltmod.SomeModifiers.integration.botania;

import com.fg.tltmod.SomeModifiers.integration.botania.base.BasicBurstModifier;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class BurstAccelerate extends BasicBurstModifier {
    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
        var entity = burst.entity();
        entity.setDeltaMovement(entity.getDeltaMovement().scale(modifier.getLevel()*0.5));
    }
}
