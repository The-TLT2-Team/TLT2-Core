package com.fg.tltmod.mixin;

import com.fg.tltmod.util.mixin.IFoodPropertyMixin;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Supplier;

@Mixin(FoodProperties.class)
public class FoodPropertyMixin implements IFoodPropertyMixin {
    @Mutable
    @Shadow @Final private int nutrition;

    @Mutable
    @Shadow @Final private float saturationModifier;

    @Mutable
    @Shadow @Final private List<Pair<Supplier<MobEffectInstance>, Float>> effects;

    @Override
    public int tltmod$getNutrition() {
        return nutrition;
    }

    @Override
    public void tltmod$setNutrition(int i) {
        nutrition = i;
    }

    @Override
    public float tltmod$getSaturationModifier() {
        return saturationModifier;
    }

    @Override
    public void tltmod$setSaturationModifier(float f) {
        saturationModifier = f;
    }

    @Override
    public List<Pair<Supplier<MobEffectInstance>, Float>> tltmod$getEffects() {
        return effects;
    }

    @Override
    public void tltmod$setEffects(List<Pair<Supplier<MobEffectInstance>, Float>> list) {
        effects = list;
    }
}
