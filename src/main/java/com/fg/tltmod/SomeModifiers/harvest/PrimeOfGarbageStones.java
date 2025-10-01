package com.fg.tltmod.SomeModifiers.harvest;

import com.fg.tltmod.util.TltmodCommonUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class PrimeOfGarbageStones extends NoLevelsModifier implements ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }

    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        ArrayList<ItemStack> list = new ArrayList<>(generatedLoot);
        for (int i=0;i<list.size();i++){
            ItemStack stack = list.get(i);
            if (stack.is(Tags.Items.STONE)&&!stack.is(Items.STONE)&&!stack.is(Items.DEEPSLATE)){
                if (RANDOM.nextInt(20)<modifier.getLevel()) list.set(i, TltmodCommonUtil.parseItemStack(ResourceLocation.parse("create:andesite_alloy"),stack.getCount()));
                else list.set(i,ItemStack.EMPTY);
            }
        }
        generatedLoot.clear();
        generatedLoot.addAll(list.stream().filter(stack -> !stack.isEmpty()).toList());
    }
}
