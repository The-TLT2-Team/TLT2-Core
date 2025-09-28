package com.fg.tltmod.mixin;

import com.fg.tltmod.Config;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {FoodData.class}, priority = 1001)
public abstract class FoodDataMixin {
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;

    public FoodDataMixin() {
    }

    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    private void eat(int hunger, float saturation, CallbackInfo ci) {
        if (Config.isPlayerHungerEnabled()) {
            int maxHunger = Config.getMaxHunger();
            this.foodLevel = Math.min(hunger + this.foodLevel, maxHunger);
            this.saturationLevel = Math.min(this.saturationLevel + hunger * saturation * 2.0F, (float)this.foodLevel);
            ci.cancel();
        }
    }

    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    private void needsFood(CallbackInfoReturnable<Boolean> cir) {
        if (Config.isPlayerHungerEnabled()) {
            int maxHunger = Config.getMaxHunger();
            cir.setReturnValue(this.foodLevel < maxHunger);
        }
    }
}

