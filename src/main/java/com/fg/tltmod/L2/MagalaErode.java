package com.fg.tltmod.L2;

import com.fuyun.cloudertinker.register.CloudertinkerEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class MagalaErode extends MobTrait {
    public MagalaErode(IntSupplier color) {
        super((color));
    }
    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living ) {
            if (living.getEffect(CloudertinkerEffects.Bloodlust.get())==null&&living.getEffect(CloudertinkerEffects.Bloodlust_erode.get())==null&&living.getEffect(CloudertinkerEffects.Bloodlust_beat.get())==null){
                living.addEffect(new MobEffectInstance(CloudertinkerEffects.Bloodlust.get(),1800,0));
            }
        }
    }
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        LivingEntity living=cache.getAttackTarget();
        if (living.getEffect(CloudertinkerEffects.Bloodlust.get())==null&&living.getEffect(CloudertinkerEffects.Bloodlust_erode.get())==null&&living.getEffect(CloudertinkerEffects.Bloodlust_beat.get())==null){
            living.addEffect(new MobEffectInstance(CloudertinkerEffects.Bloodlust.get(),1800,0));
        }
    }
    @Override
    public void tick(LivingEntity mob, int a) {
        mob.addEffect(new MobEffectInstance(CloudertinkerEffects.Bloodlust_beat.get(),30,0));
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc"
        ).withStyle(ChatFormatting.GRAY));
    }
}
