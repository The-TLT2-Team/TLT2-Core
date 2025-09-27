package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.data.enums.EnumModifier;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;

import java.util.Arrays;

public class TltCoreModifierProvider extends AbstractModifierProvider {
    public TltCoreModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        Arrays.stream(EnumModifier.values()).toList().forEach((enumModifier -> {
            buildModifier(enumModifier.id,enumModifier.condition).addModules(enumModifier.modules).tooltipDisplay(enumModifier.tooltipDisplay);
        }));
    }

    @Override
    public String getName() {
        return "TltCore Modifier Provider";
    }
}
