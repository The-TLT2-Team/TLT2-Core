package com.fg.tltmod.SomeModifiers.integration.botania.base;

import com.fg.tltmod.Register.TltCoreSlots;
import slimeknights.tconstruct.library.json.predicate.tool.ToolContextPredicate;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class AddManaSlotModifier extends Modifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(ModifierSlotModule.slot(TltCoreSlots.MANA).toolContext(ToolContextPredicate.ANY).amount(0,1));
    }
}
