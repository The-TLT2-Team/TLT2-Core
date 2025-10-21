package com.fg.tltmod.content.entity;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.fg.tltmod.util.mixin.IToolProvider;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.common.entity.ManaBurstEntity;


public class ThunderBurstEntity extends ManaBurstEntity implements IManaBurstMixin, IToolProvider {
    private IntOpenHashSet tltmod$hitEntityIds = new IntOpenHashSet();
    private float tltmod$baseDamage;
    private int tltmod$perConsumption=50;
    private int tltmod$perBlockConsumption=50;
    private IToolStackView tltmod$tool;
    int tltmod$generation = 0;
    float tltmod$damageModifier = 1;
    @Override
    public int tltmod$getGeneration() {
        return tltmod$generation;
    }
    @Override
    public void tltmod$setGeneration(int i){
        this.tltmod$generation = i;
    }
    @Override
    public void tltmod$clearHitList() {
        this.tltmod$hitEntityIds.clear();
    }
    @Override
    public float tltmod$getDamageModifier(){
        return tltmod$damageModifier;
    }
    @Override
    public void tltmod$setDamageModifier(float f){
        tltmod$damageModifier = f;
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

    public ThunderBurstEntity(EntityType<ManaBurstEntity> type, Level world) {
        super(type, world);
    }
    public ThunderBurstEntity(Level level){
        this(TltCoreEntityTypes.THUNDER_BURST.get(),level);
    }
    public ThunderBurstEntity(Player player){
        this(player.level());
        this.setOwner(player);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult hit) {
    }

    @Override
    public void particles() {
        super.particles();
    }

    @Override
    public int getColor() {
        return switch (random.nextInt(5)){
            case 1-> 0xBFFFED;
            case 2-> 0x9CFFEA;
            case 3-> 0x61FFED;
            case 4-> 0x34FAFF;
            default -> 0x7DACFF;
        };
    }

    @Override
    public void tick() {
        super.tick();
        this.level().getEntitiesOfClass(Entity.class,this.getBoundingBox().expandTowards(this.getDeltaMovement()),this::canHitEntity).forEach(entity ->
                this.onHitEntity(new EntityHitResult(entity)));
        if (this.tickCount>6) this.discard();
    }

    @Override
    public boolean canHitEntity(Entity pTarget) {
        if (pTarget==getOwner()) return false;
        if (tltmod$hitEntityIds.contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity ||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
    }
}
