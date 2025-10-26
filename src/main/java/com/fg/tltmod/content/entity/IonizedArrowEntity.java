package com.fg.tltmod.content.entity;

import cofh.core.client.particle.options.BiColorParticleOptions;
import cofh.core.init.CoreParticles;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

public class IonizedArrowEntity extends AbstractArrow {
    public boolean initSpeed = true;
    boolean critical = false;
    private static final EntityDataAccessor<Float> DATA_VELOCITY = SynchedEntityData.defineId(IonizedArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_OFFHAND = SynchedEntityData.defineId(IonizedArrowEntity.class,EntityDataSerializers.BOOLEAN);
    public IonizedArrowEntity(Level pLevel) {
        super(TltCoreEntityTypes.IONIZED_ARROW.get(), pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_VELOCITY,0f);
        entityData.define(DATA_OFFHAND,false);
    }
    public void setVelocity(float f){
        entityData.set(DATA_VELOCITY,f);
    }
    public float getVelocity(){
        return entityData.get(DATA_VELOCITY);
    }

    public void setOffhand(boolean b){entityData.set(DATA_OFFHAND,b);}
    public boolean isOffhand(){return entityData.get(DATA_OFFHAND);}

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        setVelocity(pVelocity);
        super.shoot(pX, pY, pZ, 1, 0);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        if (this.inGround){
            this.discard();
            return;
        }
        if (this.firstTick){
            this.critical = this.isCritArrow();
            this.setCritArrow(false);
        }
        if (++this.tickCount>200){
            this.discard();
            return;
        }
        boolean b = this.firstTick;
        Vec3 vec3 = this.getDeltaMovement();
        if (!b){
            initSpeed = true;
            setDeltaMovement(vec3.scale(getVelocity()));
            initSpeed = false;
        }
        super.tick();
        if (!b){
            initSpeed = true;
            setDeltaMovement(vec3);
            initSpeed = false;
        }
    }


    @Override
    public void setDeltaMovement(@NotNull Vec3 pDeltaMovement) {
        if (initSpeed){
            super.setDeltaMovement(pDeltaMovement);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity)&&entity!=this.getOwner()&&!(entity instanceof Player);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        hitAndBlowup(pResult);
        pResult.getEntity().invulnerableTime = 0;
        super.onHitEntity(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        BlockState blockstate = this.level().getBlockState(pResult.getBlockPos());
        blockstate.onProjectileHit(this.level(), blockstate, pResult, this);
        hitAndBlowup(pResult);
    }

    @Override
    protected void onHit(HitResult pResult) {
        this.setCritArrow(this.critical);
        super.onHit(pResult);
        this.setCritArrow(false);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void hitAndBlowup(HitResult hitResult){
        if (hitResult.getType()== HitResult.Type.MISS) return;
        Vec3 vec3 = hitResult.getLocation();
        AABB aabb = new AABB(vec3.subtract(2,2,2),vec3.add(2,2,2));
        if (hitResult instanceof EntityHitResult result){
            vec3 = result.getEntity().getBoundingBox().getCenter();
        }
        this.level().getEntitiesOfClass(LivingEntity.class,aabb,
                living -> living!=this.getOwner()&&!(living instanceof Player)).forEach(living ->
                living.hurt(IafDamageRegistry.causeIndirectDragonLightningDamage(this,this.getOwner()), (float) (this.getVelocity()*this.getBaseDamage())));
        if (this.level() instanceof ServerLevel serverLevel){
            for (int i=0;i<random.nextInt(4)+4;i++){
                serverLevel.sendParticles(new BiColorParticleOptions(CoreParticles.STRAIGHT_ARC.get(), 0.3F, 5.0F, 0.0F, -1, 0xAC5FFFFF),vec3.x,vec3.y,vec3.z,0,vec3.x+random.nextFloat()*4-2,vec3.y+random.nextFloat()*4-2,vec3.z+random.nextFloat()*4-2,1);
            }
            serverLevel.sendParticles(ParticleTypes.FIREWORK,vec3.x,vec3.y,vec3.z,random.nextInt(4)+4,0,0,0,0.1);
        }
        if (hitResult instanceof BlockHitResult) this.discard();
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
    }
}
