package com.fg.tltmod.SomeModifiers.rebalance;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.TltCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

//疾行削弱，倍率20->5，变为加乘增伤。
public class DashRebalance extends EtSTBaseModifier {
    public static final ResourceLocation KEY_ARROW_DASH = TltCore.getResource("arrow_dash");
    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        persistentData.putFloat(KEY_ARROW_DASH, (float) shooter.getDeltaMovement().length()*5*modifier.getLevel());
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        if (persistentData.contains(KEY_ARROW_DASH)) damage+=baseDamage*persistentData.getFloat(KEY_ARROW_DASH);
        return damage;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.isFullyCharged()){
            damage+= (float) (baseDamage*context.getAttacker().getDeltaMovement().length());
        }
        return damage;
    }
}
