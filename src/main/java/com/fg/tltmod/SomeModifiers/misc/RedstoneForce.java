package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.tool.modifiers.Common.EnergyLoaded;
import com.c2h6s.etstlib.tool.modifiers.base.BasicFEModifier;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.ToolEnergyUtil;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.SlotStackModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RedstoneForce extends EnergyLoaded implements SlotStackModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.SLOT_STACK);
    }

    @Override
    public int getCapacity(ModifierEntry modifierEntry) {
        return 200000*modifierEntry.getLevel();
    }

    @Override
    public boolean overrideOtherStackedOnMe(IToolStackView slotTool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        if (!held.is(Items.REDSTONE)&&!held.is(Items.REDSTONE_BLOCK)) return false;
        int toInsert = held.is(Items.REDSTONE)?10000:100000;
        if (!player.level().isClientSide){
            if (ToolEnergyUtil.receiveEnergy(slotTool,toInsert,true)>=toInsert){
                ToolEnergyUtil.receiveEnergy(slotTool,toInsert,false);
                held.shrink(1);
            }
        }
        return true;
    }
}
