package com.fg.tltmod.mixin.cloudertinker;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fuyun.cloudertinker.Effects.Bloodlust;
import com.fuyun.cloudertinker.register.CloudertinkerEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import twilightforest.entity.boss.Lich;

@Mixin(Bloodlust.class)
public class BloodlustMixin {
    @Shadow
    public static ResourceLocation bloodlust;


    @Inject(method = "LivingHurtEvent", at = @At("TAIL"), remap = false)
    private void injectSuperpositionCheck(LivingHurtEvent event, CallbackInfo ci) {
        if (event.getEntity() != null) {
            Entity entity1 = event.getSource().getEntity();

            LivingEntity livingEntity = null;

            if (entity1 instanceof LivingEntity) {
                livingEntity = (LivingEntity) entity1;
            } else if (entity1 instanceof Projectile && ((Projectile) entity1).getOwner() != null) {
                livingEntity = (LivingEntity) ((Projectile) entity1).getOwner();
            }

            if (livingEntity != null) {
                ModDataNBT entitydata = ModDataNBT.readFromNBT(livingEntity.getPersistentData());
                MobEffectInstance instance = livingEntity.getEffect(CloudertinkerEffects.Bloodlust.get());

                if (instance != null && livingEntity instanceof Player player) {
                    if (SuperpositionHandler.isTheCursedOne(player)) {
                        entitydata.putInt(bloodlust, entitydata.getInt(bloodlust)+6);
                    }
                }
            }
        }
    }
}

