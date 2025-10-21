package com.fg.tltmod.mixin.bot;

import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.fg.tltmod.util.mixin.IToolProvider;
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
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.ManaBurstEntity;

@Mixin(value = ManaBurstEntity.class,remap = false)
public abstract class ManaBurstEntityMixin extends ThrowableProjectile implements IManaBurstMixin , IToolProvider {
    @Unique
    private IntOpenHashSet tltmod$hitEntityIds = new IntOpenHashSet();
    @Unique
    private float tltmod$baseDamage;
    @Unique
    private int tltmod$perConsumption=50;
    @Unique
    private int tltmod$perBlockConsumption=50;
    @Unique
    private IToolStackView tltmod$tool;
    @Unique int tltmod$generation = 0;
    @Unique float tltmod$damageModifier = 1;

    protected ManaBurstEntityMixin(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    @Unique
    public int tltmod$getGeneration() {
        return tltmod$generation;
    }
    @Override
    @Unique
    public void tltmod$setGeneration(int i){
        this.tltmod$generation = i;
    }

    @Unique
    @Override
    public void tltmod$clearHitList() {
        this.tltmod$hitEntityIds.clear();
    }


    @Unique
    @Override
    public float tltmod$getDamageModifier(){
        return tltmod$damageModifier;
    }
    @Unique
    @Override
    public void tltmod$setDamageModifier(float f){
        tltmod$damageModifier = f;
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

    @Override
    public IToolStackView tltmod$getTool() {
        return tltmod$tool;
    }

    @Override
    public void tltmod$setTool(IToolStackView tool) {
        this.tltmod$tool = tool;
    }
}
