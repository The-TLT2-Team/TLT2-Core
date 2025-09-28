package com.fg.tltmod.data.enums;

import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;


public enum EnumModifier {


    ;
    public final ModifierId id;
    public final ModifierRecipeBuilder builder;
    public final BasicModifier.TooltipDisplay tooltipDisplay;
    public final ModifierModule[] modules;
    public final ICondition condition;
    public final ModifierLevelDisplay display;

    EnumModifier(ModifierId id, ModifierRecipeBuilder builder, @Nullable ICondition condition, BasicModifier.TooltipDisplay tooltipDisplay, ModifierLevelDisplay display, ModifierModule... modules) {
        this.id = id;
        this.builder = builder;
        this.tooltipDisplay = tooltipDisplay;
        this.modules = modules;
        this.condition = condition;
        this.display = display;
    }
}
