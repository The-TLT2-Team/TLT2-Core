package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.data.tinkering.AbstractFluidEffectProvider;

public class TltCoreFluidEffectProvider extends AbstractFluidEffectProvider {
    public TltCoreFluidEffectProvider(PackOutput packOutput) {
        super(packOutput, TltCore.MODID);
    }

    @Override
    protected void addFluids() {

    }
    public static ICondition modLoaded(String modId){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new ModLoadedCondition(modId));
    }
    public static ICondition tagFilled(TagKey<Item> tagKey){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new TagFilledCondition<>(tagKey));
    }

    @Override
    public String getName() {
        return "TltCore Fluid Effect Provider.";
    }
}
