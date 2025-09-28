package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;

public class TltCoreMaterialTagProvider extends AbstractMaterialTagProvider {
    public TltCoreMaterialTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TltCore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }

    @Override
    public String getName() {
        return "TltCore Material Tag Provider";
    }
}
