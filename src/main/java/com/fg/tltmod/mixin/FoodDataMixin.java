package com.fg.tltmod.mixin;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fg.tltmod.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = {FoodData.class}, priority = 1001)
public abstract class FoodDataMixin {
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Unique
    private LivingEntity tltmod$playerTmp = null;

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
    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",at = @At("HEAD"),remap = false)
    private void storePlayer(Item pItem, ItemStack pStack, LivingEntity entity, CallbackInfo ci){
        tltmod$playerTmp = entity;
    }

    @ModifyVariable(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",at = @At("STORE"),remap = false)
    private FoodProperties modifyFoodProperty(FoodProperties value){
        if (tltmod$playerTmp instanceof Player player&& SuperpositionHandler.isTheCursedOne(player)){
            float sat = value.getSaturationModifier()/2;
            int nu =Math.max(1, value.getNutrition()/2);
            var builder = new FoodProperties.Builder().nutrition(nu).saturationMod(sat);
            if (value.canAlwaysEat()) builder.alwaysEat();
            value = builder.build();
            tltmod$playerTmp = null;
        }
        return value;
    }


    @Inject(method = "needsFood", at = @At("HEAD"), cancellable = true)
    private void needsFood(CallbackInfoReturnable<Boolean> cir) {
        if (Config.isPlayerHungerEnabled()) {
            int maxHunger = Config.getMaxHunger();
            cir.setReturnValue(this.foodLevel < maxHunger);
        }
    }
}

