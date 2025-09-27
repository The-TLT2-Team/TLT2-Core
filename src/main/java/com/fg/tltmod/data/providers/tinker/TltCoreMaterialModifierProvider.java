package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.data.enums.EnumMaterial;
import com.fg.tltmod.data.enums.EnumMaterialModifier;
import com.fg.tltmod.data.enums.EnumTconMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;

public class TltCoreMaterialModifierProvider extends AbstractMaterialTraitDataProvider {
    public TltCoreMaterialModifierProvider(PackOutput packOutput) {
        super(packOutput, new TltCoreMaterialProvider(packOutput));
    }

    @Override
    protected void addMaterialTraits() {
        for (EnumMaterial material : EnumMaterial.values()){
            for (EnumMaterialModifier materialModifier:material.modifiers){
                if (materialModifier.statType==null){
                    addDefaultTraits(material.id,materialModifier.modifiers);
                }
                else addTraits(material.id,materialModifier.statType,materialModifier.modifiers);
            }
        }
        for (EnumTconMaterial material:EnumTconMaterial.values()){
            for (EnumMaterialModifier modifier:material.modifiers){
                addTraits(material.id,modifier.statType,modifier.modifiers);
            }
        }
    }

    @Override
    public String getName() {
        return "TltCore Material Modifier Provider";
    }
}
