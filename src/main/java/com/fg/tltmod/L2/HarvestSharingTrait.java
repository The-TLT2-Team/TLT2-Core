package com.fg.tltmod.L2;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Comparator;
import java.util.List;
import java.util.function.IntSupplier;

public class HarvestSharingTrait extends MobTrait {
    public HarvestSharingTrait(IntSupplier color) {
        super((color));
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHeal);
    }
    private void OnLivingHeal(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living!=null) {
            List<Mob> ls0 = living.level().getEntitiesOfClass(Mob.class, living.getBoundingBox().inflate(20));
            for (Mob mob : ls0) {
                if (mob != living && mob != null&&MobTraitCap.HOLDER.isProper(mob)) {
                    if (!validTarget(mob))return;
                    int a = MobTraitCap.HOLDER.get(mob).getTraitLevel(this);
                    if (a >= 1) {
                        mob.heal(event.getAmount()*a*0.2f);
                    }
                }

            }
        }
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
