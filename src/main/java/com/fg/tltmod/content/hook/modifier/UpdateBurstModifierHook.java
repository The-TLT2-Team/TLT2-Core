package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.content.hook.TltCoreModifierHook;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;

public interface UpdateBurstModifierHook {
    default void updateBurst(IToolStackView tool, ModifierEntry modifier , ManaBurst burst , ItemStack stack){

    }

    static void handleBurstUpdate(ManaBurst burst, ItemStack stack){
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(TltCoreModifierHook.UPDATE_BURST).updateBurst(toolStack,entry,burst,stack));
    }

    record AllMerger(Collection<UpdateBurstModifierHook> modules) implements UpdateBurstModifierHook {
        @Override
        public void updateBurst(IToolStackView tool, ModifierEntry modifier, ManaBurst burst, ItemStack stack) {
            this.modules.forEach(hook-> hook.updateBurst(tool,modifier,burst,stack));
        }
    }
}
