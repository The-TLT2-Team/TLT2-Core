package com.fg.tltmod.data.providers.l2hostility;

import com.fg.tltmod.Register.TltCoreHostilityTrait;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;

public class TraitConfigProvider extends ConfigDataProvider {
    public TraitConfigProvider(DataGenerator generator) {
        super(generator, TltCore.MODID+"Trait Config Provider");
    }

    @Override
    public void add(Collector collector) {
        TltTraitConfigRegistry.getEntries().forEach((id, config) -> {
            collector.add(L2Hostility.TRAIT, id, config);
        });
    }
}
