package com.fg.tltmod.L2;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class DevouringLifeTrait extends MobTrait {
    public DevouringLifeTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        if (attacker.level().isClientSide()) return;
        float b = 1;
        if (cache.getLivingHurtEvent() != null) {
            b = cache.getLivingHurtEvent().getAmount()*a*0.2f;
        }
        attacker.heal(b);
    }
    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return validTarget(le) && super.allow(le, difficulty, maxModLv);
    }

    public boolean validTarget(LivingEntity le) {
        if (le instanceof EnderDragon) {
            return false;
        }
        return le.canBeAffected(new MobEffectInstance(LCEffects.CURSE.get(), 100));
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*20 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
