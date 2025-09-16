package com.fg.tltmod.mixin;

import com.fg.tltmod.util.TltmodCommonUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "canEnchant",at = @At("RETURN"),cancellable = true)
    public void cannotEnchant(ItemStack pStack, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValueZ()) cir.setReturnValue(TltmodCommonUtil.canEnchant((Enchantment) (Object) this));
    }
    @Inject(method = "canApplyAtEnchantingTable",at = @At("RETURN"),cancellable = true,remap = false)
    public void cannotApply(ItemStack pStack, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValueZ()) cir.setReturnValue(TltmodCommonUtil.canEnchant((Enchantment) (Object) this));
    }
    @Inject(method = "isAllowedOnBooks",at = @At("RETURN"),cancellable = true,remap = false)
    public void notAllowedOnBooks(CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValueZ()) cir.setReturnValue(TltmodCommonUtil.canEnchant((Enchantment) (Object) this));
    }
    @Inject(method = "isDiscoverable",at = @At("RETURN"),cancellable = true)
    public void notDiscoverable(CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValueZ()) cir.setReturnValue(TltmodCommonUtil.canEnchant((Enchantment) (Object) this));
    }
    @Inject(method = "isTradeable",at = @At("RETURN"),cancellable = true)
    public void notTradeable(CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValueZ()) cir.setReturnValue(TltmodCommonUtil.canEnchant((Enchantment) (Object) this));
    }
}
