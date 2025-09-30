package com.fg.tltmod.mixin.exbot;

import io.github.lounode.extrabotany.common.entity.gaia.GaiaIII;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GaiaIII.class, remap = false)
public abstract class GaiaIIIMixin {
    @Inject(method = "getDamageCap", at = @At("HEAD"), cancellable = true)
    private void injectDamageCap(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(Float.MAX_VALUE);
    }
}
