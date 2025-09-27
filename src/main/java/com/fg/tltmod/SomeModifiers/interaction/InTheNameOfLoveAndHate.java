package com.fg.tltmod.SomeModifiers.interaction;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.Register.TltCoreKeys;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.LaserEntity;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getAttributeAttackDamage;

public class InTheNameOfLoveAndHate extends BaseModifier implements GeneralInteractionModifierHook {
    public static ResourceLocation TIME_KEY = TltCore.getResource("time_key");
    public static ResourceLocation SWITCH = TltCore.getResource("switch");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public void onKeyPress(IToolStackView tool, ModifierEntry entry, Player player, String key) {
        if (key.equals(TltCoreKeys.SWITCH_KEY_ID)) {
            ModDataNBT dataNBT = tool.getPersistentData();
            dataNBT.putBoolean(SWITCH, !dataNBT.getBoolean(SWITCH));
        }

    }
    @Override
    public String getKeyId(IToolStackView tool, ModifierEntry modifier) {
        return TltCoreKeys.SWITCH_KEY_ID;
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifierEntry, Player player, InteractionHand interactionHand, InteractionSource interactionSource) {
        ModDataNBT dataNBT = tool.getPersistentData();
        if (dataNBT.getBoolean(SWITCH)) {
            GeneralInteractionModifierHook.startUsing(tool, modifierEntry.getId(), player, interactionHand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 72000;
    }

    @Override
    public void onUsingTick(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        ModDataNBT dataNBT = tool.getPersistentData();
        int tick = dataNBT.getInt(TIME_KEY);
        dataNBT.putInt(TIME_KEY, tick + 1);
    }

    @Override
    public void onStoppedUsing(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        ModDataNBT dataNBT = tool.getPersistentData();
        Level level = entity.level();
        int time = dataNBT.getInt(TIME_KEY);
        EquipmentSlot slot = entity.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND
                : EquipmentSlot.OFFHAND;
        float damage = getAttributeAttackDamage(tool, entity, slot);
        if (!(time > 20)) return;

        LaserEntity laser = new LaserEntity(
                TltCoreEntityTypes.LASER_A.get(),
                level,
                entity,
                (ToolStack) tool,
                entity.getUsedItemHand(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                (float) ((entity.yHeadRot + 90) * Math.PI / 180),
                (float) (-entity.getXRot() * Math.PI / 180),
                time * modifier.getLevel()
        );
        laser.setRange(20*modifier.getLevel());
        laser.setDamage(damage);
        level.addFreshEntity(laser);
        dataNBT.remove(TIME_KEY);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player,
                           List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player == null) return;
        String keyName = getKeyDisplay(tool, entry);
        boolean mode = tool.getPersistentData().getBoolean(SWITCH);
        tooltip.add(Component.literal("按 [")
                .append(Component.literal(keyName).withStyle(ChatFormatting.YELLOW))
                .append("] 切换蓄力模式 (当前: " + mode + ")")
                .withStyle(ChatFormatting.GRAY));

    }

}

