package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.content.hook.TltCoreModifierHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;
import java.util.List;

public interface ModifyBurstModifierHook {
    default void modifyBurst(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, ToolStack dummyLens){}

    static void handleBurstCreation(ManaBurst burst, ItemStack stack){
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(TltCoreModifierHook.MODIFY_BURST).modifyBurst(entry,toolStack.getModifierList(),burst.entity().getOwner(),burst,toolStack));
    }

    record AllMerger(Collection<ModifyBurstModifierHook> modules) implements ModifyBurstModifierHook {
        @Override
        public void modifyBurst(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, ToolStack dummyLens) {
            this.modules.forEach(hook->hook.modifyBurst(modifier,modifierList,owner,burst,dummyLens));
        }
    }
}
