package com.fg.tltmod.mixin.adAstra;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import earth.terrarium.adastra.common.handlers.PlanetHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlanetHandler.class,remap = false)
public class PlanetHandlerMixin {
    @Inject(at = @At("RETURN"),method = "getTemperature",cancellable = true)
    private static void modifyTemperatureForBiomes(ServerLevel level, BlockPos pos, CallbackInfoReturnable<Short> cir){
        if (level.getBiome(pos).is(ACBiomeRegistry.PRIMORDIAL_CAVES)) cir.setReturnValue((short) 10);
    }
}
