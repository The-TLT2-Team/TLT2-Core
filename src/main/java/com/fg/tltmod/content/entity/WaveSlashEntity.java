package com.fg.tltmod.content.entity;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.util.AttackUtil;
import com.c2h6s.tinkers_advanced.registery.TiAcEntities;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class WaveSlashEntity extends Projectile {
    public WaveSlashEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ToolStack tool = null;
    public float rotDeg=0;
    public IntOpenHashSet set = new IntOpenHashSet();
    public WaveSlashEntity(Level level){
        this(TltCoreEntityTypes.WAVE_SLASH.get(),level);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount>14) this.discard();
        if (!this.level().isClientSide&&!this.isRemoved()){
            List<Entity> list = this.level().getEntitiesOfClass(Entity.class,this.getBoundingBox().inflate(1).expandTowards(this.getDeltaMovement()),this::canHitEntity);
            for (Entity entity:list){
                if (entity instanceof LivingEntity living && tool != null && this.getOwner() instanceof LivingEntity attacker) {
                    AttackUtil.attackEntity(tool, attacker, InteractionHand.MAIN_HAND, living, () -> 1, true, EquipmentSlot.MAINHAND, false, 0, true);
                } else {
                    entity.hurt(LegacyDamageSource.any(this.damageSources().mobProjectile(this, this.getOwner() instanceof LivingEntity living ? living : null)).setBypassArmor().setBypassInvulnerableTime(), 500);
                }
                set.add(entity.getId());
            }
        }
        setPos(this.position().add(this.getDeltaMovement()));
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget!=this&&!set.contains(pTarget.getId())&&pTarget!=this.getOwner()) {
            if (pTarget instanceof Player player && this.getOwner() instanceof Player player1) {
                return player1.canHarmPlayer(player);
            }
            return true;
        }
        return false;
    }
}
