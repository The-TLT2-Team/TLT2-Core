package com.fg.tltmod.compat.adAstra;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import earth.terrarium.adastra.api.events.AdAstraEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TltCoreGravityEventHandler implements AdAstraEvents.GravityTickEvent, AdAstraEvents.EntityGravityEvent {
    @Override
    public float getGravity(Entity entity, float v) {
        var reg = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (reg!=null){
            var nameSpace = reg.getNamespace();
            if (nameSpace.equals("alexscaves")||nameSpace.equals("tltmod")) return 1;
        }
        if (entity.getCapability(MobTraitCap.CAPABILITY).isPresent()){
            var cap = entity.getCapability(MobTraitCap.CAPABILITY).orElse(null);
            if (cap.hasTrait(LHTraits.GRAVITY.get())||cap.hasTrait(LHTraits.MOONWALK.get())) return 1;
        }
        return v;
    }

    @Override
    public boolean tick(Level level, LivingEntity livingEntity, Vec3 vec3, BlockPos blockPos) {
        if (livingEntity instanceof Player) return true;
        var biome = level.getBiome(blockPos);
        return !biome.is(ACBiomeRegistry.PRIMORDIAL_CAVES) && !biome.is(ACBiomeRegistry.ABYSSAL_CHASM) &&
                !biome.is(ACBiomeRegistry.CANDY_CAVITY) && !biome.is(ACBiomeRegistry.FORLORN_HOLLOWS) &&
                !biome.is(ACBiomeRegistry.MAGNETIC_CAVES) && !biome.is(ACBiomeRegistry.TOXIC_CAVES);
    }

    public static void init(){
        AdAstraEvents.GravityTickEvent.register(new TltCoreGravityEventHandler());
        AdAstraEvents.EntityGravityEvent.register(new TltCoreGravityEventHandler());
    }
}
