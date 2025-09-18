package com.fg.tltmod.mixin.l2;

import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EffectUtil.class,remap = false)
public class EffectUtilMixin {
    @Inject(method = "forceAddEffect",at = @At("HEAD"), cancellable = true)
    private static void stopForcePlayer(LivingEntity e, MobEffectInstance ins, Entity source, CallbackInfo ci){
        if (e instanceof Player) {
            e.forceAddEffect(ins, source);
            ci.cancel();
        }
    }
}
