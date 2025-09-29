package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.content.hook.TltCoreModifierHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;
import java.util.List;

public interface UpdateBurstModifierHook {
    default void updateBurst(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst){

    }

    static void handleBurstUpdate(ManaBurst burst, ItemStack stack){
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(TltCoreModifierHook.UPDATE_BURST).updateBurst(entry,toolStack.getModifierList(),burst.entity().getOwner(),burst));
    }

    record AllMerger(Collection<UpdateBurstModifierHook> modules) implements UpdateBurstModifierHook {
        @Override
        public void updateBurst(ModifierEntry modifier,List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst) {
            this.modules.forEach(hook-> hook.updateBurst(modifier,modifierList,owner,burst));
        }
    }
}
