package com.fg.tltmod.data.enums;

import slimeknights.tconstruct.library.materials.definition.MaterialId;

public enum EnumTconMaterial {

    ;
    public final EnumTconExtraStat stats;
    public final EnumMaterialModifier[] modifiers;
    public final MaterialId id;

    EnumTconMaterial(EnumTconExtraStat stats, MaterialId id, EnumMaterialModifier... modifiers) {
        this.stats = stats;
        this.modifiers = modifiers;
        this.id = id;
    }
}
