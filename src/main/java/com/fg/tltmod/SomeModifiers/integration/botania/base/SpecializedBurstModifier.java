package com.fg.tltmod.SomeModifiers.integration.botania.base;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class SpecializedBurstModifier extends EtSTBaseModifier implements ValidateModifierHook {
    @Override
    public int getPriority() {
        return -100;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VALIDATE);
    }

    @Override
    public @Nullable Component validate(IToolStackView tool, ModifierEntry modifier) {
        int i = 0;
        for (ModifierEntry entry:tool.getModifiers()){
            if (entry.getModifier() instanceof SpecializedBurstModifier){
                i++;
            }
            if (i>1) return Component.translatable("tooltip.tltmod.error.specialized_burst_modifier");
        }
        return null;
    }

    @Override
    public List<Component> getDescriptionList() {
        var list =new ArrayList<>(super.getDescriptionList());
        list.add(Component.translatable("info.tltmod.specialized_burst_modifier"));
        return list;
    }
}
