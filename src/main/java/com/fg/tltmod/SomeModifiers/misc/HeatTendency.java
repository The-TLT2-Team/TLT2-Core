package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class HeatTendency extends EtSTBaseModifier {
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide&&isCorrectSlot){
            if (world.getGameTime()%20==0){
                holder.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,100,0,false,false));
            }
            if (tool.getDamage()>0&&(holder.isOnFire()||holder.isInLava())) {
                if (RANDOM.nextInt(10) < modifier.getLevel()) {
                    tool.setDamage(tool.getDamage()-1);
                }
            }
        }
    }
}
