package com.fg.tltmod.effects;

import com.fg.tltmod.Register.TltCoreEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;

public class Oscillation extends TltCoreEffect {
    public Oscillation() {
        super(MobEffectCategory.NEUTRAL, 0xFF8C00);
        super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "AB6CEABF-6904-3887-1188-F7CE3930EEEC", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL);

        MinecraftForge.EVENT_BUS.addListener(this::onPlayerInteract);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerAttack);
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::onBreakBlock);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClickEmpty);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClickItem);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerRightClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingSwapItems);
        MinecraftForge.EVENT_BUS.addListener(this::onMovementInputUpdate);
        MinecraftForge.EVENT_BUS.addListener(this::onFillBucket);
        MinecraftForge.EVENT_BUS.addListener(this::onUseItem);
        MinecraftForge.EVENT_BUS.addListener(this::onPlaceBlock);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.isCancelable() &&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() &&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }

    public void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onLivingSwapItems(LivingSwapItemsEvent event) {
        if (event.isCancelable()&&event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }
    public void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (event.getEntity().hasEffect(TltCoreEffects.oscillation.get())) {
            event.getInput().forwardImpulse = 0;
            event.getInput().leftImpulse = 0;
            event.getInput().jumping = false;
            event.getInput().shiftKeyDown = false;
        }
    }
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntity();
        if (living != null) {
            if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
                event.setCanceled(true);
            }
        }
    }
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
            event.setCanceled(true);
        }
    }

    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living) {
            if (event.isCancelable() && living.hasEffect(TltCoreEffects.oscillation.get())) {
                event.setCanceled(true);
            }
        }
    }

    public void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity!=null&&entity.hasEffect(TltCoreEffects.oscillation.get())) {
            int a = 1;
            if (entity.getEffect(TltCoreEffects.oscillation.get())!=null){
                a += entity.getEffect(TltCoreEffects.oscillation.get()).getAmplifier();
            }
            event.setAmount(event.getAmount()*(1f+a));
            entity.removeEffect(TltCoreEffects.oscillation.get());
        }
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return super.isDurationEffectTick(duration, amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        super.applyEffectTick(living, amplifier);
    }
}
