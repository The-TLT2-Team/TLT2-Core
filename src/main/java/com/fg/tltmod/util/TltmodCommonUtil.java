package com.fg.tltmod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class TltmodCommonUtil {
    public static boolean canEnchant(Enchantment enchantment){
        var reg = ForgeRegistries.ENCHANTMENTS;
        ResourceLocation location = reg.getKey(enchantment);
        if (location==null) return false;
        return location.getNamespace().isEmpty() || location.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE);
    }
}
