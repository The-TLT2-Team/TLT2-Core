package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.data.enums.EnumMaterial;
import com.mojang.logging.LogUtils;
import net.minecraft.data.PackOutput;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class TltCoreMaterialProvider extends AbstractMaterialDataProvider {
    public TltCoreMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        Logger logger = LogUtils.getLogger();
        for (EnumMaterial material:EnumMaterial.values()){
            //logger.info("Now generating: {}", material.id);
            addMaterial(material.id,material.tier,8, material.craftable, material.hidden, material.condition);
        }
    }

    @Override
    public String getName() {
        return "TltCore Material Data Provider";
    }
}
