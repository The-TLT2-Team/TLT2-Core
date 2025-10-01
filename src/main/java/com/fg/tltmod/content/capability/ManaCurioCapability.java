package com.fg.tltmod.content.capability;

import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.common.tools.capability.ForgeEnergyCapability;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableCurioItem;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.CapacityStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaItem;

import java.util.function.Supplier;

public record ManaCurioCapability(Supplier<? extends IToolStackView> tool) implements ManaItem {

    private static final String KEY_MANA = "mana";
    public static ResourceLocation MANA_KEY = TltCore.getResource(KEY_MANA);
    public static final String ENERGY_FORMAT = TltCore.makeDescriptionId("tool_stat", "mana");
    public static final CapacityStat MAX_STAT = new CapacityStat(new ToolStatId(TltCore.MODID, "max_mana"), 10485760, ENERGY_FORMAT);

    public ManaCurioCapability(Supplier<? extends IToolStackView> tool) {
        this.tool = tool;
    }

    @Override
    public int getMana() {
        return tool.get().getPersistentData().getInt(MANA_KEY);
    }

    @Override
    public int getMaxMana() {
        return tool.get().getStats().getInt(MAX_STAT);
    }

    @Override
    public void addMana(int amount) {
        setToolMana(tool.get(), amount);
    }

    public static int getToolMana(IToolStackView tool) {
        return tool.getPersistentData().getInt(MANA_KEY);
    }

    public static int getToolMaxMana(IToolStackView tool) {
        return tool.getStats().getInt(MAX_STAT);
    }

    private static void setManaRaw(IToolStackView tool, int energy) {
        if (energy == 0) {
            tool.getPersistentData().remove(MANA_KEY);
        } else {
            tool.getPersistentData().putInt(MANA_KEY, energy);
        }
    }

    public static void setToolMana(IToolStackView tool, int energy) {
        setManaRaw(tool, Mth.clamp(energy, 0, getToolMaxMana(tool)));
    }

    public static void checkMana(IToolStackView tool) {
        int energy = getToolMana(tool);
        if (energy < 0) {
            setManaRaw(tool, 0);
        } else {
            int capacity = getToolMaxMana(tool);
            if (energy > capacity) {
                setManaRaw(tool, capacity);
            }
        }

    }

    @Override
    public boolean canReceiveManaFromPool(BlockEntity pool) {
        return true;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canExportManaToPool(BlockEntity pool) {
        return true;
    }

    @Override
    public boolean canExportManaToItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isNoExport() {
        return false;
    }

    @Override
    public Supplier<? extends IToolStackView> tool() {
        return this.tool;
    }

    public static class Provider implements ToolCapabilityProvider.IToolCapabilityProvider {
        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull IToolStackView tool, @NotNull Capability<T> cap) {
            if (cap == BotaniaForgeCapabilities.MANA_ITEM
                    && (tool instanceof ModifiableCurioItem || tool instanceof ModifiableCurio)
                    && tool.getModifierLevel(TltCoreModifiers.MANA_REFACTOR.get()) > 0) {
                return LazyOptional.of(() -> new ManaCurioCapability(() -> tool)).cast();
            }
            return LazyOptional.empty();
        }
    }
}
