package com.fg.tltmod.content.item;

import com.fg.tltmod.content.entity.NeutronArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NeutronArrowItem extends ArrowItem {
    public NeutronArrowItem() {
        super(new Properties());
    }
    public static final String KEY_RADIOACTIVE = "tltmod_radioactive";
    public static final String KEY_CHEMICAL_STACK = "tltmod_chemical_stack";

    @Override
    public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        NeutronArrowEntity entity = new NeutronArrowEntity(pLevel);
        entity.setOwner(pShooter);
        entity.setPos(pShooter.getX(),pShooter.getEyeY(),pShooter.getZ());
        if (pStack.getTag() != null) {
            entity.setRadioactivity(pStack.getTag().getFloat(KEY_RADIOACTIVE));
        }
        return entity;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return true;
    }
}
