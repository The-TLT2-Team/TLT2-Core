package com.fg.tltmod.content.capability.compat.botania;

import com.fg.tltmod.TltCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.CapacityStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import vazkii.botania.api.mana.ManaItem;

public class ManaCurioCapability implements ManaItem {
    private final ItemStack stack;
    private static final String KEY_MANA = "mana";
    public static ResourceLocation MANA_KEY = TltCore.getResource(KEY_MANA);
    public static final String mana_FORMAT = TltCore.makeDescriptionId("tool_stat", "mana");
    public static final CapacityStat MAX_STAT = new CapacityStat(new ToolStatId(TltCore.MODID, "max_mana"), 10485760, mana_FORMAT);

    public ManaCurioCapability(ItemStack itemStack) {
        this.stack = itemStack;
    }

    @Override
    public int getMana() {
        ToolStack tool = ToolStack.from(stack);
        return getToolMana(tool);
    }

    @Override
    public int getMaxMana() {
        ToolStack tool = ToolStack.from(stack);
        return getToolMaxMana(tool);
    }

    @Override
    public void addMana(int amount) {
        ToolStack tool = ToolStack.from(stack);
        int current = getToolMana(tool);
        setToolMana(tool, current + amount);
    }

    public static int getToolMana(IToolStackView tool) {
        return tool.getPersistentData().getInt(MANA_KEY);
    }

    public static int getToolMaxMana(IToolStackView tool) {
        return tool.getStats().getInt(MAX_STAT);
    }

    private static void setManaRaw(IToolStackView tool, int mana) {
        if (mana == 0) {
            tool.getPersistentData().remove(MANA_KEY);
        } else {
            tool.getPersistentData().putInt(MANA_KEY, mana);
        }
    }

    public static void setToolMana(IToolStackView tool, int mana) {
        setManaRaw(tool, Mth.clamp(mana, 0, getToolMaxMana(tool)));
    }

    public static void checkMana(IToolStackView tool) {
        int mana = getToolMana(tool);
        if (mana < 0) {
            setManaRaw(tool, 0);
        } else {
            int capacity = getToolMaxMana(tool);
            if (mana > capacity) {
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

//
//    public static class Provider implements ToolCapabilityProvider.IToolCapabilityProvider {
//        private final LazyOptional<ManaCurioCapability> capa;
//
//        public Provider(Supplier<? extends IToolStackView> tool) {
//            this.capa = LazyOptional.of(() -> new ManaCurioCapability(tool));
//        }
//
//        @Override
//        public <T> @NotNull LazyOptional<T> getCapability(@NotNull IToolStackView tool, @NotNull Capability<T> cap) {
//            if (cap == BotaniaForgeCapabilities.MANA_ITEM
//                    && (tool instanceof ModifiableCurioItem || tool instanceof ModifiableCurio)
//                    && tool.getStats().getInt(MAX_STAT) > 0) {
//                return capa.cast();
//            }
//            return LazyOptional.empty();
//        }
//    }
}
