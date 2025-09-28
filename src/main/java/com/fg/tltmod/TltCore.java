package com.fg.tltmod;

import com.fg.tltmod.Register.TltCoreEffects;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.Register.TltCoreHostilityTrait;
import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.client.gui.HungerBarRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TltCore.MODID)
public class TltCore
{
    public static final String MODID = "tltmod";
    public TltCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        TltCoreModifiers.MODIFIERS.register(modEventBus);
        TltCoreEntityTypes.ENTITY_TYPES.register(modEventBus);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        TltCoreEffects.EFFECT.register(modEventBus);
        TltCoreHostilityTrait.register();
    }

    public static ResourceLocation getResource(String string) {
        return ResourceLocation.fromNamespaceAndPath(MODID, string);
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
    public static String makeDescriptionId(String type, String name) {
        return type + ".tltmod." + name;
    }
}
