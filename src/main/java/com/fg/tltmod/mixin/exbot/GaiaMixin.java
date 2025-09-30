package com.fg.tltmod.mixin.exbot;

import io.github.lounode.extrabotany.api.gaia.GaiaArena;
import io.github.lounode.extrabotany.common.entity.ExtraBotanyEntityType;
import io.github.lounode.extrabotany.common.entity.gaia.Gaia;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.helper.VecHelper;

import java.util.Set;
import java.util.UUID;

@Mixin(value = Gaia.class, remap = false)
public abstract class GaiaMixin extends Monster {
    protected GaiaMixin(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Final
    @Shadow
    private static EntityDataAccessor<Integer> INVUL_TIME;

    @Final
    @Shadow
    private Set<UUID> playersWhoAttacked;

    /**
     * @author ssakura49
     * @reason 覆写hurt，取消上限
     */
    @Overwrite
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getEntity();
        if (e instanceof Player player) {
            if (PlayerHelper.isTruePlayer(e) && this.entityData.get(INVUL_TIME) == 0) {
                this.tLT2_Core$markPlayerAttacked(player);
                return super.hurt(source, amount);
            }
        }
        return false;
    }

    /**
     * @author ssakura49
     * @reason 覆写actuallyHurt，取消上限
     */
    @Overwrite
    protected void actuallyHurt(@NotNull DamageSource source, float amount) {
        super.actuallyHurt(source, amount);
        Entity attacker = source.getDirectEntity();
        if (attacker != null) {
            Vec3 thisVector = VecHelper.fromEntityCenter(this);
            Vec3 playerVector = VecHelper.fromEntityCenter(attacker);
            Vec3 motionVector = thisVector.subtract(playerVector).normalize().scale((double)0.75F);
            if (this.getHealth() > 0.0F) {
                this.setDeltaMovement(-motionVector.x, (double)0.5F, -motionVector.z);
            }
        }

        this.invulnerableTime = Math.max(this.invulnerableTime, 20);
    }

    /**
     * @author ssakura49
     * @reason 覆写getDamageAfterArmorAbsorb，取消上限
     */
    @Overwrite
    protected float getDamageAfterArmorAbsorb(DamageSource source, float damage) {
        return super.getDamageAfterArmorAbsorb(source, damage);
    }

    @Unique
    private void tLT2_Core$markPlayerAttacked(Player player) {
        if (PlayerHelper.isTruePlayer(player)) {
            this.playersWhoAttacked.add(player.getUUID());
        }

    }
}
