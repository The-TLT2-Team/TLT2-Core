package com.fg.tltmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.world.entity.monster.Slime$SlimeMoveControl")
public interface SlimeMoveControlInvoker {
    @Invoker("setDirection")
    void setDirection(float pYRot, boolean pAggressive);
}
