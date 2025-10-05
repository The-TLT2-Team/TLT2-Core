package com.fg.tltmod.content.capability.compat.botania;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableCurioItem;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaCurioCapabilityProvider {
    private ManaCurioCapabilityProvider() {}
    private static final ResourceLocation ID = TltCore.getResource("mana_item");

    public static void register() {
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, ManaCurioCapabilityProvider::attachCapability);
    }

    private static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (ToolUtil.checkTool(stack)) {
            ToolStack toolStack = ToolStack.from(stack);
            if ((stack.getItem() instanceof ModifiableCurioItem || stack.getItem() instanceof ModifiableCurio) && toolStack.getModifierLevel(TltCoreModifiers.MANA_REFACTOR.get()) > 0) {
                Provider provider = new Provider(stack);
                event.addCapability(ID, provider);
                event.addListener(provider);
            }
        }
    }

    private static class Provider implements ICapabilityProvider, Runnable {
        private final ItemStack stack;
        private LazyOptional<ManaItem> mana;

        public Provider(ItemStack stack) {
            this.stack = stack;
            this.mana = LazyOptional.of(() -> new ManaCurioCapability(stack));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return BotaniaForgeCapabilities.MANA_ITEM.orEmpty(cap, mana);
        }

        @Override
        public void run() {
            ManaItem old = mana.orElse(new ManaCurioCapability(stack));
            mana.invalidate();
            mana = LazyOptional.of(() -> old);
        }
    }
}
