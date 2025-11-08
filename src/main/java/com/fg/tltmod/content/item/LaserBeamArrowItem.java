package com.fg.tltmod.content.item;

import com.fg.tltmod.content.entity.LaserBeamArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LaserBeamArrowItem extends ArrowItem {
    public LaserBeamArrowItem() {
        super(new Properties());
    }

    @Override
    public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        LaserBeamArrowEntity entity = new LaserBeamArrowEntity(pLevel);
        entity.setOwner(pShooter);
        entity.setPos(pShooter.getX(),pShooter.getEyeY(),pShooter.getZ());
        return entity;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return true;
    }
}
