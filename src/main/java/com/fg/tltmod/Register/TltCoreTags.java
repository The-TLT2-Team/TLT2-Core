package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TltCoreTags {
    public static void init() {
        Items.init();
    }
    public static class Items {
        private static void init() {
        }
        public static final TagKey<Item> FOOD_ENTITY_BLACKLIST = local("food_entity_blacklist");

    }
    private static TagKey<Item> local(String name) {
        return TagKey.create(Registries.ITEM, TltCore.getResource(name));
    }
}
