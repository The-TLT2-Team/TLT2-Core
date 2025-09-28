package com.fg.tltmod.data.enums;

import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

public enum EnumTconExtraStat {

    ;
    public final IMaterialStats[] stats;

    EnumTconExtraStat(IMaterialStats... stats) {
        this.stats = stats;
    }
}
