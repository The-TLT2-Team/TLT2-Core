package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.data.enums.EnumMaterial;
import com.fg.tltmod.data.enums.EnumTconMaterial;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;

public class TltCoreMaterialStatProvider extends AbstractMaterialStatsDataProvider {
    public TltCoreMaterialStatProvider(PackOutput packOutput) {
        super(packOutput, new TltCoreMaterialProvider(packOutput));
    }

    @Override
    protected void addMaterialStats() {
        for (EnumMaterial material:EnumMaterial.values()){
            if (material.stats.getArmorBuilder()!=null){
                addArmorStats(material.id, material.stats.getArmorBuilder(),material.stats.getStats());
                if (material.stats.allowShield){
                    addMaterialStats(material.id, material.stats.getArmorBuilder().buildShield());
                }
            }
            else addMaterialStats(material.id, material.stats.getStats());
        }
        for (EnumTconMaterial material:EnumTconMaterial.values()){
            addMaterialStats(material.id,material.stats.stats);
        }
    }

    @Override
    public String getName() {
        return "TLT Tech Material Stats Data Provider";
    }
}
