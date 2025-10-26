package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.PnCIntegration.AirStorageProvider;
import com.c2h6s.etstlib.util.ToolEnergyUtil;
import com.c2h6s.tinkers_advanced.util.CommonUtil;
import com.fg.tltmod.Register.TltCoreEffects;
import com.fuyun.cloudertinker.Cloudertinker;
import com.fuyun.cloudertinker.register.CloudertinkerItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.shared.TinkerMaterials;
import twilightforest.entity.monster.SkeletonDruid;
import util.method.ModifierEffect;

import java.util.List;


public class ThunderFire extends Modifier implements MeleeHitModifierHook {
    public static final ResourceLocation heatbomb = Cloudertinker.getResource("heatbomb");
    public static final ResourceLocation heatbombdamage = Cloudertinker.getResource("heatbombdamage");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        MinecraftForge.EVENT_BUS.addListener(this::LivingHurtEvent);
    }
    @Nullable
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        iToolStackView.getPersistentData().remove(heatbomb);
        return null;}
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        ModDataNBT tooldata = tool.getPersistentData();
        int sum=1;
        if (context.getLivingTarget()!=null&&context.getLivingTarget()!= context.getAttacker()&& context.isFullyCharged()){
            ModDataNBT entitydata = ModDataNBT.readFromNBT(context.getLivingTarget().getPersistentData());
            tooldata.putInt(heatbomb, tooldata.getInt(heatbomb)+1);
            if (ToolEnergyUtil.extractEnergy(tool,250,true)>=250){
                ToolEnergyUtil.extractEnergy(tool,250,false);
                sum*=2;
            }
            if (AirStorageProvider.getAir(tool)>50){
                AirStorageProvider.addAir(tool,-50);
                sum*=2;
            }
            if (tooldata.getInt(heatbomb)>=(4/sum)){
                double x = context.getLivingTarget().getX();
                double y = context.getLivingTarget().getY();
                double z = context.getLivingTarget().getZ();
                List<Mob> mobList = context.getLivingTarget().getCommandSenderWorld().getEntitiesOfClass(Mob.class, new AABB(x + 1, y + 1, z + 1, x - 1, y - 1, z - 1));
                for (Mob mob : mobList) {
                    if (mob != null) {
                        mob.invulnerableTime= 0;
                        mob.hurt(LegacyDamageSource.mobAttack(context.getAttacker()).setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setExplosion().setMsgId("thunderfire_damage"),(float) ((damageDealt*0.5*modifier.getLevel())));
                        mob.setRemainingFireTicks((100));
                        mob.invulnerableTime= 0;

                    }}
                context.getLivingTarget().playSound(SoundEvents.GENERIC_EXPLODE,1,1);
                if (context.getLivingTarget().getCommandSenderWorld() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION,context.getLivingTarget().getX(),context.getLivingTarget().getY()+0.5*context.getLivingTarget().getBbHeight(),context.getLivingTarget().getZ(),1 ,0,0,0,0);
                }
                entitydata.putFloat(heatbombdamage, (float) (damageDealt*0.5*modifier.getLevel()));
                context.getLivingTarget().getPersistentData().putInt(CommonUtil.KEY_ATTACKER,context.getAttacker().getId());
            }
        }
    }
    private void LivingHurtEvent(LivingHurtEvent event) {

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            CompoundTag nbt = attacker.getPersistentData();
            ModDataNBT entitydata = ModDataNBT.readFromNBT(attacker.getPersistentData());
            Level world = attacker.level();
            double x = attacker.getX();
            double y = attacker.getY();
            double z = attacker.getZ();
            if (nbt.contains(CommonUtil.KEY_ATTACKER, Tag.TAG_INT)) {
                List<Mob> mobList = attacker.getCommandSenderWorld().getEntitiesOfClass(Mob.class, new AABB(x + 1, y + 1, z + 1, x - 1, y - 1, z - 1));
                for (Mob mob : mobList) {
                    if (mob != null) {
                        mob.invulnerableTime= 0;
                        mob.hurt(LegacyDamageSource.mobAttack((LivingEntity) world.getEntity(nbt.getInt(CommonUtil.KEY_ATTACKER))).setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setExplosion().setMsgId("thunderfire_damage"),(float) (entitydata.getInt(heatbombdamage)));
                        mob.setRemainingFireTicks((100));
                        mob.invulnerableTime= 0;

                    }}
                attacker.playSound(SoundEvents.GENERIC_EXPLODE,1,1);
                if (attacker.getCommandSenderWorld() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION,attacker.getX(),attacker.getY()+0.5*attacker.getBbHeight(),attacker.getZ(),1 ,0,0,0,0);
                }
                nbt.remove(CommonUtil.KEY_ATTACKER);
                entitydata.remove(heatbombdamage);
            }
        } else if (event.getSource().getEntity() instanceof Projectile projectile) {
            Entity attacker = projectile.getOwner();
            if (attacker instanceof LivingEntity) {
                CompoundTag nbt = attacker.getPersistentData();
                ModDataNBT entitydata = ModDataNBT.readFromNBT(attacker.getPersistentData());
                Level world = attacker.level();
                double x = attacker.getX();
                double y = attacker.getY();
                double z = attacker.getZ();
                if (nbt.contains(CommonUtil.KEY_ATTACKER, Tag.TAG_INT)) {
                    List<Mob> mobList = attacker.getCommandSenderWorld().getEntitiesOfClass(Mob.class, new AABB(x + 1, y + 1, z + 1, x - 1, y - 1, z - 1));
                    for (Mob mob : mobList) {
                        if (mob != null) {
                            mob.invulnerableTime= 0;
                            mob.hurt(LegacyDamageSource.mobAttack((LivingEntity) world.getEntity(nbt.getInt(CommonUtil.KEY_ATTACKER))).setBypassArmor().setBypassInvul().setBypassInvulnerableTime().setExplosion().setMsgId("thunderfire_damage"),(float) (entitydata.getInt(heatbombdamage)));
                            mob.setRemainingFireTicks((100));
                            mob.invulnerableTime= 0;

                        }}
                    attacker.playSound(SoundEvents.GENERIC_EXPLODE,1,1);
                    if (attacker.getCommandSenderWorld() instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(ParticleTypes.EXPLOSION,attacker.getX(),attacker.getY()+0.5*attacker.getBbHeight(),attacker.getZ(),1 ,0,0,0,0);
                    }
                    nbt.remove(CommonUtil.KEY_ATTACKER);
                    entitydata.remove(heatbombdamage);
                }
            }
        }

    }
}
