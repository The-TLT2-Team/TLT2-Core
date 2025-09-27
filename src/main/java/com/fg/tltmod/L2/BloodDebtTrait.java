package com.fg.tltmod.L2;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class BloodDebtTrait extends MobTrait {
    public BloodDebtTrait(IntSupplier color) {
        super((color));
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingTick);
    }
    public static String blood_debt_value = TltCore.getResource( "blood_debt_value").toString();
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        LivingEntity living=cache.getAttackTarget();
        float b = 1;
        if (cache.getLivingHurtEvent() != null) {
            b = cache.getLivingHurtEvent().getAmount()*0.5f*(1f+a*0.4f);
        }
        living.getPersistentData().putFloat(blood_debt_value,living.getPersistentData().getFloat(blood_debt_value)+b);
        cache.addHurtModifier(DamageModifier.multTotal(0.5f));
    }
    private void OnLivingTick(LivingEvent.LivingTickEvent event) {
        var entity=event.getEntity();
        if (entity.level().isClientSide)return;
        float a = entity.getPersistentData().getFloat(blood_debt_value);
        if (a>=1){
            //entity.setHealth(entity.getHealth() - a*0.02f);
            entity.invulnerableTime=0;
            entity.hurt(LegacyDamageSource.any(entity.level().damageSources().magic()).setBypassArmor().setBypassMagic(),a*0.02f);
            entity.invulnerableTime=0;
            entity.getPersistentData().putFloat(blood_debt_value,a*0.98f);
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal((100+i*40) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
