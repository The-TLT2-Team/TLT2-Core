package com.fg.tltmod.data.providers;

import com.fg.tltmod.Register.TltCoreHostilityTrait;
import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.register.STTags;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import dev.xkmc.l2hostility.init.data.LHTagGen;
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
        this.tag(LHTagGen.TRAIT_ITEM).replace(false).add(
                TltCoreHostilityTrait.defense.get().asItem(),
                TltCoreHostilityTrait.furious.get().asItem(),
                TltCoreHostilityTrait.evil_karma.get().asItem(),
                TltCoreHostilityTrait.bloody_battle.get().asItem(),
                TltCoreHostilityTrait.transfer.get().asItem(),
                TltCoreHostilityTrait.thud.get().asItem(),
                TltCoreHostilityTrait.contaminated_blood.get().asItem(),
                TltCoreHostilityTrait.blood_debt.get().asItem(),
                TltCoreHostilityTrait.titan_bloodline.get().asItem(),
                TltCoreHostilityTrait.show_sword.get().asItem(),
                TltCoreHostilityTrait.devouring_life.get().asItem(),
                TltCoreHostilityTrait.harvest_sharing.get().asItem(),
                TltCoreHostilityTrait.critical_hit.get().asItem(),
                TltCoreHostilityTrait.vector_stance.get().asItem(),
                TltCoreHostilityTrait.absorb_mana.get().asItem(),
                TltCoreHostilityTrait.magala_erode.get().asItem(),
                TltCoreHostilityTrait.broken_armor.get().asItem(),
                TltCoreHostilityTrait.devouring_electricity.get().asItem()
        );
    }
}
