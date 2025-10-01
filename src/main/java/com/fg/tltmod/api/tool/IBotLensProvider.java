package com.fg.tltmod.api.tool;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
//比hook快捷的手段为你的词条添加透镜，如果需要复杂条件判断则还是得上hook
public interface IBotLensProvider {
    @NotNull Collection<ItemStack> getLens();
}
