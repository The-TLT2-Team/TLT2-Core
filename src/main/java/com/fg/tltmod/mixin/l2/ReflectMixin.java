package com.fg.tltmod.mixin.l2;

import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ReflectTrait.class ,remap = false)
public class ReflectMixin {
    @Inject(method = "onHurtByOthers",cancellable = true,at = @At(value = "INVOKE", target = "Ldev/xkmc/l2library/init/events/GeneralEventHandler;schedule(Ljava/lang/Runnable;)V"),locals = LocalCapture.CAPTURE_FAILHARD)
    public void changeSource(int level, LivingEntity entity, LivingHurtEvent event, CallbackInfo ci, LivingEntity le, float factor){
        GeneralEventHandler.schedule(() -> le.hurt(entity.level().damageSources().magic(),Math.min (event.getAmount() * factor,entity.getMaxHealth()/10f)));
        ci.cancel();
    }
}
