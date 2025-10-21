package com.fg.tltmod.SomeModifiers.melee;

import com.fg.tltmod.Register.TltCoreEffects;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class ToxicOutbreakModifier extends BaseModifier {
    private static final double EFFECT_RADIUS = 5.0D;

    @Override
    public void onKillLivingTarget(IToolStackView tool, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
        if (!target.hasEffect(TltCoreEffects.venom.get())) {
            return;
        }
        int lvl = entry.getLevel();
        Level level = target.level();
        if (level.isClientSide) return;
        AABB area = new AABB(
                target.getX() - EFFECT_RADIUS, target.getY() - 2, target.getZ() - EFFECT_RADIUS,
                target.getX() + EFFECT_RADIUS, target.getY() + 2, target.getZ() + EFFECT_RADIUS
        );
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, area, e ->
                e.isAlive() &&
                        e != attacker &&
                        e != target &&
                        e instanceof Monster
        );

        for (LivingEntity mob : nearby) {
            mob.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    200,
                    lvl + 1,
                    true,
                    true,
                    true
            ));
        }
    }

}
