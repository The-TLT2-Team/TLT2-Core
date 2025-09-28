package com.fg.tltmod.L2;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.IntSupplier;

public class BrokenArmor extends MobTrait {
    public BrokenArmor(IntSupplier color) {
        super((color));
    }
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        LivingEntity living=cache.getAttackTarget();
        if (living.getEffect(CloudertinkerEffects.Armorbroken.get())==null){
            living.addEffect(new MobEffectInstance(CloudertinkerEffects.Armorbroken.get(),100*a,0));
        }else{
            int timeleft = attacker.getEffect(CloudertinkerEffects.Armorbroken.get()).getDuration();
            int EffectLevel = attacker.getEffect(CloudertinkerEffects.Armorbroken.get()).getAmplifier();
            living.addEffect(new MobEffectInstance(CloudertinkerEffects.Armorbroken.get(),timeleft+(60*a),EffectLevel+1));

        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal((i*100)/20 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
