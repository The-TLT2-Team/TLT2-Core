package com.fg.tltmod.SomeModifiers.misc;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.TwilightItemTier;

public class HeatBulge extends NoLevelsModifier implements InventoryTickModifierHook {

    private static final float BASELINE_TEMPERATURE = 0.75f;
    public void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }
    private static boolean needsRepair(IToolStackView tool) {
        return tool.getDamage() > 0 && !tool.isBroken();
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide() && entity instanceof ServerPlayer player && !(entity instanceof FakePlayer)) {
            if (!isCorrectSlot) return;
            if (!needsRepair(tool)) return;
            if (player.isUsingItem()||player.isBlocking()||player.gameMode.isDestroyingBlock)return;
            if(player.level().getBiome(player.blockPosition()).value().getTemperature(player.blockPosition()) - BASELINE_TEMPERATURE>0) ToolDamageUtil.repair(tool, 1);
        }
    }
}
