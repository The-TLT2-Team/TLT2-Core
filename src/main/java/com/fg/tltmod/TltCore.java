package com.fg.tltmod;

import com.fg.tltmod.Register.*;
import com.fg.tltmod.content.capability.CapabilitiesRegister;
import com.fg.tltmod.content.capability.compat.ars.CastToolCapability;
import com.fg.tltmod.content.capability.compat.botania.ManaCurioCapability;
import com.fg.tltmod.content.capability.compat.botania.ManaCurioCapabilityProvider;
import com.fg.tltmod.data.providers.TltCoreTraitsGLMProvider;
import com.fg.tltmod.network.TltCorePacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TltCore.MODID)
public class TltCore
{
    public static final String MODID = "tltmod";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public TltCore(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        TltCoreModifiers.MODIFIERS.register(modEventBus);
        TltCoreEntityTypes.ENTITY_TYPES.register(modEventBus);
        TltCoreItems.BASIC_ITEM.register(modEventBus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        TltCoreEffects.EFFECT.register(modEventBus);
        TltCoreEntityTickers.ENTITY_TICKERS.register(modEventBus);
        TltCoreHostilityTrait.register();
        TltCorePacketHandler.init();
        TltCoreTraitsGLMProvider.register(modEventBus);
        CapabilitiesRegister.init();

        ToolStats.register(ManaCurioCapability.MAX_STAT);

        forgeEventBus.addListener(this::commonSetup);
        forgeEventBus.addListener(this::registerRecipeSerializers);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(()->{
           TltCoreSlots.init();
        });
        //ToolCapabilityProvider.register(((stack, supplier) -> new ManaCurioCapability.Provider(supplier)));
        //ToolCapabilityProvider.register(((stack, supplier) -> new CastToolCapability.Provider(supplier)));
    }

    @SubscribeEvent
    public void registerRecipeSerializers(RegisterEvent event){
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

    public static void sealClass(Object self, String base, String solution) {
        String name = self.getClass().getName();
        if (!name.startsWith("com.fg.tltmod.")) {
            throw new IllegalStateException(base + " being extended from invalid package " + name + ". " + solution);
        }
    }
}
