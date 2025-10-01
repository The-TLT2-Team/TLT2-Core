package com.fg.tltmod.mixin.sophisticated;

import com.fg.tltmod.Config;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FeedingUpgradeWrapper.class, remap = false)
public class FeedingUpgradeWrapperMixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(intValue = 10)
    )
    private int modifyStillHungryCooldown(int original) {
        return Config.getFeedingCooldown();
    }

    @ModifyConstant(
            method = "feedPlayerAndGetHungry",
            constant = @Constant(intValue = 20)
    )
    private int modifyMaxHunger(int original) {
        return Config.getMaxHunger();
    }
}
