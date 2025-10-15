package com.fg.tltmod.effects;

import com.fg.tltmod.TltCore;
import com.fuyun.cloudertinker.Cloudertinker;
import com.fuyun.cloudertinker.register.CloudertinkerEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Objects;

public class VibrioVulnificus extends TltCoreEffect{
    public static final ResourceLocation vibrio_vulnificus = TltCore.getResource("vibrio_vulnificus");
    public VibrioVulnificus() {
        super(MobEffectCategory.HARMFUL, 0xFF8C00);
        MinecraftForge.EVENT_BUS.addListener(this::LivingHurtEvent);
    }

    public void LivingHurtEvent(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance =entity.getEffect(this);
        if (instance != null)event.setAmount(event.getAmount()*1.25f);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        ModDataNBT entitydata = ModDataNBT.readFromNBT(living.getPersistentData());
        MobEffectInstance effectInstance = living.getEffect(this);
        if (effectInstance != null && effectInstance.getDuration() <= 1) {
            entitydata.putInt(vibrio_vulnificus,8);
        }
        if(living.tickCount%20==0){
            if (entitydata.getInt(vibrio_vulnificus)<=1)entitydata.putInt(vibrio_vulnificus,1);
            living.invulnerableTime = 0;
            Objects.requireNonNull(living).hurt(living.damageSources().wither(), entitydata.getInt(vibrio_vulnificus));
            living.invulnerableTime = 0;
            if (entitydata.getInt(vibrio_vulnificus)<=living.getMaxHealth()*0.1f){
                entitydata.putInt(vibrio_vulnificus, entitydata.getInt(vibrio_vulnificus)*2);
                if (entitydata.getInt(vibrio_vulnificus)>=living.getMaxHealth()*0.1f){
                    entitydata.putInt(vibrio_vulnificus, (int) (living.getMaxHealth()*0.1f));
                }
            }
        }
    }
}
