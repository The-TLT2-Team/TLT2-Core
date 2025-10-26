package com.fg.tltmod.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class VenomEffect extends NoMilkEffect {
    public VenomEffect() {
        super(MobEffectCategory.HARMFUL, 0x9932ff, true);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getHealth() > 1.0F) {
            entity.hurt(entity.damageSources().magic(), 2.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = Math.max(5, 25 >> amplifier);
        return duration % interval == 0;
    }
}
