package com.fg.tltmod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TltmodCommonUtil {
    public static boolean canEnchant(Enchantment enchantment){
        var reg = ForgeRegistries.ENCHANTMENTS;
        ResourceLocation location = reg.getKey(enchantment);
        if (location==null) return false;
        return location.getNamespace().isEmpty() || location.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE);
    }
    public static @NotNull ItemStack parseItemStack(ResourceLocation name,int count){
        try {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(name),count);
        } catch (Exception ignored){}
        return ItemStack.EMPTY;
    }

    public static @NotNull Optional<Item> parseItemOptional(ResourceLocation name){
        return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(name));
    }
}
