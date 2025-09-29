package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;
import java.util.List;

public interface BurstDamageModifierHook {
    float getBurstDamage(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst,float baseDamage,float damage);

    record AllMerger(Collection<BurstDamageModifierHook> modules) implements BurstDamageModifierHook {
        @Override
        public float getBurstDamage(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst, float baseDamage, float damage) {
            for (BurstDamageModifierHook hook:this.modules){
                damage = hook.getBurstDamage(modifier,modifierList,owner,target,burst,baseDamage,damage);
            }
            return damage;
        }
    }
}
