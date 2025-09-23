package com.fg.tltmod.SomeModifiers;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.util.TltcoreConstants;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;

public class BasicRagnarokSealPrevent extends EtSTBaseModifier implements VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA);
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        volatileData.putBoolean(TltcoreConstants.NbtLocations.KEY_ANTI_RAGNAROK,true);
    }
}
