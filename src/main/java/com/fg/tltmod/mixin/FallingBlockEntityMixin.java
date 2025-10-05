package com.fg.tltmod.mixin;

import com.fg.tltmod.util.mixin.IFallingBlockEntityMixin;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin implements IFallingBlockEntityMixin {

    @Shadow private BlockState blockState;

    @Override
    public void tltmod$setBlockState(BlockState state) {
        this.blockState = state;
    }
}
