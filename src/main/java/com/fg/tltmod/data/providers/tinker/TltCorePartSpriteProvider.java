package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.TltCore;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

public class TltCorePartSpriteProvider extends AbstractPartSpriteProvider {
    public TltCorePartSpriteProvider() {
        super(TltCore.MODID);
    }

    @Override
    public String getName() {
        return "TltCore Part Sprite Provider";
    }

    @Override
    protected void addAllSpites() {
    }
}
