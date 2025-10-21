package com.fg.tltmod.SomeModifiers.melee;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CriticalAttackModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.AttackUtil;
import com.c2h6s.etstlib.util.EntityInRangeUtil;
import com.fg.tltmod.util.MathUtil;
import com.fg.tltmod.util.ParticleContext;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WarpAttack extends EtSTBaseModifier implements CriticalAttackModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.CRITICAL_ATTACK);
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (level instanceof ServerLevel serverLevel &&player.getAttackStrengthScale(0)>0.5){
            var target = EntityInRangeUtil.getNearestLivingEntity(player,4+4*entry.getLevel(),new IntOpenHashSet(),
                    entity -> !(entity instanceof Player)&&entity.isAlive()&&
                            MathUtil.includedAngleCos(entity.getBoundingBox().getCenter().subtract(player.getBoundingBox().getCenter()),player.getLookAngle())>0.5);
            if (target!=null){
                AttackUtil.attackEntity(tool,player,InteractionHand.MAIN_HAND, target,
                        ()->player.getAttackStrengthScale(0),false
                        ,EquipmentSlot.MAINHAND, false,0,false);
                double height = target.getBbHeight();
                Vec3 velocity = new Vec3(RANDOM.nextDouble()*height*2-height,
                        RANDOM.nextDouble()*height*2-height,
                        RANDOM.nextDouble()*height*2-height);
                Vec3 pos =target.position().add(0,target.getBbHeight()/2,0);
                ParticleContext.buildParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get())
                        .setPos(pos.subtract(velocity)).setVelocity(pos.add(velocity)).build().sendToClient(serverLevel);
            }
        }
    }
    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel &&player.getAttackStrengthScale(0)>0.5){
            var target = EntityInRangeUtil.getNearestLivingEntity(player,4+4*entry.getLevel(),new IntOpenHashSet(),
                    entity -> !(entity instanceof Player)&&entity.isAlive()&&
                            MathUtil.includedAngleCos(entity.getBoundingBox().getCenter().subtract(player.getBoundingBox().getCenter()),player.getLookAngle())>0.5);
            if (target!=null){
                AttackUtil.attackEntity(tool,player,InteractionHand.MAIN_HAND, target,
                        ()->player.getAttackStrengthScale(0),false
                        ,EquipmentSlot.MAINHAND, false,0,false);
                double height = target.getBbHeight();
                Vec3 velocity = new Vec3(RANDOM.nextDouble()*height*2-height,
                        RANDOM.nextDouble()*height*2-height,
                        RANDOM.nextDouble()*height*2-height);
                Vec3 position =target.position().add(0,target.getBbHeight()/2,0);
                ParticleContext.buildParticle(ACParticleRegistry.SCARLET_SHIELD_LIGHTNING.get())
                        .setPos(position.subtract(velocity)).setVelocity(position.add(velocity)).build().sendToClient(serverLevel);
            }
        }
    }

    @Override
    public boolean setCritical(IToolStackView iToolStackView, ModifierEntry modifierEntry, LivingEntity livingEntity, InteractionHand interactionHand, Entity entity, EquipmentSlot equipmentSlot, boolean b, boolean b1, boolean b2) {
        return modifierEntry.getLevel()>1;
    }
}
