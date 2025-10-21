package com.fg.tltmod.mixin.l2;

import com.fg.tltmod.util.tinker.ModifierModuleUtil;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.LHAttackListener;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LHAttackListener.class,remap = false)
public class LHAttackListenerMixin {
    @Redirect(method = "lambda$onDamage$1",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/base/MobTrait;onDamaged(ILnet/minecraft/world/entity/LivingEntity;Ldev/xkmc/l2damagetracker/contents/attack/AttackCache;)V"))
    private static void addOnDamageImmuneLogic(MobTrait instance, int level, LivingEntity mob, AttackCache cache){
        if (cache.getAttacker()!=null&& ModifierModuleUtil.getTraitImmunity(instance,cache.getAttacker())) return;
        instance.onDamaged(level,mob,cache);
    }
    @Redirect(method = "lambda$onHurt$0",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/base/MobTrait;onHurtTarget(ILnet/minecraft/world/entity/LivingEntity;Ldev/xkmc/l2damagetracker/contents/attack/AttackCache;Ldev/xkmc/l2hostility/content/logic/TraitEffectCache;)V"))
    private static void addOnHurtSourceImmuneLogic(MobTrait instance, int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache){
        if (ModifierModuleUtil.getTraitImmunity(instance,cache.getAttackTarget())) return;
        instance.onHurtTarget(level,attacker,cache,traitCache);
    }
}
