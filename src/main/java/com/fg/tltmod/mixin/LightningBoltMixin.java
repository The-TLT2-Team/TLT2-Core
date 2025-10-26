package com.fg.tltmod.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {
    @Shadow @Nullable public abstract ServerPlayer getCause();

    @Inject(method = "spawnFire",at = @At("HEAD"),cancellable = true)
    public void cancelFireWhenOwned(int pExtraIgnitions, CallbackInfo ci){
        if (getCause()!=null) ci.cancel();
    }
}
