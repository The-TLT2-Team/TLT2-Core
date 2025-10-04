package com.fg.tltmod.L2;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;
import java.util.function.IntSupplier;

public class FuriousTrait extends MobTrait {
    public FuriousTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        float b = (float) (0.1f*Math.pow(2,a-1));
        event.setAmount(event.getAmount()*(1f+b));
    }
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        float b = (float) (0.1f*Math.pow(2,a));
        cache.addHurtModifier(DamageModifier.multTotal(b));
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(((float) (0.1f*Math.pow(2,i-1))*100) + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(((float) (0.1f*Math.pow(2,i))*100) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        if (le.getMaxHealth()>100*(1+LHConfig.COMMON.healthFactor.get()*difficulty)) return false;
        return super.allow(le,difficulty,maxModLv);
    }
}
