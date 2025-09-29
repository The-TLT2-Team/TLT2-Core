package com.fg.tltmod.mixin.bot;

import com.fg.tltmod.util.mixin.IManaBurstMixin;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.common.entity.ManaBurstEntity;

@Mixin(value = ManaBurstEntity.class,remap = false)
public abstract class ManaBurstEntityMixin extends ThrowableProjectile implements IManaBurstMixin {
    @Unique
    private IntOpenHashSet tltmod$hitEntityIds = new IntOpenHashSet();
    @Unique
    private float tltmod$baseDamage;
    @Unique
    private int tltmod$perConsumption=50;
    @Unique
    private int tltmod$perBlockConsumption=50;

    protected ManaBurstEntityMixin(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    @Unique
    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget==getOwner()) return false;
        if (tltmod$hitEntityIds.contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
    }

    @Override
    public IntOpenHashSet tltmod$getHitEntityIdList() {
        return tltmod$hitEntityIds;
    }

    @Override
    public void tltmod$addToHitList(Entity entity) {
        tltmod$hitEntityIds.add(entity.getId());
    }

    @Override
    public float tltmod$getBaseDamage() {
        return tltmod$baseDamage;
    }

    @Override
    public void tltmod$setBaseDamage(float baseDamage) {
        this.tltmod$baseDamage=baseDamage;
    }

    @Override
    public int tltmod$getPerConsumption() {
        return tltmod$perConsumption;
    }

    @Override
    public void tltmod$setPerConsumption(int i) {
        tltmod$perConsumption=i;
    }

    @Override
    public int tltmod$getPerBlockConsumption() {
        return tltmod$perBlockConsumption;
    }

    @Override
    public void tltmod$setPerBlockConsumption(int i) {
        tltmod$perBlockConsumption = i;
    }
}
