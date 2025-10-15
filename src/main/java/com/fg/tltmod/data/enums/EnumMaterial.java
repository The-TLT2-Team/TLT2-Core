package com.fg.tltmod.data.enums;

import com.fg.tltmod.data.TltCoreMaterialIds;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public enum EnumMaterial {
    MYSTERIOUS_FOX(TltCoreMaterialIds.MYSTERIOUS_FOX,8,false,false,EnumMaterialStats.MYSTERIOUS_FOX,null,EnumMaterialModifier.MYSTERIOUS_FOX_MELEE,EnumMaterialModifier.MYSTERIOUS_FOX_ARMOR),
    SEA_GLASS_SHARDS(TltCoreMaterialIds.SEA_GLASS_SHARDS,7,true,false,EnumMaterialStats.SEA_GLASS_SHARDS,null,EnumMaterialModifier.SEA_GLASS_SHARDS_MELEE,EnumMaterialModifier.SEA_GLASS_SHARDS_ARMOR,EnumMaterialModifier.SEA_GLASS_RANGED),


    ;
    public final MaterialId id;
    public final int tier;
    public final boolean craftable;
    public final boolean hidden;
    public final EnumMaterialStats stats;
    public final EnumMaterialModifier[] modifiers;
    public final ICondition condition;
    EnumMaterial(MaterialId id, int tier, boolean craftable, boolean hidden, EnumMaterialStats stats, ICondition condition, EnumMaterialModifier... modifiers){
        this.id = id;
        this.tier =tier;
        this.craftable = craftable;
        this.hidden = hidden;
        this.stats = stats;
        this.modifiers = modifiers;
        this.condition = condition;
    }
    public static ICondition modLoaded(String modId){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new ModLoadedCondition(modId));
    }
    public static ICondition tagFilled(TagKey<Item> tagKey){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS, new TagFilledCondition<>(tagKey));
    }
}
