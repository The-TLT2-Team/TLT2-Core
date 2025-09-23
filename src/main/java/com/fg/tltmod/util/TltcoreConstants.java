package com.fg.tltmod.util;

import com.fg.tltmod.TltCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class TltcoreConstants {
    public static class NbtLocations{
        //使单个工具不被封印，以VolatileData的形式附加在匠魂工具上便能生效，这样无需遍历全词条。
        public static final ResourceLocation KEY_ANTI_RAGNAROK = TltCore.getResource("anti_ragnarok");
    }
    public static class TinkerDataKeys{
        //实体带有这个data后全身物品都不会被封印，用ArmorLevelingModule来添加。
        public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_TOTAL_ANTI_RAGNAROK = TinkerDataCapability.TinkerDataKey.of(TltCore.getResource("total_anti_ragnarok"));
    }
    public static class TagKeys {
    }
}
