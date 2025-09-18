package com.fg.tltmod;

import com.fg.tltmod.Register.TlTModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TltCore.MODID)
public class TltCore
{
    public static final String MODID = "tltmod";
    public TltCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        TlTModifiers.MODIFIERS.register(modEventBus);
    }

    public static ResourceLocation getResource(String string) {
        return new ResourceLocation(MODID, string);
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
    public static String makeDescriptionId(String type, String name) {
        return type + ".tltmod." + name;
    }
}
