package com.fg.tltmod.content.entity;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.util.TltmodCommonUtil;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonFireCharge;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;

public class BurningSkyProjectile extends EntityDragonFireCharge {
    public float damage;
    public BurningSkyProjectile(Level pLevel) {
        super(TltCoreEntityTypes.BURNING_SKY_PROJ.get(), pLevel);
    }

    @Override
    public void tick() {
        if (this.tickCount>6) {
            this.hitAndBlowup();
            return;
        }
        super.tick();
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (this.level().isClientSide) return;
        HitResult.Type hitresult$type = pResult.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.hitAndBlowup(pResult);
        } else if (hitresult$type == HitResult.Type.BLOCK&&this.tickCount>4) {
            this.hitAndBlowup(pResult);
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return TltmodCommonUtil.projectileShouldHit(entity)&&!(entity instanceof Projectile) &&entity!=this.getOwner()&&!(entity instanceof Player);
    }

    @Override
    protected boolean canHitMob(Entity hitMob) {
        return this.canHitEntity(hitMob);
    }

    public void hitAndBlowup(HitResult hitResult){
        if (hitResult.getType()== HitResult.Type.MISS) return;
        Vec3 vec3 = hitResult.getLocation();
        AABB aabb = new AABB(vec3.subtract(6,3,6),vec3.add(6,3,6));
        if (hitResult instanceof EntityHitResult result){
            vec3 = result.getEntity().getBoundingBox().getCenter();
        }
        this.level().getEntitiesOfClass(LivingEntity.class,aabb,
                living -> living!=this.getOwner()&&!(living instanceof Player)).forEach(living ->
                living.hurt(LegacyDamageSource.any(IafDamageRegistry.causeIndirectDragonFireDamage(this,this.getOwner())).setBypassInvulnerableTime().setBypassArmor(), this.damage));
        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.EXPLOSION,vec3.x,vec3.y,vec3.z,1,0,0,0,0);
            serverLevel.sendParticles(ParticleTypes.FLAME,vec3.x,vec3.y,vec3.z,random.nextInt(6)+6,0,0,0,0.5);
        }
        this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        this.discard();
    }
    public void hitAndBlowup(){
        if (this.level().isClientSide)return;
        Vec3 vec3 = this.position();
        AABB aabb = new AABB(vec3.subtract(4,2,4),vec3.add(4,2,4));
        this.level().getEntitiesOfClass(LivingEntity.class,aabb,
                living -> living!=this.getOwner()&&!(living instanceof Player)).forEach(living ->
                living.hurt(LegacyDamageSource.any(IafDamageRegistry.causeIndirectDragonFireDamage(this,this.getOwner())).setBypassInvulnerableTime().setBypassArmor(), this.damage));
        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.EXPLOSION,vec3.x,vec3.y,vec3.z,1,0,0,0,0);
            serverLevel.sendParticles(ParticleTypes.FLAME,vec3.x,vec3.y,vec3.z,random.nextInt(6)+6,0,0,0,0.5);
        }
        this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        this.discard();
    }

    @Override
    public void destroyArea(Level world, BlockPos center, EntityDragonBase destroyer) {
    }
}
