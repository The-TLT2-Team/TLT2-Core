package com.fg.tltmod.SomeModifiers;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.util.TltcoreConstants;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class TotalRagnarokDefense extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(TltcoreConstants.TinkerDataKeys.KEY_TOTAL_ANTI_RAGNAROK,false, TinkerTags.Items.MODIFIABLE));
    }
}
