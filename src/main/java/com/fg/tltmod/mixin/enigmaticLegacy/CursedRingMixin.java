package com.fg.tltmod.mixin.enigmaticLegacy;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

@Mixin(value = CursedRing.class)
public abstract class CursedRingMixin extends ItemBaseCurio {
    @ModifyArg(method = "appendHoverText",at = @At(value = "INVOKE", target = "Lcom/aizistral/enigmaticlegacy/helpers/ItemLoreHelper;addLocalizedString(Ljava/util/List;Ljava/lang/String;)V",ordinal = 6),index = 1)
    public String replaceCurse10(String str){
        return "tooltip.tltmod.cursed_ring_replacement1";
    }
    @Redirect(method = "appendHoverText",at = @At(value = "INVOKE", target = "Lcom/aizistral/enigmaticlegacy/helpers/ItemLoreHelper;addLocalizedString(Ljava/util/List;Ljava/lang/String;Lnet/minecraft/ChatFormatting;[Ljava/lang/Object;)V",ordinal = 6))
    public void replaceCurse15(List<Component> comp, String s, ChatFormatting value, Object[] list){
        ItemLoreHelper.addLocalizedString(comp, "tooltip.tltmod.cursed_ring_replacement2");
    }
    @Redirect(method = "appendHoverText",at = @At(value = "INVOKE", target = "Lcom/aizistral/enigmaticlegacy/helpers/ItemLoreHelper;addLocalizedString(Ljava/util/List;Ljava/lang/String;)V",ordinal = 3))
    public void replaceCurse5(List<Component> list, String str){
        ItemLoreHelper.addLocalizedString(list, "tooltip.tltmod.cursed_ring_replacement3");
    }
    @Inject(method = "curioTick",at = @At("HEAD"),cancellable = true,remap = false)
    public void stopSettingTarget(SlotContext context, ItemStack stack, CallbackInfo ci){
        ci.cancel();
    }
}
