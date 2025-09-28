package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

public class TltCoreModifierTagProvider extends AbstractModifierTagProvider {
    public TltCoreModifierTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TltCore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }

    @Override
    public String getName() {
        return "TltCore Modifier Tag Provider.";
    }
}
