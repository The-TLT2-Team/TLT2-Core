package com.fg.tltmod.SomeModifiers.misc;

import com.fg.tltmod.Register.TltCoreEffects;
import com.fg.tltmod.effects.TltCoreEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import util.method.ModifierEffect;

public class VibrioVulnificus extends Modifier implements  MeleeHitModifierHook,ProjectileHitModifierHook, ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.PROJECTILE_HIT,ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget()!=null&&context.getLivingTarget()!= context.getAttacker()){
            ModifierEffect.direaddMobEffect(context.getLivingTarget(), TltCoreEffects.vibrio_vulnificus.get(),200*modifier.getLevel(),0);
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target!=null&&target!=attacker) {
            ModifierEffect.direaddMobEffect(target,TltCoreEffects.vibrio_vulnificus.get(), 200*modifier.getLevel(), 0);
        }
        return false;
    }
    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifier, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if (damageSource.getEntity() instanceof LivingEntity entity) {
            ModifierEffect.direaddMobEffect(entity,TltCoreEffects.vibrio_vulnificus.get(), 200*modifier.getLevel(), 0);
        }
        return v;
    }
}
