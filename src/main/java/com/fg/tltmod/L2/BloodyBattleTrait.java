package com.fg.tltmod.L2;

import com.fg.tltmod.util.TltmodHurtProcess;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.network.TraitEffectToClient;
import dev.xkmc.l2hostility.init.network.TraitEffects;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

public class BloodyBattleTrait extends MobTrait {
    public BloodyBattleTrait(IntSupplier color) {
        super((color));
    }
    public static String fight_to_end = "fight_to_end";

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
