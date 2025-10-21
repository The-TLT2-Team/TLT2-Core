package com.fg.tltmod.util.tinker;

import com.fg.tltmod.TltCore;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import java.util.HashMap;
import java.util.Map;

public class ModifierModuleUtil {
    public static Map<MobTrait, TinkerDataCapability.TinkerDataKey<Integer>> TRAIT_IMMUNE_MAP = new HashMap<>();

    public static ArmorLevelModule getTraitImmuneModule(MobTrait trait, boolean allowBroken, TagKey<Item> heldTag){
        TinkerDataCapability.TinkerDataKey<Integer> key =TinkerDataCapability.TinkerDataKey.of(trait.getRegistryName());
        TRAIT_IMMUNE_MAP.put(trait,key);
        TltCore.LOGGER.info("Added Trait-Immune TinkerDataKey: {}", key);
        return new ArmorLevelModule(key,allowBroken,heldTag);
    }
    public static ArmorLevelModule getTraitImmuneModule(MobTrait trait, TagKey<Item> heldTag){
        return getTraitImmuneModule(trait,false,heldTag);
    }
    public static ArmorLevelModule getTraitImmuneModule(MobTrait trait){
        return getTraitImmuneModule(trait,false,null);
    }
    public static boolean getTraitImmunity(MobTrait trait, Entity entity){
        if (entity==null) return false;
        TinkerDataCapability.TinkerDataKey<Integer> key =TRAIT_IMMUNE_MAP.getOrDefault(trait,null);
        if (key!=null) {
            var optional = entity.getCapability(TinkerDataCapability.CAPABILITY);
            return ArmorLevelModule.getLevel(optional, key) > 0;
        }
        return false;
    }
}
