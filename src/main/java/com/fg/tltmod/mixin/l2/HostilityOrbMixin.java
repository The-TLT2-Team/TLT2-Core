package com.fg.tltmod.mixin.l2;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import dev.xkmc.l2hostility.content.item.consumable.HostilityOrb;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = HostilityOrb.class)
public class HostilityOrbMixin {
    @Inject(method = "use",at = @At("HEAD"),cancellable = true)
    public void limitForCurse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        if (player instanceof FakePlayer) cir.setReturnValue(InteractionResultHolder.fail(player.getItemInHand(hand)));
        if (!level.isClientSide&&SuperpositionHandler.isTheCursedOne(player)){
            ServerPlayer serverPlayer = (ServerPlayer) player;
            var statistic = serverPlayer.getStats();
            int count = statistic.getValue(Stats.ITEM_USED.get(LHItems.HOSTILITY_ORB.get()));
            if (count>=2){
                cir.setReturnValue(InteractionResultHolder.fail(player.getItemInHand(hand)));
                player.sendSystemMessage(Component.translatable("tooltip.tltmod.no_orbs_left").withStyle(ChatFormatting.DARK_RED));
            } else serverPlayer.awardStat(Stats.ITEM_USED.get(LHItems.HOSTILITY_ORB.get()),1);
        }
    }
}
