package com.fg.tltmod.mixin;

import com.fg.tltmod.util.TltmodHurtProcess;
import com.fg.tltmod.util.mixin.ILivingEntityMixin;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {
    @Shadow public abstract boolean isDeadOrDying();

    @Shadow public abstract boolean isSleeping();

    @Shadow public abstract void stopSleeping();

    @Shadow protected int noActionTime;

    @Shadow protected abstract void blockUsingShield(LivingEntity pAttacker);

    @Shadow public abstract boolean isDamageSourceBlocked(DamageSource pDamageSource);

    @Shadow protected abstract void hurtCurrentlyUsedShield(float pDamageAmount);

    @Shadow @Final public WalkAnimationState walkAnimation;

    @Shadow protected float lastHurt;

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot pSlot);

    @Shadow protected abstract void hurtHelmet(DamageSource pDamageSource, float pDamageAmount);

    @Shadow public abstract void setLastHurtByMob(@Nullable LivingEntity pLivingEntity);

    @Shadow protected int lastHurtByPlayerTime;

    @Shadow @javax.annotation.Nullable protected Player lastHurtByPlayer;

    @Shadow public abstract void knockback(double pStrength, double pX, double pZ);

    @Shadow public abstract void indicateDamage(double p_270514_, double p_270826_);

    @Shadow @javax.annotation.Nullable protected abstract SoundEvent getDeathSound();

    @Shadow protected abstract float getSoundVolume();

    @Shadow public abstract float getVoicePitch();

    @Shadow protected abstract void playHurtSound(DamageSource pSource);

    @Shadow @javax.annotation.Nullable private DamageSource lastDamageSource;

    @Shadow private long lastDamageStamp;

    @Shadow protected abstract void actuallyHurt(DamageSource pDamageSource, float pDamageAmount);

    @Shadow public int hurtDuration;

    @Shadow public int hurtTime;

    @Shadow public abstract void setHealth(float pHealth);

    @Shadow public abstract CombatTracker getCombatTracker();

    @Shadow public abstract void setAbsorptionAmount(float pAbsorptionAmount);

    @Shadow public abstract float getAbsorptionAmount();

    @Shadow public abstract float getHealth();

    @Shadow protected boolean dead;

    @Shadow @javax.annotation.Nullable public abstract LivingEntity getKillCredit();

    @Shadow protected int deathScore;

    @Shadow @Final private static Logger LOGGER;

    @Shadow protected abstract void dropAllDeathLoot(DamageSource pDamageSource);

    @Shadow protected abstract void createWitherRose(@Nullable LivingEntity pEntitySource);

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private boolean tltmod$hurtLogic(DamageSource source,float amount){
        net.minecraftforge.common.ForgeHooks.onLivingAttack((LivingEntity)(Object) this, source, amount);
        if (TltmodHurtProcess.getHurtDamage(this,amount)<=0) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (isDeadOrDying()) {
            return false;
        } else {
            if (isSleeping() && !this.level().isClientSide) {
                stopSleeping();
            }

            noActionTime = 0;
            float f = amount;
            boolean flag = false;
            float f1 = 0.0F;

            if (source.is(DamageTypeTags.IS_FREEZING) && this.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                amount *= 5.0F;
            }

            walkAnimation.setSpeed(1.5F);
            boolean flag1 = true;
            lastHurt = amount;
            this.invulnerableTime = 20;
            tltmod$actualHurtLogic(source, amount);
            hurtDuration = 10;
            hurtTime = this.hurtDuration;
            if (source.is(DamageTypeTags.DAMAGES_HELMET) && !getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                hurtHelmet(source, amount);
            }

            Entity entity1 = source.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity) {
                    LivingEntity livingentity1 = (LivingEntity)entity1;
                    if (!source.is(DamageTypeTags.NO_ANGER)) {
                        setLastHurtByMob(livingentity1);
                    }
                }

                if (entity1 instanceof Player) {
                    Player player1 = (Player)entity1;
                    lastHurtByPlayerTime = 100;
                    lastHurtByPlayer = player1;
                } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
                    if (tamableEntity.isTame()) {
                        this.lastHurtByPlayerTime = 100;
                        LivingEntity livingentity2 = tamableEntity.getOwner();
                        if (livingentity2 instanceof Player) {
                            Player player = (Player)livingentity2;
                            this.lastHurtByPlayer = player;
                        } else {
                            this.lastHurtByPlayer = null;
                        }
                    }
                }
            }

            this.level().broadcastDamageEvent(this, source);

            if (!source.is(DamageTypeTags.NO_IMPACT) && (!flag || amount > 0.0F)) {
                this.markHurt();
            }

            if (entity1 != null && !source.is(DamageTypeTags.IS_EXPLOSION)) {
                double d0 = entity1.getX() - this.getX();

                double d1;
                for(d1 = entity1.getZ() - this.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                    d0 = (Math.random() - Math.random()) * 0.01D;
                }

                knockback((double)0.4F, d0, d1);
                if (!flag) {
                    indicateDamage(d0, d1);
                }
            }

            if (this.isDeadOrDying()) {
                SoundEvent soundevent = getDeathSound();
                if (flag1 && soundevent != null) {
                    this.playSound(soundevent, getSoundVolume(), getVoicePitch());
                }

                tltmod$dieLogic(source);
            } else {
                playHurtSound(source);
            }

            lastDamageSource = source;
            lastDamageStamp = this.level().getGameTime();
            LivingEntity living = (LivingEntity) (Object) this;

            if (living instanceof ServerPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)living, source, f, amount, flag);
                if (f1 > 0.0F && f1 < 3.4028235E37F) {
                    ((ServerPlayer)living).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_BLOCKED_BY_SHIELD), Math.round(f1 * 10.0F));
                }
            }

            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, this, source, f, amount, flag);
            }

            return true;
        }
    }
    @Unique
    private void tltmod$dieLogic(DamageSource source){
        LivingEntity living = (LivingEntity)(Object)this;
        net.minecraftforge.common.ForgeHooks.onLivingDeath(living, source);
        setHealth(0);
        setRemoved(RemovalReason.KILLED);
        dead = true;
        Entity entity = source.getEntity();
        LivingEntity livingentity = getKillCredit();
        if (deathScore >= 0 && livingentity != null) {
            livingentity.awardKillScore(this, this.deathScore, source);
        }

        if (this.isSleeping()) {
            this.stopSleeping();
        }

        if (!this.level().isClientSide && this.hasCustomName()) {
            LOGGER.info("Named entity {} died: {}", this, this.getCombatTracker().getDeathMessage().getString());
        }

        this.dead = true;
        this.getCombatTracker().recheckStatus();
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            if (entity == null || entity.killedEntity(serverlevel, living)) {
                this.gameEvent(GameEvent.ENTITY_DIE);
                dropAllDeathLoot(source);
                createWitherRose(livingentity);
            }

            this.level().broadcastEntityEvent(this, (byte)3);
        }

        this.setPose(Pose.DYING);
    }
    @Unique
    private void tltmod$actualHurtLogic(DamageSource source,float amount){
        LivingEntity living = (LivingEntity)(Object)this;
        net.minecraftforge.common.ForgeHooks.onLivingHurt(living, source, amount);
        net.minecraftforge.common.ForgeHooks.onLivingDamage(living, source, amount);
        if (amount != 0.0F) {
            getCombatTracker().recordDamage(source, amount);
            setHealth(getHealth() - amount);
            setAbsorptionAmount(getAbsorptionAmount() - amount);
            this.gameEvent(GameEvent.ENTITY_DAMAGE);
        }
    }

    @Override
    public void tltmod$actualHurt(DamageSource source, float amount) {
        tltmod$actualHurtLogic(source,amount);
    }

    @Override
    public boolean tltmod$hurt(DamageSource source, float amount) {
        return tltmod$hurtLogic(source,amount);
    }

    @Override
    public void tltmod$die(DamageSource source) {
        tltmod$dieLogic(source);
    }
}
