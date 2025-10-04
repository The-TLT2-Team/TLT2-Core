package com.fg.tltmod.mixin;

import com.fg.tltmod.content.event.ForgeEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TheEndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    @Inject(method = "teleportEntity",at = @At("HEAD"))
    private static void processBlock(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity, TheEndGatewayBlockEntity pBlockEntity, CallbackInfo ci){
        if (pEntity instanceof FallingBlockEntity entity){
            ForgeEventHandler.resetFallenBlock(entity);
        }
    }
}
