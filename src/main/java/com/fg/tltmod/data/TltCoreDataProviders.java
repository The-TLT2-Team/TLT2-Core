package com.fg.tltmod.data;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.data.providers.*;
import com.fg.tltmod.data.providers.l2hostility.TraitConfigProvider;
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
        boolean client = event.includeClient();
        boolean server = event.includeServer();

        generator.addProvider(client,new TltCoreBlockStateProvider(output,helper));
        generator.addProvider(client,new TltCoreItemModelProvider(output,helper));
        generator.addProvider(client,new TltCoreFluidTextureProvider(output));
        generator.addProvider(client,new TltCoreFluidTagProvider(output,lookupProvider,helper));
        generator.addProvider(client,new FluidBucketModelProvider(output, TltCore.MODID));
        TltCoreBlockTagProvider blockTags = new TltCoreBlockTagProvider(output, lookupProvider, helper);
        generator.addProvider(client,blockTags);
        generator.addProvider(server,new TltCoreItemTagProvider(output,lookupProvider,blockTags.contentsGetter(),helper));
        generator.addProvider(client,new TltCoreMaterialProvider(output));
        generator.addProvider(client,new TltCoreMaterialStatProvider(output));
        generator.addProvider(client,new TltCoreMaterialModifierProvider(output));
        generator.addProvider(client,new TltCoreFluidEffectProvider(output));
        generator.addProvider(client,new TltCoreMaterialTagProvider(output,helper));
        generator.addProvider(client,new TltCoreModifierTagProvider(output,helper));
        generator.addProvider(client,new TltCoreMaterialRenderInfoProvider(output,new TltCoreMaterialSpriteProvider(),helper));
        generator.addProvider(client,new TltCoreModifierProvider(output));
        generator.addProvider(server, new TraitConfigProvider(generator));
        generator.addProvider(server, new TltCoreModifierRecipeProvider(output));
        generator.addProvider(server, new TltCoreTraitsGLMProvider(output));
        generator.addProvider(server, new TltCoreEntityTagProvider(output,lookupProvider));

        //正常runData时不用运行这个，这个是用来生成A洞群系的地表规则的。
        //生成后的文件是手动复制result内的内容放进其它维度的噪声生成器的地表规则内，用后需删除。
        //generator.addProvider(server,new TltCoreNoiseSettingProvider(output));
    }
}
