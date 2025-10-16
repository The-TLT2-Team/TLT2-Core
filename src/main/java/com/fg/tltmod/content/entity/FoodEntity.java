package com.fg.tltmod.content.entity;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class FoodEntity extends Projectile {
    public FoodEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ItemStack stack;
    public FoodEntity(Level level){
        this(TltCoreEntityTypes.FOOD_ENTITY.get(), level);
    }
    private static final EntityDataAccessor<ItemStack> DATA_FOOD = SynchedEntityData.defineId(FoodEntity.class, EntityDataSerializers.ITEM_STACK);
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FOOD, ItemStack.EMPTY);
    }

    private static final List<Item> FOOD = new ArrayList<>();
    private static final Random RANDOM = new Random();
    static {
        for (Item item : ForgeRegistries.ITEMS) {
            if (isValidFoodItem(item)) {
                FOOD.add(item);
            }
        }
    }
    private static boolean isValidFoodItem(Item item) {
        return item != null &&
                item != Items.AIR &&
                item.isEdible() &&
                item.getDefaultInstance().isEdible();
    }
    public static Item getRandomFood() {
        return FOOD.get(RANDOM.nextInt(FOOD.size()));
    }
    public ItemStack getFood() {
        if (this.stack == null||stack.isEmpty()||stack.getItem() == Items.AIR) {
            if (!this.level().isClientSide()) {
                int maxAttempts = 30;
                ItemStack item = Items.APPLE.getDefaultInstance();
                for (int i = 0; i < maxAttempts; i++) {
                    item = getRandomFood().getDefaultInstance();
                    if (isValidStack(item)) {
                        break;
                    }
                }
                this.stack = item;
                this.entityData.set(DATA_FOOD, item);
            } else {
                ItemStack syncedStack = this.entityData.get(DATA_FOOD);
                if (!syncedStack.isEmpty()) {
                    return syncedStack;
                }
            }
        }
        return this.stack;
    }
    private boolean isValidStack(ItemStack stack) {
        return stack != null &&
                !stack.isEmpty() &&
                stack.getItem() != Items.AIR &&
                stack.getItem().isEdible();
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.level().isClientSide() && key.equals(DATA_FOOD)) {
            this.stack = this.entityData.get(DATA_FOOD).getItem().getDefaultInstance();
        }
    }
    public ItemStack getItem() {
        if (this.level().isClientSide()) {
            return this.entityData.get(DATA_FOOD).getItem().getDefaultInstance();
        }
        return getFood();
    }

    private void FoodSummon() {
        if (this.level() instanceof ServerLevel level) {
            ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), getItem());
            itemEntity.setPickUpDelay(40);
            level.addFreshEntity(itemEntity);
        }
    }

    @Override
    public void tick() {
        getItem();
        super.tick();
        if (this.tickCount>200) this.discard();
        if (this.getDeltaMovement().length() < 1) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.05, 0));
        }
        HitResult hitresult = this.level().clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(), this.position().add(this.getDeltaMovement()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(2),this::canHitEntity);
        super.move(MoverType.SELF,this.getDeltaMovement());
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (hitresult.getType()!= HitResult.Type.MISS){
            this.onHit(hitresult);
        }
        if (this.onGround()){
            this.onHit(new BlockHitResult(this.position(), Direction.UP,this.blockPosition().below(),false));
        }
    }

    public void Explode(){
        if (!this.level().isClientSide) {
            Explosion explosion =this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1, false, Level.ExplosionInteraction.NONE);
            List<LivingEntity> lis = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8));
            for (LivingEntity entity : lis) {
                if (entity != null) {
                    entity.invulnerableTime = 0;
                    entity.hurt(this.level().damageSources().explosion(explosion),6);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        Explode();
        FoodSummon();
        this.discard();
    }
    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        Explode();
        FoodSummon();
        this.discard();
    }
}
