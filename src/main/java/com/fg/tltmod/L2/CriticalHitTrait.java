package com.fg.tltmod.L2;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;
import java.util.function.IntSupplier;

public class CriticalHitTrait extends MobTrait {
    public CriticalHitTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        Random random=new Random();
        if (random.nextInt(100)<10*a){
            cache.addHurtModifier(DamageModifier.multTotal(1f+a*0.5f));
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*10 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(i*50 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
