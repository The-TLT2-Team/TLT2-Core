package com.fg.tltmod.content.entity;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LaserBeamArrowEntity extends AbstractArrow {
    boolean critical = false;
    public LaserBeamArrowEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public LaserBeamArrowEntity(Level level){
        this(TltCoreEntityTypes.LASER_BEAM_ARROW.get(),level);
        this.setBaseDamage(6);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        this.critical = this.isCritArrow();
        this.setCritArrow(false);
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().scale(1.011));
        if (this.inGround) this.discard();
        this.setCritArrow(this.critical);
        if (this.tickCount>200) this.discard();
    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float xRot = pShooter.getXRot();
        float yRot = pShooter.getYRot();
        Vec3 direction = pShooter.getLookAngle();
        super.shoot(direction.x,direction.y,direction.z,pVelocity,0);
        Vec3 vec3 = this.getDeltaMovement().cross(new Vec3(0.01,-1,0.01)).normalize();
        double deltaX = Math.sqrt(Math.pow(pX-xRot,2)+Math.pow(pY-yRot,2));
        int i = pX>xRot?1:-1;
        this.setPos(this.position().add(vec3.scale(0.02*i*deltaX)));
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_)&&p_36743_!=this.getOwner();
    }
    @Override
    protected void onHit(HitResult pResult) {
        this.setCritArrow(this.critical);
        super.onHit(pResult);
        this.setCritArrow(false);
    }
}
