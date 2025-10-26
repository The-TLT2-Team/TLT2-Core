package com.fg.tltmod.SomeModifiers.integration.arsnouveau;

import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CasterToolModifier extends BaseModifier {

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide&&(isCorrectSlot||isSelected)&&holder instanceof Player player){
            RepairingPerk.attemptRepair(stack, player);
        }
    }
}
