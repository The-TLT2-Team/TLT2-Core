package com.fg.tltmod.SomeModifiers.integration.mekanism;

import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration.ToolBasicChemicalTankProvider;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public class ChemicalStorageModifier extends Modifier implements ToolStatsModifierHook , TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS,ModifierHooks.TOOLTIP);
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        EtSTLibToolStat.CHEMICAL_TANK_CAPACITY.add(builder,100000*modifier.getLevel());
        EtSTLibToolStat.CHEMICAL_TANK_COUNT.add(builder,1);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        long max = tool.getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        for (int i=0;i<tool.getStats().getInt(EtSTLibToolStat.CHEMICAL_TANK_COUNT);i++){
            var nbt = tool.getPersistentData().getCompound(ToolBasicChemicalTankProvider.getKeyForTank(i));
            GasStack gas = GasStack.readFromNBT(nbt);
            if (!gas.isEmpty()){
                tooltip.add(Component.translatable("tooltip.tltmod.chemical_gas").append(Component.translatable(gas.getTranslationKey())).append(" "+gas.getAmount()+"/"+max+"mB").withStyle(s->s.withColor(0x00FFB2)));
                continue;
            }
            InfusionStack infuse = InfusionStack.readFromNBT(nbt);
            if (!infuse.isEmpty()){
                tooltip.add(Component.translatable("tooltip.tltmod.chemical_infusion").append(Component.translatable(infuse.getTranslationKey())).append(" "+infuse.getAmount()+"/"+max+"mB").withStyle(s->s.withColor(0x00FFB2)));
                continue;
            }
            SlurryStack slurry = SlurryStack.readFromNBT(nbt);
            if (!slurry.isEmpty()){
                tooltip.add(Component.translatable("tooltip.tltmod.chemical_slurry").append(Component.translatable(slurry.getTranslationKey())).append(" "+slurry.getAmount()+"/"+max+"mB").withStyle(s->s.withColor(0x00FFB2)));
                continue;
            }
            PigmentStack pigment = PigmentStack.readFromNBT(nbt);
            if (!pigment.isEmpty()){
                tooltip.add(Component.translatable("tooltip.tltmod.chemical_pigment").append(Component.translatable(pigment.getTranslationKey())).append(" "+pigment.getAmount()+"/"+max+"mB").withStyle(s->s.withColor(0x00FFB2)));
            }
        }
    }
}
