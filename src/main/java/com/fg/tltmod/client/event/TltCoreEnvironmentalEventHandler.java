package com.fg.tltmod.client.event;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TltCoreEnvironmentalEventHandler implements AdAstraEvents.OxygenTickEvent, AdAstraEvents.TemperatureTickEvent, AdAstraEvents.AcidRainTickEvent, AdAstraEvents.GravityTickEvent {
    @Override
    public boolean tick(ServerLevel serverLevel, LivingEntity livingEntity) {
        if (serverLevel.getBiome(livingEntity.blockPosition()).is(ACBiomeRegistry.PRIMORDIAL_CAVES)) return false;
        var reg = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType());
        if (reg!=null){
            var nameSpace = reg.getNamespace();
            return nameSpace.equals("alexscaves")||nameSpace.equals("tltmod");
        }
        return true;
    }
    public static void init(){
        AdAstraEvents.TemperatureTickEvent.register(new TltCoreEnvironmentalEventHandler());
        AdAstraEvents.OxygenTickEvent.register(new TltCoreEnvironmentalEventHandler());
        AdAstraEvents.AcidRainTickEvent.register(new TltCoreEnvironmentalEventHandler());
    }

    @Override
    public boolean tick(Level level, LivingEntity livingEntity, Vec3 vec3, BlockPos blockPos) {
        if (livingEntity instanceof Player) return true;
        var biome = level.getBiome(blockPos);
        return !biome.is(ACBiomeRegistry.PRIMORDIAL_CAVES) && !biome.is(ACBiomeRegistry.ABYSSAL_CHASM) &&
                !biome.is(ACBiomeRegistry.CANDY_CAVITY) && !biome.is(ACBiomeRegistry.FORLORN_HOLLOWS) &&
                !biome.is(ACBiomeRegistry.MAGNETIC_CAVES) && !biome.is(ACBiomeRegistry.TOXIC_CAVES);
    }
}
