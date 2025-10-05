package com.fg.tltmod.mixin.alexscaves;

import com.github.alexmodguy.alexscaves.server.event.CommonEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommonEvents.class, remap = false)
public abstract class CommonEventMixin {
    @Inject(
            method = "checkAndDestroyExploitItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void disableCheck(Player player, EquipmentSlot slot, CallbackInfo ci) {
        ci.cancel();
    }
}
