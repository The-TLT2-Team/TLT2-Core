package com.fg.tltmod.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class Oscillation extends TltCoreEffect {
    public Oscillation() {
        super(MobEffectCategory.NEUTRAL, 0xFF8C00);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return super.isDurationEffectTick(duration, amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        super.applyEffectTick(living, amplifier);
    }
}
