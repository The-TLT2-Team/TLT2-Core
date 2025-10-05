package com.fg.tltmod.compat.jei;

import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record CursedRingLootRecipe() implements ITraitLootRecipe {

    @Override
    public List<ItemStack> getResults() {
        var manager = ForgeRegistries.ITEMS.tags();
        if (manager == null) return List.of();
        ArrayList<ItemStack> ans = new ArrayList<>();
        for (var e : manager.getTag(LHTagGen.TRAIT_ITEM)) {
            ans.add(e.getDefaultInstance());
        }
        return ans;
    }

    @Override
    public @NotNull List<ItemStack> getCurioRequired() {
        return List.of(new ItemStack(EnigmaticItems.CURSED_RING));
    }

    @Override
    public @NotNull List<ItemStack> getInputs() {
        return List.of();
    }

    @Override
    public void addTooltip(List<Component> list) {
        list.add(Component.translatable("tooltip.cursed_ring.trait_loot").withStyle(ChatFormatting.YELLOW));
    }
}
