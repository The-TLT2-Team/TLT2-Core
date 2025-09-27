package com.fg.tltmod.L2;

import com.fg.tltmod.Register.TltCoreHostilityTrait;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.mob.PerformanceConstants;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.MiscHandlers;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

public class ShowSwordTrait extends MobTrait {
    public ShowSwordTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living) {
            double c = entity.distanceTo(living);
            if (c>=(8-a*2)) {
                event.setAmount(0);
            }
        }
    }
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        cache.addHurtModifier(DamageModifier.multTotal(a*0.4f));
        LivingEntity living=cache.getAttackTarget();
        double c = attacker.distanceTo(living);
        if (c>=(8-a*2)) {
            cache.addHurtModifier(DamageModifier.multTotal(-1f));
        }
    }
    @Override
    public void tick(LivingEntity mob, int level) {
        int range = (8-level*2);
        if (mob.level().isClientSide()) {
            Vec3 center = mob.position();
            float tpi = (float) (Math.PI * 2);
            Vec3 v0 = new Vec3(0, range, 0);
            v0 = v0.xRot(tpi / 4).yRot(mob.getRandom().nextFloat() * tpi);
            int k = TltCoreHostilityTrait.show_sword.get().getColor();
            mob.level().addAlwaysVisibleParticle(ParticleTypes.EFFECT,
                    center.x + v0.x,
                    center.y + v0.y + 0.5f,
                    center.z + v0.z,
                    (k >> 16 & 255) / 255.0,
                    (k >> 8 & 255) / 255.0,
                    (k & 255) / 255.0);
        }
    }
//    private void spawnImmuneParticles(LivingEntity entity,ServerLevel serverLevel,double r) {
//        for (int i = 0; i <= 5; i++) {
//            double rad = i * 0.017453292519943295;
//            double x = r * Math.cos(rad);
//            double z = r * Math.sin(rad);
//            serverLevel.sendParticles(ParticleTypes.EFFECT,entity.getX()+x, entity.getY(),entity.getZ()+z, 1, 0, 0, 0, 1);
//        }
//    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*40 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal((8-i*2) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
