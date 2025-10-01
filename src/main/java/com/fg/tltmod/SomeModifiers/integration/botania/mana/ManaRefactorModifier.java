package com.fg.tltmod.SomeModifiers.integration.botania.mana;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;

public class ManaRefactorModifier extends BasicManaModifier{
    @Override
    public int getCapacity(ModifierEntry modifier) {
        return 20000000*modifier.getLevel();
    }
}
