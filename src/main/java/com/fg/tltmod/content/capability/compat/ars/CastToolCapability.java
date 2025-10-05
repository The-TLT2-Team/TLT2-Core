package com.fg.tltmod.content.capability.compat.ars;

import com.fg.tltmod.SomeModifiers.integration.arsnouveau.CasterCapUtil;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.common.items.SpellParchment;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CastToolCapability implements ICasterTool {
    private final ItemStack stack;
    private final SpellCaster caster;
    public CastToolCapability(ItemStack itemStack) {
        this.stack = itemStack;
        this.caster = new SpellCaster(stack);
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public @NotNull ISpellCaster getSpellCaster(ItemStack stack) {
        return caster;
    }

    @Override
    public boolean isScribedSpellValid(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        return spell.recipe.stream().noneMatch(s -> s instanceof AbstractCastMethod);
    }

    @Override
    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
        recipe.add(MethodTouch.INSTANCE);
        recipe.addAll(spell.recipe);
        recipe.add(AugmentAmplify.INSTANCE);
        spell.recipe = recipe;
        caster.setSpell(spell);
        return true;
    }

    @Override
    public boolean onScribe(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack tableStack) {
        ItemStack heldStack = player.getItemInHand(handIn);
        ISpellCaster thisCaster = CasterCapUtil.getCaster(tableStack);
        if (!(heldStack.getItem() instanceof SpellBook) && !(heldStack.getItem() instanceof SpellParchment) && heldStack.getItem() != ItemsRegistry.MANIPULATION_ESSENCE.asItem()) {
            return false;
        } else {
            Spell spell = new Spell();
            if (heldStack.getItem() instanceof ICasterTool) {
                ISpellCaster heldCaster = CasterCapUtil.getCaster(heldStack);
                spell = heldCaster.getSpell();
                thisCaster.setColor(heldCaster.getColor());
                thisCaster.setFlavorText(heldCaster.getFlavorText());
                thisCaster.setSpellName(heldCaster.getSpellName());
                thisCaster.setSound(spell.sound);
            } else if (heldStack.getItem() == ItemsRegistry.MANIPULATION_ESSENCE.asItem()) {
                String[] words = new String[]{"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette"};
                int numWords = world.random.nextInt(3) + 3;
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < numWords; ++i) {
                    sb.append(words[world.random.nextInt(words.length)]).append(" ");
                }

                thisCaster.setSpellHidden(true);
                thisCaster.setHiddenRecipe(sb.toString());
                PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.spell_hidden"));
                return true;
            }

            if (this.isScribedSpellValid(thisCaster, player, handIn, tableStack, spell)) {
                boolean success = this.setSpell(thisCaster, player, handIn, tableStack, spell);
                if (success) {
                    this.sendSetMessage(player);
                    return true;
                }
            } else {
                this.sendInvalidMessage(player);
            }

            return false;
        }
    }

    public void sendSetMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.set_spell"));
    }

    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.invalid_spell"));
    }
}
