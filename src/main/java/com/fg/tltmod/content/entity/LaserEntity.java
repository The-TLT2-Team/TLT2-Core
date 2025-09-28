package com.fg.tltmod.content.entity;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.api.animation.ControlledAnimation;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileRange;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import com.ssakura49.sakuratinker.utils.tinker.AttackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaserEntity extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, IProjectileRange {
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(LaserEntity.class, EntityDataSerializers.FLOAT);
    public float prevYaw;
    public float prevPitch;

    private ItemStack itemStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private InteractionHand hand;
    private EquipmentSlot slot;
    private float baseDamage;
    private boolean critical = false;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public LaserEntity(EntityType<? extends LaserEntity> type, Level world) {
        super(type, world);
        noCulling = true;
        if (world.isClientSide) {
            attractorPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
    }

    public LaserEntity(EntityType<? extends LaserEntity> type, Level world, LivingEntity caster, ToolStack tool, InteractionHand hand, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.itemStack = ItemStack.EMPTY;
        this.toolStack = tool;
        this.hand = hand;
        this.slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    public LaserEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(TltCoreEntityTypes.LASER_A.get(),level);
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
            this.itemStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.itemStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.stats = StatsNBT.EMPTY;
        }
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }


    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYaw = renderYaw;
        prevPitch = renderPitch;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level().isClientSide) {
            caster = (LivingEntity) level().getEntity(getCasterID());
        }
        if (!level().isClientSide) {
            if (caster instanceof ServerPlayer) {
                this.updateWithPlayer();
            }
        }
        if (caster != null) {
            renderYaw = (float) ((caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            renderPitch = (float) (-caster.getXRot() * Math.PI / 180.0d);
        }

        if (!on && appear.getTimer() == 0) {
            this.discard();
        }
        if (on && tickCount > 2) {
            appear.increaseTimer();
        } else {
            appear.decreaseTimer();
        }

        if (caster != null && !caster.isAlive()) discard();

        if (tickCount > 2) {
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (!level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (target == this.caster) continue;

                    target.invulnerableTime = 0;

                    if (toolStack != null && !toolStack.isBroken()) {
                        ProjectileUtils.attackEntity(
                                itemStack.getItem(),
                                this,
                                toolStack,
                                caster,
                                target,
                                false
                        );
//                        AttackUtil.attackEntity(
//                                toolStack,
//                                (Player) caster,
//                                hand,
//                                target,
//                                () -> 1.0f,
//                                false,
//                                slot,
//                                true,
//                                getDamage(),
//                                true
//                        );
                    } else {
                        target.hurt(
                                LegacyDamageSource.any(this.damageSources().generic())
                                        .setBypassArmor()
                                        .setBypassInvulnerableTime(),
                                100
                        );
                    }
                }
            }
        }
        if (tickCount - 2 > getDuration()) {
            on = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(YAW, 0F);
        this.entityData.define(PITCH, 0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(CASTER, -1);
        this.entityData.define(HEAD, 0);
        this.entityData.define(RANGE, 0F);
    }

    public float getYaw() {
        return entityData.get(YAW);
    }

    public void setYaw(float yaw) {
        entityData.set(YAW, yaw);
    }

    public float getPitch() {
        return entityData.get(PITCH);
    }

    public void setPitch(float pitch) {
        entityData.set(PITCH, pitch);
    }

    public int getDuration() {
        return entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        entityData.set(DURATION, duration);
    }

    public int getHead() {
        return entityData.get(HEAD);
    }

    public void setHead(int head) {
        entityData.set(HEAD, head);
    }


    public int getCasterID() {
        return entityData.get(CASTER);
    }

    public void setCasterID(int id) {
        entityData.set(CASTER, id);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", itemStack.serializeNBT());
        tag.putBoolean("Critical", critical);
        tag.putFloat("BaseDamage", baseDamage);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.critical = tag.getBoolean("Critical");
        this.baseDamage = tag.getFloat("BaseDamage");
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.setTool(buffer.readItem());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        if (level().isClientSide()) {
            endPosX = getX() + getRange() * Math.cos(renderYaw) * Math.cos(renderPitch);
            endPosZ = getZ() + getRange() * Math.sin(renderYaw) * Math.cos(renderPitch);
            endPosY = getY() + getRange() * Math.sin(renderPitch);
        } else {
            endPosX = getX() + getRange() * Math.cos(getYaw()) * Math.cos(getPitch());
            endPosZ = getZ() + getRange() * Math.sin(getYaw()) * Math.cos(getPitch());
            endPosY = getY() + getRange() * Math.sin(getPitch());
        }
    }

    public LaserbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        LaserbeamHitResult result = new LaserbeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.blockHit.getDirection();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    private void updateWithPlayer() {
        this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0d));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + 1.2f + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    @Override
    public void setCritical(boolean b) {
        this.critical = b;
    }

    @Override
    public boolean getCritical() {
        return critical;
    }

    @Override
    public void setDamage(float v) {
        this.baseDamage = v;
    }

    @Override
    public float getDamage() {
        return baseDamage;
    }

    @Override
    public void setRange(float v) {
        entityData.set(RANGE, v);
    }

    @Override
    public float getRange() {
        return entityData.get(RANGE);
    }

    public static class LaserbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
