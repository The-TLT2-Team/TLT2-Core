package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class RageOfSparta extends EtSTBaseModifier {
    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity living = context.getAttacker();
        return damage*(1+modifier.getLevel()*0.5f*(living.getMaxHealth()-living.getHealth())/living.getMaxHealth());
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        if (attacker!=null){
            damage*=1+entry.getLevel()*0.5f*(attacker.getMaxHealth()-attacker.getHealth())/attacker.getMaxHealth();
        }
        return damage;
    }
}
