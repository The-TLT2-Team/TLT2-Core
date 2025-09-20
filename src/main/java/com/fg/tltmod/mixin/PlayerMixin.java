package com.fg.tltmod.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    //取消了PvP，包里词条的情况如果允许PvP可能会崩100万回，这个样子能省很多事。
    @Inject(at = @At("HEAD"),method = "hurt",cancellable = true)
    public void cancelPvp(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir){
        if (pSource.getEntity() instanceof Player) cir.setReturnValue(false);
    }
}
