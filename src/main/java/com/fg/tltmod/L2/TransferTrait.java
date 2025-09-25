package com.fg.tltmod.L2;

import com.fg.tltmod.TltCore;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.List;
import java.util.Random;
import java.util.function.IntSupplier;

public class TransferTrait extends MobTrait {
    public TransferTrait(IntSupplier color) {
        super((color));
    }
    public static String transfer_cooldown = TltCore.getResource("transfer_cooldown").toString();
    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        Random random=new Random();
        if (event.getSource().getEntity() instanceof LivingEntity living) {
            if (getAllModifierlevel(living, TinkerModifiers.enderference.getId())>0
                    ||living.hasEffect(TinkerEffects.enderference.get()))return;

            if (random.nextInt(100) < a * 10 && entity.getPersistentData().getInt(transfer_cooldown) == 0) {
                teleport(living);
                entity.getPersistentData().putInt(transfer_cooldown, 60);
            }
        }
    }
    @Override
    public void tick(LivingEntity mob, int a) {
        if (!mob.level().isClientSide()&&mob.getPersistentData().getInt(transfer_cooldown)>0) {
            mob.getPersistentData().putInt(transfer_cooldown,mob.getPersistentData().getInt(transfer_cooldown)-1);
        }
    }
    private static boolean teleport(LivingEntity entity) {
        int r = 6;
        if (!entity.level().isClientSide() && entity.isAlive()) {
            double d0 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * r * 2;
            double d1 = entity.getY() + (double) (entity.getRandom().nextInt(r * 2) - r);
            double d2 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * r * 2;
            return teleport(entity, d0, d1, d2);
        } else {
            return false;
        }
    }
    private static boolean teleport(LivingEntity entity, double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos(pX, pY, pZ);
        while (ipos.getY() > entity.level().getMinBuildHeight() && !entity.level().getBlockState(ipos).blocksMotion()) {
            ipos.move(Direction.DOWN);
        }

        BlockState blockstate = entity.level().getBlockState(ipos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(entity, pX, pY, pZ);
            if (event.isCanceled()) return false;
            Vec3 vec3 = entity.position();
            boolean flag2 = entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                entity.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                if (!entity.isSilent()) {
                    entity.level().playSound(null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return flag2;
        } else {
            return false;
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*10 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
    public static int getAllModifierlevel(LivingEntity entity, ModifierId modifierId) {
        return ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.MAINHAND), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.OFFHAND), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.HEAD), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.CHEST), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.LEGS), modifierId)
                + ModifierUtil.getModifierLevel(entity.getItemBySlot(EquipmentSlot.FEET), modifierId);
    }
}
