package com.fg.tltmod.data.enums;

import com.c2h6s.tinkers_advanced.content.item.tinkering.materialStat.FluxCoreMaterialStat;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.*;

import static slimeknights.tconstruct.tools.stats.PlatingMaterialStats.Builder;

public enum EnumMaterialStats {
    MYSTERIOUS_FOX(
            armor(234,7f,12f,9f,7f).toughness(15).knockbackResistance(1F),
            true,
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.MAILLE,
            StatlessMaterialStats.SHIELD_CORE,
            new HandleMaterialStats(1.0f,0.5f,0.8f,1.4f),
            new HeadMaterialStats(5678,8f, Tiers.NETHERITE,20f)
    ),
    SEA_GLASS_SHARDS(
            StatlessMaterialStats.BINDING,
            StatlessMaterialStats.SHIELD_CORE,
            StatlessMaterialStats.REPAIR_KIT,
            new HandleMaterialStats(-0.4f,1.7f,0.8f,1.4f),
            new HeadMaterialStats(378,10f, Tiers.NETHERITE,20f),
            new LimbMaterialStats(378,0.3f,0.4f,-0.4f),
            new GripMaterialStats(-0.4f,-0.1f,30f)
    )

    ;
    private final IMaterialStats[] stats;
    private final Builder armorStatBuilder;
    public final boolean allowShield;
    EnumMaterialStats(Builder builder,boolean allowShield ,IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder =builder;
        this.allowShield = allowShield;
    }
    EnumMaterialStats(IMaterialStats... stats) {
        this.stats = stats;
        this.armorStatBuilder =null;
        this.allowShield = false;
    }

    public IMaterialStats[] getStats() {
        return stats;
    }
    public Builder getArmorBuilder() {
        return armorStatBuilder;
    }

    public static Builder armor(int durabilityFactor,float helmet,float chestplate,float leggings,float boots){
        return PlatingMaterialStats.builder().durabilityFactor(durabilityFactor).armor(boots,leggings,chestplate,helmet);
    }
}
