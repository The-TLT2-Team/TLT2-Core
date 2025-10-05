package com.fg.tltmod.SomeModifiers.integration.arsnouveau;

import com.fg.tltmod.content.capability.compat.ars.CastToolCapabilitiesProvider;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CasterCapUtil {
    public static ISpellCaster getCaster(ItemStack stack) {
        return stack.getCapability(CastToolCapabilitiesProvider.CASTER_TOOL)
                .map(ICasterTool::getSpellCaster)
                .orElseGet(() -> {
                    Item item = stack.getItem();
                    if (item instanceof ICasterTool casterTool) {
                        return casterTool.getSpellCaster(stack);
                    }
                    return new SpellCaster(stack);
                });
    }
}
