package com.fg.tltmod.data.providers;

import com.fg.tltmod.TltCore;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TltCoreBlockStateProvider extends BlockStateProvider {
    public TltCoreBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TltCore.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }

    private void addSimpleBlock(Block block){
        ModelFile file = cubeAll(block);
        simpleBlock(block,file);
    }

}
