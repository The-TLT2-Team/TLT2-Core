package com.fg.tltmod.data.providers;

import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.register.STTags;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TltCoreItemTagProvider extends ItemTagsProvider {
    public TltCoreItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, TltCore.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(STTags.Items.TINKER_CURIO).replace(false).add(
                TIItems.TINKERS_MEDAL.get()
        );
    }
}
