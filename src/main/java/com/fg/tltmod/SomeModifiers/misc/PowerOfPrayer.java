package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.tinkers_advanced.TinkersAdvanced;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.Set;

public class PowerOfPrayer extends BaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.getAllStats().forEach(iToolStat -> {
            if (iToolStat instanceof INumericToolStat<?> toolStat
                    && !BLACKLIST.contains(iToolStat.getName())) {
                toolStat.percent(builder, 0.1 * modifier.getLevel());
            }
        });
    }

    private static final Set<ResourceLocation> BLACKLIST = Set.of(
            STToolStats.COOLDOWN.getName(),
            STToolStats.PENETRATION.getName(),
            STToolStats.TIME.getName(),
            STToolStats.MAX_COLLECTED.getName()
    );
}
