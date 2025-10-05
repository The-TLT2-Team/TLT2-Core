package com.fg.tltmod.data.providers;

import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.data.loot.CursedRingLootModifier;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.init.loot.PlayerHasItemCondition;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TltCoreTraitsGLMProvider extends GlobalLootModifierProvider {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TltCore.MODID);
    public static final RegistryObject<Codec<CursedRingLootModifier>> LOOT_CURSED_RING = LOOT_MODIFIERS.register("loot_cursed_ring", () -> CursedRingLootModifier.CODEC);;

    public static void register(IEventBus bus) {
        LOOT_MODIFIERS.register(bus);
    }

    public TltCoreTraitsGLMProvider(PackOutput output) {
        super(output, TltCore.MODID);
    }

    @Override
    protected void start() {
        add("loot_cursed_ring", new CursedRingLootModifier(LootTableTemplate.byPlayer().build(),
                new PlayerHasItemCondition(EnigmaticItems.CURSED_RING)));
    }


}
