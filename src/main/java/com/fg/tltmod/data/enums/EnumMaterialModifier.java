package com.fg.tltmod.data.enums;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fuyun.cloudertinker.register.CloudertinkerModifiers;
import com.ssakura49.sakuratinker.register.STModifiers;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public enum EnumMaterialModifier {
    MYSTERIOUS_FOX_MELEE(MaterialRegistry.MELEE_HARVEST, entry(TltCoreModifiers.LOVE_AND_HATE_STATIC.getId()),entry(TltCoreModifiers.BLESSING_FROM_FOX_GOD.getId()),entry(TltCoreModifiers.POWER_OF_PRAYER.getId(),3)),
    MYSTERIOUS_FOX_ARMOR(MaterialRegistry.ARMOR, entry(STModifiers.Absorption.getId()),entry(STModifiers.Celestial.getId()),entry(TltCoreModifiers.BLESSING_FROM_FOX_GOD.getId()),entry(TltCoreModifiers.POWER_OF_PRAYER.getId(),3)),

    SEA_GLASS_SHARDS_MELEE(MaterialRegistry.MELEE_HARVEST, entry(TltCoreModifiers.VIBRIO_VULNIFICUS.getId())),
    SEA_GLASS_SHARDS_ARMOR(MaterialRegistry.ARMOR, entry(TltCoreModifiers.VIBRIO_VULNIFICUS.getId())),
    SEA_GLASS_RANGED(MaterialRegistry.RANGED,entry(TltCoreModifiers.VIBRIO_VULNIFICUS.getId()))

    ;
    public final ModifierEntry[] modifiers;
    public final MaterialStatsId statType;
    EnumMaterialModifier(MaterialStatsId statType, ModifierEntry... modifiers){
        this.modifiers = modifiers;
        this.statType = statType;
    }
    public static ModifierEntry entry(ModifierId id,int level){
        return new ModifierEntry(id,level);
    }
    public static ModifierEntry entry(ModifierId id){
        return new ModifierEntry(id,1);
    }
}
