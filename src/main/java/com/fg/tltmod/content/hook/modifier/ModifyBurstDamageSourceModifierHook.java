package com.fg.tltmod.content.hook.modifier;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;
import java.util.Collection;

public interface ModifyBurstDamageSourceModifierHook {
    @NotNull LegacyDamageSource modifyBurstSource(IToolStackView tool, ManaBurst burst, IManaBurstMixin burstExtra, @NotNull Entity owner, @NotNull Entity target, LegacyDamageSource source);
    record AllMerger(Collection<ModifyBurstDamageSourceModifierHook> modules) implements ModifyBurstDamageSourceModifierHook{

        @Override
        public @NotNull LegacyDamageSource modifyBurstSource(IToolStackView tool, ManaBurst burst, IManaBurstMixin burstExtra, @NotNull Entity owner, @NotNull Entity target, LegacyDamageSource source) {
            for (var hook:this.modules) source = hook.modifyBurstSource(tool,burst,burstExtra,owner,target,source);
            return source;
        }
    }
}
