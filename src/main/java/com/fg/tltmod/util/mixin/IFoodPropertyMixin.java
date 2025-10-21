package com.fg.tltmod.util.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.function.Supplier;

public interface IFoodPropertyMixin {
    int tltmod$getNutrition();
    void tltmod$setNutrition(int i);
    float tltmod$getSaturationModifier();
    void tltmod$setSaturationModifier(float f);
    List<Pair<Supplier<MobEffectInstance>, Float>> tltmod$getEffects();
    void tltmod$setEffects(List<Pair<Supplier<MobEffectInstance>, Float>> list);
}
