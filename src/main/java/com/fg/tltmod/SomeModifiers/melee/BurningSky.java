package com.fg.tltmod.SomeModifiers.melee;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.EntityInRangeUtil;
import com.fg.tltmod.content.entity.BurningSkyProjectile;
import com.fg.tltmod.util.MathUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class BurningSky extends EtSTBaseModifier {
    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (context.isFullyCharged()){
            Entity entity = context.getTarget();
            for (int i =0;i<modifier.getLevel()*3+ RANDOM.nextInt(4);i++){
                BurningSkyProjectile projectile = new BurningSkyProjectile(context.getLevel());
                Vec3 end = entity.getBoundingBox().getCenter().add(RANDOM.nextDouble()*8-4,RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*8-4);
                Vec3 start = end.add(RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*2+16,RANDOM.nextDouble()*4-2);
                Vec3 path = end.subtract(start);
                double velocity = path.length()/6;
                projectile.setPos(start);
                projectile.setDeltaMovement(path.normalize().scale(velocity));
                projectile.damage = damage*0.2f;
                projectile.setOwner(context.getAttacker());
                context.getLevel().addFreshEntity(projectile);
            }
        }
        return knockback;
    }
    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (level instanceof ServerLevel serverLevel && player.getAttackStrengthScale(0) > 0.9) {
            var target = EntityInRangeUtil.getNearestLivingEntity(player, (float) player.getEntityReach(), new IntOpenHashSet(),
                    entity -> !(entity instanceof Player) && entity.isAlive() &&
                            MathUtil.includedAngleCos(entity.getBoundingBox().getCenter().subtract(player.getBoundingBox().getCenter()), player.getLookAngle()) > 0.75);
            if (target != null) {
                for (int i =0;i<entry.getLevel()*3+ RANDOM.nextInt(4);i++){
                    BurningSkyProjectile projectile = new BurningSkyProjectile(serverLevel);
                    Vec3 end = target.getBoundingBox().getCenter().add(RANDOM.nextDouble()*6-3,RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*6-3);
                    Vec3 start = end.add(RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*2+16,RANDOM.nextDouble()*4-2);
                    Vec3 path = end.subtract(start);
                    double velocity = path.length()/5;
                    projectile.setPos(start);
                    projectile.setOwner(player);
                    projectile.setDeltaMovement(path.normalize().scale(velocity));
                    projectile.damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE)*0.2f;
                    serverLevel.addFreshEntity(projectile);
                }
            }
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel && player.getAttackStrengthScale(0) > 0.9) {
            var target = EntityInRangeUtil.getNearestLivingEntity(player, (float) player.getEntityReach(), new IntOpenHashSet(),
                    entity -> !(entity instanceof Player) && entity.isAlive() &&
                            MathUtil.includedAngleCos(entity.getBoundingBox().getCenter().subtract(player.getBoundingBox().getCenter()), player.getLookAngle()) > 0.75);
            if (target != null) {
                for (int i =0;i<entry.getLevel()*3+ RANDOM.nextInt(4);i++){
                    BurningSkyProjectile projectile = new BurningSkyProjectile(serverLevel);
                    Vec3 end = target.getBoundingBox().getCenter().add(RANDOM.nextDouble()*8-4,RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*8-4);
                    Vec3 start = end.add(RANDOM.nextDouble()*4-2,RANDOM.nextDouble()*2+16,RANDOM.nextDouble()*4-2);
                    Vec3 path = end.subtract(start);
                    double velocity = path.length()/6;
                    projectile.setPos(start);
                    projectile.setOwner(player);
                    projectile.setDeltaMovement(path.normalize().scale(velocity));
                    projectile.damage = tool.getStats().get(ToolStats.ATTACK_DAMAGE)*0.2f;
                    serverLevel.addFreshEntity(projectile);
                }
            }
        }
    }
}
