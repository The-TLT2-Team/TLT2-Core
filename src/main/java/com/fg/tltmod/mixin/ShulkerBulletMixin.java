package com.fg.tltmod.mixin;

import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBullet.class)
public class ShulkerBulletMixin {
    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void discardAfterTooLong(CallbackInfo ci){
        var entity = (ShulkerBullet)(Object)this;
        if (entity.tickCount>200) entity.discard();
    }
}
