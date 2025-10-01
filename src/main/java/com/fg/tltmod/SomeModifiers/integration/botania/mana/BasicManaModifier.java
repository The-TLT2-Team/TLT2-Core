package com.fg.tltmod.SomeModifiers.integration.botania.mana;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.fg.tltmod.content.capability.ManaCurioCapability;
import com.fg.tltmod.util.MathUtil;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.utils.component.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import javax.annotation.Nullable;

import java.util.List;

import static com.fg.tltmod.content.capability.ManaCurioCapability.*;

public abstract class BasicManaModifier extends BaseModifier implements ModifierRemovalHook, TooltipModifierHook, ToolStatsModifierHook, CustomBarDisplayModifierHook, ValidateModifierHook {
    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP,ModifierHooks.TOOL_STATS, EtSTLibHooks.CUSTOM_BAR);
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifierEntry) {
        checkMana(tool);
        return null;
    }

    @Nullable
    @Override
    public Component onRemoved(@NotNull IToolStackView tool, @NotNull Modifier modifier) {
        if (getToolMaxMana(tool) <= 0) {
            tool.getPersistentData().remove(MANA_KEY);
        }
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        Component component = DynamicComponentUtil.ScrollColorfulText.getColorfulText("tooltip.tltmod.mana_storage",":"+" "+ MathUtil.Mana.getManaString(getToolMana(tool))+"/"+ MathUtil.Mana.getManaString(getToolMaxMana(tool)),new int[]{0xFF0000,0xFF5A00},40,100,true);
        if (!list.contains(component)) list.add(component);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        ManaCurioCapability.MAX_STAT.add(modifierStatsBuilder,getCapacity(modifierEntry));
    }

    public abstract int getCapacity(ModifierEntry modifier);

    @Override
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return getToolMana(tool)>0;
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return 0xFF00fbff;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int mana = getToolMana(tool);
        int maxStorage = getToolMaxMana(tool);
        if (maxStorage>0) {
            return new Vec2(Math.min(13, 13F * mana / maxStorage), 1);
        }
        return new Vec2(0,0);
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "tltmod:mana_bar";
    }
}
