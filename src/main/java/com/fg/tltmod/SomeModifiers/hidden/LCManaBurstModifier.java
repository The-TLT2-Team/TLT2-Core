package com.fg.tltmod.SomeModifiers.hidden;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.LeftClickModifierHook;
import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.content.item.DummyToolManaLens;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.fg.tltmod.util.mixin.IToolProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

//用ModifierTraitModule附加这个
public class LCManaBurstModifier extends Modifier implements LeftClickModifierHook , MeleeHitModifierHook, TooltipModifierHook {
    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.LEFT_CLICK, ModifierHooks.MELEE_HIT,ModifierHooks.TOOLTIP);
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.9&&!level.isClientSide){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExact(((ToolStack) tool).createStack(),player,burst.getMana(),true)){
                player.level().addFreshEntity(burst);
            }
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.9&&!level.isClientSide){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExact(((ToolStack) tool).createStack(),player,burst.getMana(),true)){
                player.level().addFreshEntity(burst);
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (tool.getModifierLevel(TltCoreModifiers.FAR_SIGHTS.get())<=0&&!context.isExtraAttack()&&context.isFullyCharged()&&context.getAttacker() instanceof Player player){
            var burst = getBurst(player, (ToolStack) tool);
            if (ManaItemHandler.instance().requestManaExact(((ToolStack) tool).createStack(),player,burst.getMana(),true)){
                player.level().addFreshEntity(burst);
            }
        }
        return knockback;
    }

    public static ManaBurstEntity getBurst(Player player, ToolStack toolStack){
        ManaBurstEntity burst = new ManaBurstEntity(player);
        burst.setColor(2162464);
        burst.setMana(50);
        burst.setStartingMana(50);
        burst.setMinManaLoss(40);
        burst.setManaLossPerTick(2.0F);
        burst.setGravity(0.0F);
        burst.setDeltaMovement(burst.getDeltaMovement().scale(7));
        ((IToolProvider)burst).tltmod$setTool(toolStack);
        ItemStack dummyLens = DummyToolManaLens.getDummyLens(toolStack);
        burst.setSourceLens(dummyLens);
        ModifyBurstModifierHook.handleBurstCreation(burst,dummyLens,toolStack);
        return burst;
    }


    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player!=null) {
            var burst = getBurst(player, (ToolStack) tool);
            tooltip.add(Component.translatable("tooltip.tltmod.mana_per_burst")
                    .append(""+burst.getMana()).withStyle(Style.EMPTY.withColor(0x73F5FF)));
            tooltip.add(Component.translatable("tooltip.tltmod.mana_per_entity")
                    .append(""+((IManaBurstMixin)burst).tltmod$getPerConsumption()).withStyle(Style.EMPTY.withColor(0x71D7FF)));
            tooltip.add(Component.translatable("tooltip.tltmod.mana_per_block")
                    .append(""+((IManaBurstMixin)burst).tltmod$getPerBlockConsumption()).withStyle(Style.EMPTY.withColor(0x71B2FF)));
        }
    }
}
