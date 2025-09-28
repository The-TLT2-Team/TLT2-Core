package com.fg.tltmod.data;


import com.fg.tltmod.TltCore;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class TltCoreMaterialIds {
    public static MaterialId MYSTERIOUS_FOX = createMaterial("mysterious_fox");

    private static MaterialId createMaterial(String name) {
        return new MaterialId(TltCore.getResource(name));
    }
}
