package com.fg.tltmod.mixin.l2;

import com.fg.tltmod.util.TltcoreConstants;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RagnarokTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(value = RagnarokTrait.class,remap = false)
public abstract class RagnarokMixin extends LegendaryTrait {
    public RagnarokMixin(ChatFormatting format) {
        super(format);
    }

    @Inject(method = "allowSeal",at = @At("HEAD"),cancellable = true)
    private static void disallowSeal(EntitySlotAccess access, CallbackInfoReturnable<Boolean> cir){
        ItemStack stack = access.get();
        if (stack.getItem() instanceof IModifiable){
            ToolStack tool = ToolStack.from(stack);
            if (tool.getVolatileData().getBoolean(TltcoreConstants.NbtLocations.KEY_ANTI_RAGNAROK)) cir.setReturnValue(false);
        }
    }

    @Inject(method = "postHurtImpl",at = @At("HEAD"),cancellable = true)
    public void totalDisallowSeal(int level, LivingEntity attacker, LivingEntity target, CallbackInfo ci){
        target.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap-> {
            if (cap.get(TltcoreConstants.TinkerDataKeys.KEY_TOTAL_ANTI_RAGNAROK,false)) {
                ci.cancel();
            }
        });
    }
}
