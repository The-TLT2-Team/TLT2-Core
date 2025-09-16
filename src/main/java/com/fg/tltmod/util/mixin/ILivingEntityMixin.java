package com.fg.tltmod.util.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface ILivingEntityMixin {
    boolean tltmod$hurt(DamageSource source, float amount);
    void tltmod$actualHurt(DamageSource source, float amount);
    void tltmod$die(DamageSource source);
}
