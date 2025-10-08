package com.fg.tltmod.content.hook.modifier;

import com.fg.tltmod.api.tool.IBotLensProvider;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.fg.tltmod.util.mixin.IToolProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.common.item.lens.Lens;
import vazkii.botania.common.item.lens.LensItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ModifyBurstModifierHook {
    default void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens){}

    static void handleBurstCreation(ManaBurst burst, ItemStack stack,IToolStackView tool){
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(TltCoreModifierHook.MODIFY_BURST).modifyBurst(tool,entry,tool.getModifierList(),burst.entity().getOwner(),burst,(IManaBurstMixin) burst,toolStack));
        BurstProperties burstProperties = new BurstProperties(burst.getMana(),burst.getMinManaLoss(),burst.getManaLossPerTick(),burst.getBurstGravity(),1,burst.getColor());
        List<ItemStack> list = LensProviderModifierHook.gatherLens(tool,burst);
        list.forEach(lensStack ->{
            if (lensStack.getItem() instanceof LensItem lens) lens.apply(lensStack,burstProperties,burst.entity().level());
        });
        burst.setMana(burstProperties.maxMana);
        burst.setColor(burstProperties.color);
        burst.setMinManaLoss(burstProperties.ticksBeforeManaLoss);
        burst.setManaLossPerTick(burstProperties.manaLossPerTick);
        burst.setGravity(burstProperties.gravity);
        burst.entity().setDeltaMovement(burst.entity().getDeltaMovement().scale(burstProperties.motionModifier));
    }

    record AllMerger(Collection<ModifyBurstModifierHook> modules) implements ModifyBurstModifierHook {
        @Override
        public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
            this.modules.forEach(hook->hook.modifyBurst(tool,modifier,modifierList,owner,burst,burstExtras,dummyLens));
        }
    }
}
