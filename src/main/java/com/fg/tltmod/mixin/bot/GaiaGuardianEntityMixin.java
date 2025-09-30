package com.fg.tltmod.mixin.bot;

import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.common.entity.GaiaGuardianEntity;

@Mixin(value = GaiaGuardianEntity.class,remap = false)
public abstract class GaiaGuardianEntityMixin implements Enemy {
}
