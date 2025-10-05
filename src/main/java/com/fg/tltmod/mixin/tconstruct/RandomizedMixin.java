package com.fg.tltmod.mixin.tconstruct;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.tconstruct.library.json.IntRange;

@Mixin(targets = "slimeknights.tconstruct.library.materials.RandomMaterial$Randomized",remap = false)
public class RandomizedMixin {
    @Mutable
    @Shadow @Final private IntRange tier;

    @Inject(method = "<init>",at = @At("RETURN"))
    public void resetTier(IntRange tier, boolean allowHidden, IJsonPredicate material, CallbackInfo ci){
        if (tier.max()>1){
            this.tier = new IntRange(0,0);
        }
    }
}
