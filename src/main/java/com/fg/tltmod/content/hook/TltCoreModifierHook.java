package com.fg.tltmod.content.hook;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.hook.modifier.UpdateBurstModifierHook;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

public class TltCoreModifierHook {
    public static final ModuleHook<UpdateBurstModifierHook> UPDATE_BURST = ModifierHooks.register(TltCore.getResource("update_burst"), UpdateBurstModifierHook.class, UpdateBurstModifierHook.AllMerger::new, new UpdateBurstModifierHook() {
        @Override
        public void updateBurst(IToolStackView tool, ModifierEntry modifier, ManaBurst burst, ItemStack stack) {

        }
    });
}
