package com.fg.tltmod.event;

import com.fg.tltmod.Register.TltCoreEffects;
import com.fg.tltmod.TltCore;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TltCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OscillationEvent {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.isCancelable() &&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() &&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onLivingSwapItems(LivingSwapItemsEvent event) {
        if (event.isCancelable()&&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().jumping = false;
            event.getInput().shiftKeyDown = false;
        }
    }
    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntity();
        if (living != null) {
            if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living) {
            if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(TltCoreEffects.oscillation.get())) {
            int a = 1;
            if (entity.getEffect(TltCoreEffects.oscillation.get())!=null){
                a += entity.getEffect(TltCoreEffects.oscillation.get()).getAmplifier();
            }
            event.setAmount(event.getAmount()*(1f+a));
            entity.removeEffect(TltCoreEffects.oscillation.get());
        }
    }
}
