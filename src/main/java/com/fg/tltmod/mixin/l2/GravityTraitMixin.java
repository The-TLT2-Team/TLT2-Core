package com.fg.tltmod.mixin.l2;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.GravityTrait;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = GravityTrait.class,remap = false)
public abstract class GravityTraitMixin extends AuraEffectTrait {
    public GravityTraitMixin(Supplier<MobEffect> eff) {
        super(eff);
    }
    //L2作者喝高了把重力改这么恶心
    @Inject(method = "onDamaged",at = @At("HEAD"),cancellable = true)
    public void cancelPushDown(int level, LivingEntity mob, AttackCache cache, CallbackInfo ci){
        ci.cancel();
    }

}
