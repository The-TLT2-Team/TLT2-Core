package com.fg.tltmod.compat.adAstra;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class TltCoreEnvironmentalEventHandler implements AdAstraEvents.OxygenTickEvent, AdAstraEvents.TemperatureTickEvent, AdAstraEvents.AcidRainTickEvent {
    @Override
    public boolean tick(ServerLevel serverLevel, LivingEntity livingEntity) {
        if (serverLevel.getBiome(livingEntity.blockPosition()).is(ACBiomeRegistry.PRIMORDIAL_CAVES)) return false;
        var reg = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType());
        if (reg!=null){
            var nameSpace = reg.getNamespace();
            return !nameSpace.equals("alexscaves")&&!nameSpace.equals("tltmod");
        }
        return true;
    }
    public static void init(){
        AdAstraEvents.TemperatureTickEvent.register(new TltCoreEnvironmentalEventHandler());
        AdAstraEvents.OxygenTickEvent.register(new TltCoreEnvironmentalEventHandler());
        AdAstraEvents.AcidRainTickEvent.register(new TltCoreEnvironmentalEventHandler());
    }


}
