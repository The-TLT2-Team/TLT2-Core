package com.fg.tltmod.SomeModifiers.integration.sevenCurse;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.network.TltCorePacketHandler;
import com.fg.tltmod.network.packets.PClientMessagePacket;
import com.fg.tltmod.util.TltcoreConstants;
import com.ssakura49.sakuratinker.library.hooks.curio.combat.CurioKillTargetModifierHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public class TheCursedOne extends NoLevelsModifier implements ToolDamageModifierHook, ToolStatsModifierHook, VolatileDataModifierHook, ModifierRemovalHook, MeleeHitModifierHook , CurioKillTargetModifierHook, TooltipModifierHook , InventoryTickModifierHook {
    public static final ResourceLocation KEY_CURSED_PROGRESS = TltCore.getResource("curse_progress");
    public static final ResourceLocation KEY_CURSED_LEVEL = TltCore.getResource("curse_level");
    private static int BASIC_MAX_PROGRESS = 400;
    private static int POW = 2;

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_DAMAGE,ModifierHooks.TOOL_STATS,ModifierHooks.VOLATILE_DATA,ModifierHooks.REMOVE,ModifierHooks.MELEE_HIT, STHooks.CURIO_KILL_TARGET,ModifierHooks.TOOLTIP,ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (!tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) incrementProgress(tool,amount,holder);
        return amount;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        IModDataView nbt = context.getPersistentData();
        if (nbt.getInt(KEY_CURSED_LEVEL)>0){
            int level = nbt.getInt(KEY_CURSED_LEVEL);
            TltcoreConstants.LIST_GENERAL_STATS_FOR_BONUS.forEach(stat->stat.percent(builder,level*0.05f));
        }
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        IModDataView nbt = context.getPersistentData();
        if (nbt.getInt(KEY_CURSED_LEVEL)>0) {
            int level = nbt.getInt(KEY_CURSED_LEVEL);
            volatileData.addSlots(SlotType.ABILITY,level);
        }
    }


    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)&&context.isFullyCharged()) incrementProgress(tool,1+(int) (damageDealt/100),context.getAttacker());
    }

    @Override
    public void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
        incrementProgress(curio,1,attacker);
    }

    public void incrementProgress(IToolStackView tool, int amount, @Nullable LivingEntity holder){
        ModDataNBT nbt = tool.getPersistentData();
        int level = nbt.getInt(KEY_CURSED_LEVEL);
        if (level>=10) return;
        int progress = nbt.getInt(KEY_CURSED_PROGRESS);
        progress+=amount;
        int maxProgress = (int) (BASIC_MAX_PROGRESS*Math.pow(POW,level));
        boolean needRebuild = false;
        if (progress>=maxProgress){
            progress-=maxProgress;
            level+=1;
            needRebuild = true;
        }
        nbt.putInt(KEY_CURSED_PROGRESS,progress);
        nbt.putInt(KEY_CURSED_LEVEL,level);
        if (needRebuild){
            ((ToolStack)tool).rebuildStats();
            if (holder instanceof ServerPlayer serverPlayer){
                ItemStack stack = ((ToolStack)tool).createStack();
                MutableComponent itemName = stack.getDisplayName().copy();
                MutableComponent component = switch (level){
                    case 4,5,6 ->
                            itemName.append("：").append(Component.translatable("message.tltmod.upgraded_cursed_tool_2").withStyle(ChatFormatting.LIGHT_PURPLE));
                    case 7,8,9 ->
                            itemName.append("：").append(Component.translatable("message.tltmod.upgraded_cursed_tool_3").withStyle(ChatFormatting.LIGHT_PURPLE));
                    case 10->
                            itemName.append("：").append(Component.translatable("message.tltmod.upgraded_cursed_tool_4").withStyle(ChatFormatting.LIGHT_PURPLE));
                    default ->
                            itemName.append("：").append(Component.translatable("message.tltmod.upgraded_cursed_tool_1").withStyle(ChatFormatting.LIGHT_PURPLE));
                };
                TltCorePacketHandler.sendToPlayer(new PClientMessagePacket(component),serverPlayer);
            }
        }
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        return super.getDisplayName(1).copy().append(" ["+tool.getPersistentData().getInt(KEY_CURSED_LEVEL)+"]");
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        ModDataNBT nbt = tool.getPersistentData();
        nbt.remove(KEY_CURSED_LEVEL);
        nbt.remove(KEY_CURSED_PROGRESS);
        nbt.remove(TltcoreConstants.NbtLocations.KEY_IS_THE_CURSED_ONE);
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT nbt = tool.getPersistentData();
        int level = nbt.getInt(KEY_CURSED_LEVEL);
        int progress = nbt.getInt(KEY_CURSED_PROGRESS);
        int maxProgress = (int) (BASIC_MAX_PROGRESS*Math.pow(POW,level));
        if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)){
            tooltip.add(Component.translatable("tooltip.tltmod.upgrade_on_hit").withStyle(Style.EMPTY.withColor(getColor())));
        } else if (tool.getItem()== STItems.tinker_charm.asItem()){
            tooltip.add(Component.translatable("tooltip.tltmod.upgrade_on_kill").withStyle(Style.EMPTY.withColor(getColor())));
        } else {
            tooltip.add(Component.translatable("tooltip.tltmod.upgrade_on_damage").withStyle(Style.EMPTY.withColor(getColor())));
        }
        if (level<10) tooltip.add(Component.translatable("tooltip.tltmod.upgrade_progress").append(" "+progress+"/"+maxProgress).withStyle(Style.EMPTY.withColor(getColor())));
        else tooltip.add(Component.translatable("tooltip.tltmod.end_of_upgrade").withStyle(Style.EMPTY.withColor(getColor())));
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.getGameTime()%40==0&&holder instanceof Player player&&!world.isClientSide){
            if (!SuperpositionHandler.isTheCursedOne(player)){
                onRemoved(tool,modifier.getModifier());
                ((ToolStack)tool).rebuildStats();
            }
        }
    }
}
