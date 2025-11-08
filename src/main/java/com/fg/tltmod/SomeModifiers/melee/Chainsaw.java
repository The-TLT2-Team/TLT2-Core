package com.fg.tltmod.SomeModifiers.melee;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.AttackUtil;
import com.c2h6s.etstlib.util.ToolEnergyUtil;
import com.fg.tltmod.TltCore;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.UUID;

public class Chainsaw extends EtSTBaseModifier {
    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return context.isExtraAttack()?damage:damage*0.25f;
    }

    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        if (!isExtraAttack) return source;
        var damageSource = LegacyDamageSource.any(RANDOM.nextBoolean()?CMDamageTypes.causeDeathLaserDamage(attacker,attacker):CMDamageTypes.causeLaserDamage(attacker,attacker));
        damageSource.damageTypes = source.damageTypes;
        return damageSource;
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (!context.isExtraAttack()&&context.isFullyCharged()&&damage>0){
            ChainsawAttackSchedule.addSchedule(tool,modifier,context,damage);
        }
        return knockback*0.25f;
    }

    @Mod.EventBusSubscriber(modid = TltCore.MODID)
    public static class ChainsawAttackSchedule{
        @SubscribeEvent
        public static void onLevelTick(TickEvent.ServerTickEvent event){
            if (scheduleList!=null&&!scheduleList.isEmpty()){
                scheduleList =new ArrayList<>(scheduleList.stream().filter(entry::validate).toList());
                scheduleList.forEach(entry::tick);
            }
        }
        public static ArrayList<entry> scheduleList = new ArrayList<>();
        public static void addSchedule(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage){
            scheduleList.add(entry.withAutoCount(tool,modifier,context,damage));
        }
        public static class entry{
            int tick;
            final int spacing;
            final IToolStackView tool;
            final ModifierEntry modifier;
            final ToolAttackContext context;
            final float damage;
            final UUID uuid;

            public entry(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, UUID uuid, int tick, int spacing) {
                this.tool = tool;
                this.modifier = modifier;
                this.context = context;
                this.damage = damage;
                this.uuid = uuid;
                this.tick = tick;
                this.spacing = spacing;
            }

            public static entry withRandomUuid(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage,
                         int count,int spacing){
                return new entry(tool, modifier, context, damage, UUID.randomUUID(), count*spacing,spacing);
            }
            public static entry withAutoCount(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage){
                return withRandomUuid(tool,modifier,context,damage,modifier.getLevel()+4,2);
            }
            public boolean validate(){
                return context.getAttacker().isAlive()&&context.getTarget().isAlive()&&tool!=null&&tick>0;
            }
            public void tick(){
                if (tick%spacing==0) {
                    try {
                        if (ToolEnergyUtil.extractEnergy(tool,250,true)>=250) {
                            context.getTarget().invulnerableTime = 0;
                            AttackUtil.attackEntity(tool, context.getAttacker(), context.getHand(), context.getTarget(), context::getCooldown, true, context.getSlotType(), true, damage, true);
                            ToolEnergyUtil.extractEnergy(tool,250,false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                tick--;
            }
        }
    }
}
