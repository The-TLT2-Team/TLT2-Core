package com.fg.tltmod.mixin.l2;

import com.fg.tltmod.util.tinker.ModifierModuleUtil;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.MobEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MobEvents.class,remap = false)
public class MobEventsMixin {
    @Redirect(method = "lambda$onMobAttack$0",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/base/MobTrait;onAttackedByOthers(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingAttackEvent;)V"))
    private static void onAttackImmuneLogic(MobTrait instance, int level, LivingEntity entity, LivingAttackEvent event){
        if (ModifierModuleUtil.getTraitImmunity(instance,event.getSource().getEntity())) return;
        instance.onAttackedByOthers(level,entity,event);
    }
    @Redirect(method = "lambda$onMobHurt$1",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/base/MobTrait;onHurtByOthers(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingHurtEvent;)V"))
    private static void onHurtImmuneLogic(MobTrait instance, int level, LivingEntity entity, LivingHurtEvent event){
        if (ModifierModuleUtil.getTraitImmunity(instance,event.getSource().getEntity())) return;
        instance.onHurtByOthers(level,entity,event);
    }
    @Redirect(method = "lambda$onMobDeath$3",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/base/MobTrait;onDeath(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingDeathEvent;)V"))
    private static void onDeathImmuneLogic(MobTrait instance, int level, LivingEntity entity, LivingDeathEvent event){
        if (ModifierModuleUtil.getTraitImmunity(instance,event.getSource().getEntity())) return;
        instance.onDeath(level,entity,event);
    }
}
