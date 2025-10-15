package com.fg.tltmod.util.tinker;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class ModifierEffect {
    public static void direaddMobEffect(LivingEntity entity, MobEffect effect, int duration, int amplifier){
        var pinstance=new MobEffectInstance(effect,duration,amplifier);
        MobEffectInstance instance=entity.getActiveEffectsMap().get(pinstance.getEffect());
        if (instance==null){
            entity.getActiveEffectsMap().put(pinstance.getEffect(),pinstance);
            entity.onEffectAdded(pinstance,null);
        } else if (instance.update(pinstance)) {
            entity.onEffectUpdated(instance,false,null);
        }
    }
}
