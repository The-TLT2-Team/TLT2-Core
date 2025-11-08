package com.fg.tltmod.content.entity;

import cofh.core.client.particle.options.BiColorParticleOptions;
import cofh.core.init.CoreParticles;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import mekanism.common.lib.radiation.RadiationManager;
import mekanism.common.registries.MekanismParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NeutronArrowEntity extends IonizedArrowEntity{
    private static final EntityDataAccessor<Float> DATA_RADIOACTIVE = SynchedEntityData.defineId(NeutronArrowEntity.class, EntityDataSerializers.FLOAT);

    public NeutronArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(16);
        this.maxLife = 20;
    }
    public NeutronArrowEntity(Level level) {
        this(TltCoreEntityTypes.NEUTRON_ARROW.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_RADIOACTIVE,0f);
    }
    public void setRadioactivity(float f){
        entityData.set(DATA_RADIOACTIVE,f);
    }
    public float getRadioactivity(){
        return entityData.get(DATA_RADIOACTIVE);
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        pVelocity*=(1+20*getRadioactivity());
        super.shoot(pX, pY, pZ, pVelocity, 0);
    }
    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float xRot = pShooter.getXRot();
        float yRot = pShooter.getYRot();
        super.shootFromRotation(pShooter,xRot,yRot,pZ,pVelocity,0);
        Vec3 vec3 = this.getDeltaMovement().cross(new Vec3(0.01,-1,0.01)).normalize();
        double deltaX = Math.sqrt(Math.pow(pX-xRot,2)+Math.pow(pY-yRot,2));
        int i = pX>xRot?1:-1;
        this.setPos(this.position().add(vec3.scale(0.02*i*deltaX)));
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (pResult.getType()== HitResult.Type.BLOCK){
            this.discard();
            return;
        }
        this.setPierceLevel((byte) (this.getPierceLevel()+1));
        super.onHit(pResult);
    }

    @Override
    public void hitAndBlowup(HitResult hitResult) {
        if (hitResult instanceof EntityHitResult entityHitResult&&entityHitResult.getEntity() instanceof LivingEntity living){
            RadiationManager.get().radiate(living,getRadioactivity()*20);
            if (this.level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(MekanismParticleTypes.RADIATION.get(),living.getX(),living.getY()+0.5*living.getBbHeight(),living.getZ(),8,living.getBbWidth()*0.75,living.getBbHeight()*0.75,living.getBbWidth()*0.75,0);
            }
        }
    }
}
