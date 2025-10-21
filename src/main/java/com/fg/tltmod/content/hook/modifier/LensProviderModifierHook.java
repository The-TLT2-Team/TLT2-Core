package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.api.tool.IBotLensProvider;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.lens.Lens;
import vazkii.botania.common.item.lens.LensItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface LensProviderModifierHook {
    default List<ItemStack> getLens(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, ManaBurst burst, IManaBurstMixin burstExtra){
        return List.of();
    }
    static List<ItemStack> gatherLens(IToolStackView tool,ManaBurst burst){
        List<ItemStack> list = new ArrayList<>();
        tool.getModifierList().forEach(entry -> {
            if (entry.getModifier() instanceof IBotLensProvider provider) list.addAll(provider.getLens());
            else list.addAll(entry.getHook(TltCoreModifierHook.LENS_PROVIDER).getLens(tool,entry,tool.getModifierList(),burst,(IManaBurstMixin) burst));
        });
        return list;
    }
    record AllMerger(Collection<LensProviderModifierHook> modules) implements LensProviderModifierHook {
        @Override
        public List<ItemStack> getLens(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, ManaBurst burst,IManaBurstMixin burstExtra) {
            List<ItemStack> list = new ArrayList<>();
            for (var entry:this.modules){
                list.addAll(entry.getLens(tool,modifier,modifierList,burst,(IManaBurstMixin) burst));
            }
            return list;
        }
    }
}
