package com.fg.tltmod.data.providers.l2hostility;

import dev.xkmc.l2hostility.content.config.TraitConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TltTraitConfigRegistry {
    private static final Map<ResourceLocation, TraitConfig> ENTRIES = new HashMap<>();

    public static void register(ResourceLocation id, TraitConfig config) {
        ENTRIES.put(id, config);
    }

    public static Map<ResourceLocation, TraitConfig> getEntries() {
        return ENTRIES;
    }
}
