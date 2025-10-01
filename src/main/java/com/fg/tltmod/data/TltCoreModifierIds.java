package com.fg.tltmod.data;

import com.fg.tltmod.TltCore;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class TltCoreModifierIds {
    public static ModifierId manaRefactor = id("mana_refactor");

    public static ModifierId id(String string) {
        return new ModifierId(TltCore.getResource(string));
    }
}
