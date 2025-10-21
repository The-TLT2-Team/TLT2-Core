package com.fg.tltmod.SomeModifiers.integration.botania;

import com.fg.tltmod.SomeModifiers.integration.botania.base.BasicBurstModifier;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class PulseEngine extends BasicBurstModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void updateBurst(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtra) {
        var entity = burst.entity();
        entity.setDeltaMovement(entity.getDeltaMovement().scale(1.1f));
    }
}
