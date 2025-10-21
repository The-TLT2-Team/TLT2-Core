package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class DaysAndNights extends EtSTBaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.isFullyCharged()){
            var target = context.getTarget();
            var attacker = context.getAttacker();
            if (target.isOnFire()) {
                target.hurt(LegacyDamageSource.any(attacker.damageSources().freeze().typeHolder(),attacker)
                        .setBypassArmor().setBypassInvulnerableTime(),damageDealt*0.33f*modifier.getLevel());
                target.setSecondsOnFire(0);
                target.setTicksFrozen(262144);
            } else if (target.getTicksFrozen()>0){
                target.hurt(LegacyDamageSource.any(attacker.damageSources().inFire().typeHolder(),attacker)
                        .setBypassEnchantment().setBypassMagic().setBypassInvulnerableTime(),damageDealt*0.33f*modifier.getLevel());
                target.setTicksFrozen(0);
                target.setSecondsOnFire(262144);
            } else {
                target.setTicksFrozen(262144);
            }
            target.hurt(LegacyDamageSource.any(attacker.damageSources().explosion(attacker,attacker))
                            .setBypassInvulnerableTime().setMsgId(RANDOM.nextBoolean()?"explosion":"tltmod.explosion"),
                    damageDealt*0.33f*modifier.getLevel());
        }
    }
    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        if (attacker!=null){
            if (target.isOnFire()) {
                target.hurt(LegacyDamageSource.any(attacker.damageSources().freeze().typeHolder(),attacker)
                        .setBypassArmor().setBypassInvulnerableTime(),damageDealt*0.33f*entry.getLevel());
                target.setSecondsOnFire(0);
                target.setTicksFrozen(262144);
            } else if (target.getTicksFrozen()>0){
                target.hurt(LegacyDamageSource.any(attacker.damageSources().inFire().typeHolder(),attacker)
                        .setBypassEnchantment().setBypassMagic().setBypassInvulnerableTime(),damageDealt*0.33f*entry.getLevel());
                target.setTicksFrozen(0);
                target.setSecondsOnFire(262144);
            } else {
                target.setTicksFrozen(262144);
            }
            target.hurt(LegacyDamageSource.any(attacker.damageSources().explosion(attacker,attacker))
                            .setBypassInvulnerableTime().setMsgId(RANDOM.nextBoolean()?"explosion":"tltmod.explosion"),
                    damageDealt*0.33f*entry.getLevel());
        }
    }
}
