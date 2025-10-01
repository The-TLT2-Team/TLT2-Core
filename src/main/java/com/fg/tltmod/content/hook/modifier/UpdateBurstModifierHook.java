package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IToolProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.lens.LensItem;

import java.util.Collection;
import java.util.List;

public interface UpdateBurstModifierHook {
    default void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst){

    }

    static void handleBurstUpdate(ManaBurst burst, ItemStack stack){
        IToolStackView actualTool = ((IToolProvider)burst).tltmod$getTool();
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(TltCoreModifierHook.UPDATE_BURST).updateBurst(actualTool,entry,toolStack.getModifierList(),burst.entity().getOwner(),burst));
        LensProviderModifierHook.gatherLens(toolStack,burst).forEach(lensStack ->{
            if (lensStack.getItem() instanceof LensItem lens) lens.updateBurst(burst,stack);
        });
    }

    record AllMerger(Collection<UpdateBurstModifierHook> modules) implements UpdateBurstModifierHook {
        @Override
        public void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier,List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst) {
            this.modules.forEach(hook-> hook.updateBurst(tool,modifier,modifierList,owner,burst));
        }
    }
}
