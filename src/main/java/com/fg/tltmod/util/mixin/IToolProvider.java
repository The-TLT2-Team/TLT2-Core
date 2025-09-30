package com.fg.tltmod.util.mixin;

import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface IToolProvider {
    IToolStackView tltmod$getTool();
    void tltmod$setTool(IToolStackView tool);
}
