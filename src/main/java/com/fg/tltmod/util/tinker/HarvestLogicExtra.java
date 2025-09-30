package com.fg.tltmod.util.tinker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.mining.HarvestEnchantmentsModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.definition.module.aoe.AreaOfEffectIterator;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.BlockSideHitListener;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Collections;
import java.util.Objects;

import static com.ssakura49.sakuratinker.library.logic.helper.BreakLogicHelper.breakBlock;
import static com.ssakura49.sakuratinker.library.logic.helper.BreakLogicHelper.removeBlock;

public class HarvestLogicExtra {
    public static boolean handleBlockBreakNoToolDamage(ItemStack stack, BlockPos pos, Player player) {

        if (player.level().isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        ServerLevel world = serverPlayer.serverLevel();
        ToolStack tool = ToolStack.from(stack);
        BlockState state = world.getBlockState(pos);
        Direction sideHit = BlockSideHitListener.getSideHit(player);

        if (tool.isBroken()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            ToolHarvestContext context = new ToolHarvestContext(world, serverPlayer, state, pos, sideHit,
                    !player.isCreative() && state.canHarvestBlock(world, pos, player), false);
            breakBlock(tool, ItemStack.EMPTY, context);
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        } else {
            ToolHarvestContext context = new ToolHarvestContext(world, serverPlayer, state, pos, sideHit,
                    !player.isCreative() && state.canHarvestBlock(world, pos, player),
                    IsEffectiveToolHook.isEffective(tool, state));
            for (ModifierEntry entry : tool.getModifierList()) {
                entry.getHook(ModifierHooks.BLOCK_HARVEST).startHarvest(tool, entry, context);
            }
            ListTag originalEnchantments = HarvestEnchantmentsModifierHook.updateHarvestEnchantments(tool, stack, context);
            UseOnContext useContext = new UseOnContext(world, player, InteractionHand.MAIN_HAND, stack, Util.createTraceResult(pos, sideHit, false));
            Iterable<BlockPos> extraBlocks = context.isEffective() ? tool.getHook(ToolHooks.AOE_ITERATOR).getBlocks(tool, useContext, state, AreaOfEffectIterator.AOEMatchType.BREAKING) : Collections.emptyList();
            int harvested = 0;
            if (breakBlockNoToolDamage(tool, stack, context)) {
                harvested += 1;
            }
            if (harvested > 0) {
                for (BlockPos extraPos : extraBlocks) {
                    BlockState extraState = world.getBlockState(extraPos);
                    if (!extraState.isAir()) {
                        if (breakExtraBlockNoToolDamage(tool, stack, context.forPosition(extraPos.immutable(), extraState))) {
                            harvested += 1;
                        }
                    }
                }
            }
            if (originalEnchantments != null) {
                HarvestEnchantmentsModifierHook.restoreEnchantments(stack, originalEnchantments);
            }
            for (ModifierEntry entry : tool.getModifierList()) {
                entry.getHook(ModifierHooks.BLOCK_HARVEST).finishHarvest(tool, entry, context, harvested);
            }
        }

        return true;
    }
    public static boolean breakBlockNoToolDamage(ToolStack tool, ItemStack stack, ToolHarvestContext context) {
        ServerPlayer player = Objects.requireNonNull(context.getPlayer());
        ServerLevel world = context.getWorld();
        BlockPos pos = context.getPos();
        GameType type = player.gameMode.getGameModeForPlayer();
        int exp = ForgeHooks.onBlockBreakEvent(world, type, player, pos);
        if (exp == -1) {
            return false;
        } else if (player.blockActionRestricted(world, pos, type)) {
            return false;
        } else if (player.isCreative()) {
            removeBlock(tool, context);
            return true;
        } else {
            BlockState state = context.getState();
            BlockEntity te = world.getBlockEntity(pos);
            boolean removed = removeBlock(tool, context);
            Block block = state.getBlock();
            if (removed) {
                block.playerDestroy(world, player, pos, state, te, stack);
            }

            if (removed && exp > 0) {
                block.popExperience(world, pos, exp);
            }

            if (!tool.isBroken()) {
                for(ModifierEntry entry : tool.getModifierList()) {
                    entry.getHook(ModifierHooks.BLOCK_BREAK).afterBlockBreak(tool, entry, context);
                }
            }

            return true;
        }
    }
    public static boolean breakExtraBlockNoToolDamage(ToolStack tool, ItemStack stack, ToolHarvestContext context) {
        if (breakBlockNoToolDamage(tool, stack, context)) {
            Level world = context.getWorld();
            BlockPos pos = context.getPos();
            world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(context.getState()));
            TinkerNetwork.getInstance().sendVanillaPacket(Objects.requireNonNull(context.getPlayer()), new ClientboundBlockUpdatePacket(world, pos));
            return true;
        }
        return false;
    }
}
