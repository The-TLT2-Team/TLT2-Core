package com.fg.tltmod.content.item;

import com.fg.tltmod.content.entity.IonizedArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IonizedArrowItem extends ArrowItem {
    public IonizedArrowItem() {
        super(new Properties());
    }

    @Override
    public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        var arrow = new IonizedArrowEntity(pLevel);
        arrow.setOwner(pShooter);
        arrow.setPos(pShooter.getX(),pShooter.getEyeY(),pShooter.getZ());
        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return true;
    }
}
