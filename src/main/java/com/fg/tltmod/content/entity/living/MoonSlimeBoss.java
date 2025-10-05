package com.fg.tltmod.content.entity.living;

import com.fg.tltmod.mixin.SlimeMoveControlInvoker;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MoonSlimeBoss extends Slime {
    public MoonSlimeBoss(EntityType<? extends Slime> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setSize(4,true);
    }
    public boolean isDashing = false;
    public boolean canDash = true;
    public int dashCd = 0;

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
        if (dashCd>0) dashCd--;
    }

    public boolean dashReady() {
        return canDash&&dashCd<=0;
    }

    public void hitBlock(BlockPos pos){

    }

    public static class DashGoal extends Goal{
        private final MoonSlimeBoss entity;
        private int charge = 0;
        private int dashCount = 0;
        private int dashTicker = 0;
        public DashGoal(MoonSlimeBoss entity){
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return entity.dashReady()&&entity.getTarget()!=null;
        }

        @Override
        public void stop() {
            stopDash();
            resetCounters();
            entity.canDash = true;
            entity.dashCd = 100;
        }

        @Override
        public boolean canContinueToUse() {
            return entity.dashReady()&&entity.getTarget()!=null;
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
                } else if (!entity.isDashing) {
                    if (dashTicker == 0) doDash(living);
                } else if (dashTicker < 5) {
                    Vec3 curMovement = entity.getDeltaMovement();
                    Vec3 direction = entity.getDeltaMovement().normalize();
                    entity.setDeltaMovement(direction.scale(Math.max(1.5, curMovement.length() * 0.8)));
                    dashTicker++;
                } else {
                    stopDash();
                    dashTicker = 0;
                    charge = 15;
                }
            }
        }

        public void doDash(LivingEntity living){
            dashCount++;
            if (dashCount>3) {
                resetCounters();
                entity.dashCd = 100;
                return;
            }
            Vec3 vec3 = living.position().subtract(entity.position().add(living.getDeltaMovement())).normalize();
            entity.setNoGravity(true);
            entity.noPhysics = true;
            entity.setDeltaMovement(vec3.scale(3));
            entity.isDashing = true;
        }

        public void stopDash(){
            entity.setNoGravity(false);
            entity.noPhysics = false;
            entity.isDashing = false;
        }

        public void resetCounters(){
            charge = 0;
            dashCount = 0;
            dashTicker = 0;
        }

    }
}
