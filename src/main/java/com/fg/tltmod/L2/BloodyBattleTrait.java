package com.fg.tltmod.L2;

import com.fg.tltmod.TltCore;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class BloodyBattleTrait extends MobTrait {
    public BloodyBattleTrait(IntSupplier color) {
        super((color));
    }
    public static String fight_to_end = TltCore.getResource("fight_to_end").toString();

    @Override
    public void onDeath(int a, LivingEntity entity, LivingDeathEvent event) {
        if (entity.level().isClientSide()) {
            return;
        }
        if (entity.getPersistentData().getInt(fight_to_end)==0){
            entity.setHealth(entity.getMaxHealth());
            event.setCanceled(true);
            entity.getPersistentData().putInt(fight_to_end,1);
        }
    }

    @Override
    public void tick(LivingEntity mob, int a) {
        if (!mob.level().isClientSide()&&mob.getPersistentData().getInt(fight_to_end)>=1) {
            float b = mob.getMaxHealth()*0.002f*(500-mob.getPersistentData().getInt(fight_to_end));
            if (b<=1)b=1;
            mob.setHealth(b);
            if (mob.getPersistentData().getInt(fight_to_end)<500){
                mob.getPersistentData().putInt(fight_to_end,mob.getPersistentData().getInt(fight_to_end)+1);
            }
        }
    }


    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        if (entity.getPersistentData().getInt(fight_to_end)>=1) {
            event.setAmount(event.getAmount() * 0.25f);
        }
    }
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        if (attacker.getPersistentData().getInt(fight_to_end)>=1) {
            cache.addHurtModifier(DamageModifier.multTotal(4f));
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc"
        ).withStyle(ChatFormatting.GRAY));
    }
}
