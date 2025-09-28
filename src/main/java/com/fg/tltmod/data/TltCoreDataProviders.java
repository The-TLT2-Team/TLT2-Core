package com.fg.tltmod.data;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.data.providers.*;
import com.fg.tltmod.data.providers.tinker.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TltCore.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class TltCoreDataProviders {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator=event.getGenerator();
        PackOutput output=generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider=event.getLookupProvider();
        ExistingFileHelper helper=event.getExistingFileHelper();

        generator.addProvider(event.includeClient(),new TltCoreBlockStateProvider(output,helper));
        generator.addProvider(event.includeClient(),new TltCoreItemModelProvider(output,helper));
        generator.addProvider(event.includeClient(),new TltCoreFluidTextureProvider(output));
        generator.addProvider(event.includeClient(),new TltCoreFluidTagProvider(output,lookupProvider,helper));
        generator.addProvider(event.includeClient(),new FluidBucketModelProvider(output, TltCore.MODID));
        TltCoreBlockTagProvider blockTags = new TltCoreBlockTagProvider(output, lookupProvider, helper);
        generator.addProvider(event.includeClient(),blockTags);
        generator.addProvider(event.includeServer(),new TltCoreItemTagProvider(output,lookupProvider,blockTags.contentsGetter(),helper));
        generator.addProvider(event.includeClient(),new TltCoreMaterialProvider(output));
        generator.addProvider(event.includeClient(),new TltCoreMaterialStatProvider(output));
        generator.addProvider(event.includeClient(),new TltCoreMaterialModifierProvider(output));
        generator.addProvider(event.includeClient(),new TltCoreFluidEffectProvider(output));
        generator.addProvider(event.includeClient(),new TltCoreMaterialTagProvider(output,helper));
        generator.addProvider(event.includeClient(),new TltCoreModifierTagProvider(output,helper));
        generator.addProvider(event.includeClient(),new TltCoreMaterialRenderInfoProvider(output,new TltCoreMaterialSpriteProvider(),helper));
        generator.addProvider(event.includeServer(),new TltCoreModifierProvider(output));
    }
}
