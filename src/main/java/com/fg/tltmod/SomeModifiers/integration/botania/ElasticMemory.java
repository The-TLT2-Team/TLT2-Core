package com.fg.tltmod.SomeModifiers.integration.botania;

import com.fg.tltmod.SomeModifiers.integration.botania.base.BasicBurstModifier;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class ElasticMemory extends BasicBurstModifier {
    @Override
    public void updateBurst(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtra) {
        int level = Math.min(3,modifier.getLevel());
        int ticks = 6-level;
        var entity = burst.entity();
        if (entity.tickCount%ticks==0){
            burstExtra.tltmod$clearHitList();
        }
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return super.getDisplayName(Math.min(3,level));
    }
}
