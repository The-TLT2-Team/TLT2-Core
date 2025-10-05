package com.fg.tltmod.content.capability.compat.ars;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.TltCore;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableCurioItem;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CastToolCapabilitiesProvider {
    public static final Capability<ICasterTool> CASTER_TOOL = CapabilityManager.get(new CapabilityToken<>() {});
    private static final ResourceLocation ID = TltCore.getResource("caster_tool");

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, RegisterCapabilitiesEvent.class, CastToolCapabilitiesProvider::register);
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CastToolCapabilitiesProvider::attachCapability);
    }

    private static void register(RegisterCapabilitiesEvent event) {
        event.register(CastToolCapability.class);
    }

    private static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (ToolUtil.checkTool(stack)) {
            ToolStack toolStack = ToolStack.from(stack);
            if (!toolStack.hasTag(TinkerTags.Items.ARMOR) && toolStack.getModifierLevel(TltCoreModifiers.CASTER_TOOL.get()) > 0) {
                CastToolCapabilitiesProvider.Provider provider = new CastToolCapabilitiesProvider.Provider(stack);
                event.addCapability(ID, provider);
                event.addListener(provider);
            }
        }
    }

    private static class Provider implements ICapabilityProvider, Runnable {
        private final ItemStack stack;
        private LazyOptional<ICasterTool> casterTool;

        public Provider(ItemStack stack) {
            this.stack = stack;
            this.casterTool = LazyOptional.of(() -> new CastToolCapability(stack));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CASTER_TOOL.orEmpty(cap, casterTool);
        }

        @Override
        public void run() {
            ICasterTool old = casterTool.orElse(new CastToolCapability(stack));
            casterTool.invalidate();
            casterTool = LazyOptional.of(() -> old);
        }
    }
}
