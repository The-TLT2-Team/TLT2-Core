package com.fg.tltmod.content.entity.living;

import com.c2h6s.tinkers_advanced.util.FakeExplosionUtil;
import com.fg.tltmod.api.interfaces.IMobCooldownProvider;
import com.fg.tltmod.content.entity.ai.GoalWithCD;
import com.fg.tltmod.mixin.SlimeMoveControlInvoker;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class MoonSlimeBoss extends Slime implements IMobCooldownProvider {
    public MoonSlimeBoss(EntityType<? extends Slime> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setSize(4,true);
    }
    public boolean isDashing = false;
    public int dashCd = 0;
    public Map<String,Integer> coolDownMap = new HashMap<>();
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1,new DashGoal(this));
        super.registerGoals();
    }
    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }
    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }
    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.is(DamageTypeTags.IS_DROWNING)||pSource.is(DamageTypeTags.IS_FALL)||pSource.is(DamageTypes.IN_WALL);
    }
    @Override
    public void tick() {
        super.tick();
        tickCooldown(coolDownMap);
    }

    @Override
    protected void dealDamage(LivingEntity pLivingEntity) {
        super.dealDamage(pLivingEntity);
        if (this.isDashing) {
            double knockBack = 1.0D - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            Vec3 direction = pLivingEntity.position().subtract(this.position()).normalize();
            pLivingEntity.setDeltaMovement(pLivingEntity.getDeltaMovement().add(direction.scale(knockBack*getDeltaMovement().length())));
        }
    }

    public void onHitBlock(BlockPos pos){
        this.isDashing = false;
        var set = new IntOpenHashSet();
        set.add(this.getId());
        FakeExplosionUtil.fakeExplode(25,this,this.level(),Vec3.atCenterOf(pos),set,false);
    }

    @Override
    public void remove(RemovalReason pReason) {
        this.brain.clearMemories();
        this.setRemoved(pReason);
        this.invalidateCaps();
    }

    @Override
    public int getCooldown(String key) {
        return coolDownMap.getOrDefault(key,0);
    }

    @Override
    public void setCooldown(String key,int value) {
        coolDownMap.put(key,value);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        var data = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setSize(8,true);
        return data;
    }

    public static class DashGoal extends GoalWithCD {
        private final MoonSlimeBoss entity;
        private int charge = 0;
        private int dashCount = 0;
        private int dashTicker = 0;
        public DashGoal(MoonSlimeBoss entity){
            super(entity,"dash_cd",200);
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));
        }
        @Override
        public boolean canUse() {
            return super.canUse()&&entity.getTarget()!=null;
        }
        @Override
        public void stop() {
            stopDash();
            resetCounters();
            super.stop();
        }
        @Override
        public boolean canContinueToUse() {
            return canUse();
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean isInterruptable() {
            return !entity.isDashing;
        }

        @Override
        public void tick() {
            LivingEntity living = entity.getTarget();
            if (living!=null) {
                if (this.charge < 20) {
                    MoveControl movecontrol = entity.getMoveControl();
                    entity.lookAt(living, 40.0F, 40.0F);
                    if (movecontrol instanceof SlimeMoveControlInvoker slimeMoveControl) {
                        slimeMoveControl.setDirection(entity.getYRot(), entity.isDealsDamage());
                    }
                    this.charge++;
                }
                else if (!entity.isDashing) {
                    if (dashTicker == 0) doDash(living);
                    else breakDash();
                }
                else if (dashTicker < 10) {
                    Vec3 curMovement = entity.getDeltaMovement();
                    Vec3 direction = entity.getDeltaMovement().normalize();
                    entity.setDeltaMovement(direction.scale(Math.max(1.5, curMovement.length() * 0.8)));
                    dashTicker++;
                }
                else breakDash();
            }
        }
        public void doDash(LivingEntity living){
            dashCount++;
            if (dashCount>3) {
                stop();
                return;
            }
            Vec3 vec3 = living.position().subtract(entity.position()).normalize();
            entity.setNoGravity(true);
            entity.setDeltaMovement(vec3.scale(3));
            entity.isDashing = true;
        }
        public void stopDash(){
            entity.setNoGravity(false);
            entity.isDashing = false;
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.1));
        }
        public void breakDash(){
            stopDash();
            dashTicker = 0;
            charge = 10;
        }
        public void resetCounters(){
            charge = 0;
            dashCount = 0;
            dashTicker = 0;
        }
    }
}
