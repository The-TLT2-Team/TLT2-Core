package com.fg.tltmod.L2;

import com.fg.tltmod.Register.TltCoreEffects;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class ThudTrait extends MobTrait {
    public ThudTrait(IntSupplier color) {
        super((color));
    }
    public static String thud_cooldown = TltCore.getResource("thud_cooldown").toString();
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        LivingEntity living=cache.getAttackTarget();
        if (!living.hasEffect(TltCoreEffects.oscillation.get())&&attacker.getPersistentData().getInt(thud_cooldown) == 0){
            applyCustomKnockback(attacker, living,Math.pow(2+a,2));
            living.addEffect(new MobEffectInstance(TltCoreEffects.oscillation.get(),20*a,a-1));
            attacker.getPersistentData().putInt(thud_cooldown, 10);
        }
    }
    private static void applyCustomKnockback(LivingEntity attacker, LivingEntity target,double totalStrength) {
        double angle = attacker.getYRot() * (Math.PI / 180.0);
        double a =1 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        if (a<0)a=0;
        double knockbackX = -Math.sin(angle) * totalStrength * a;
        double knockbackZ = Math.cos(angle) * totalStrength * a;
        double knockbackY = (0.3 + (totalStrength * 0.05)) * a;

        target.setDeltaMovement(knockbackX, knockbackY, knockbackZ);
    }
    @Override
    public void tick(LivingEntity mob, int a) {
        if (!mob.level().isClientSide()&&mob.getPersistentData().getInt(thud_cooldown)>0&&mob.tickCount%20==0) {
            mob.getPersistentData().putInt(thud_cooldown,mob.getPersistentData().getInt(thud_cooldown)-1);
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
