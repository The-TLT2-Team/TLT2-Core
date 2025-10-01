package com.fg.tltmod.data.providers.tinker;

import com.fg.tltmod.data.TltCoreModifierIds;
import com.fg.tltmod.data.base.BaseRecipeProvider;
import com.ssakura49.sakuratinker.register.STTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

public class TltCoreModifierRecipeProvider extends BaseRecipeProvider {
    public TltCoreModifierRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public @NotNull String getName() {
        return "Tlt Core Modifier Recipes";
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addModifierRecipe(consumer);
    }

    private void addModifierRecipe(Consumer<FinishedRecipe> consumer) {
        // modifiers
        String upgradeFolder = "tools/modifiers/upgrade/";
        String abilityFolder = "tools/modifiers/ability/";
        String slotlessFolder = "tools/modifiers/slotless/";
        String defenseFolder = "tools/modifiers/defense/";
        String compatFolder = "tools/modifiers/compat/";
        String worktableFolder = "tools/modifiers/worktable/";
        // salvage
        String salvageFolder = "tools/modifiers/salvage/";
        String upgradeSalvage = salvageFolder + "upgrade/";
        String abilitySalvage = salvageFolder + "ability/";
        String defenseSalvage = salvageFolder + "defense/";
        String compatSalvage = salvageFolder + "compat/";
        IncrementalModifierRecipeBuilder.modifier(TltCoreModifierIds.manaRefactor)
                .setTools(ingredientFromTags(STTags.Items.TINKER_CURIO))
                .setInput(BotaniaItems.manaSteel, 1, 2)
                .setInput(BotaniaItems.elementium, 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(consumer, prefix(TltCoreModifierIds.manaRefactor, upgradeSalvage))
                .save(consumer, prefix(TltCoreModifierIds.manaRefactor, upgradeFolder));
    }

    @SafeVarargs
    private static Ingredient ingredientFromTags(TagKey<Item>... tags) {
        Ingredient[] tagIngredients = new Ingredient[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagIngredients[i] = Ingredient.of(tags[i]);
        }
        return CompoundIngredient.of(tagIngredients);
    }
}
