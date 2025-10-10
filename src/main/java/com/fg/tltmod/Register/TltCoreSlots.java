package com.fg.tltmod.Register;

import slimeknights.tconstruct.library.tools.SlotType;

public class TltCoreSlots {
    public static void init(){
        MANA.getName();
    }
    public static final SlotType MANA = SlotType.getOrCreate("tltmod_mana");
}
