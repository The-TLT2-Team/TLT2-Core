package com.fg.tltmod.SomeModifiers.ranged;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration.ToolBasicChemicalTankProvider;
import com.fg.tltmod.Register.TltCoreItems;
import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.SomeModifiers.ranged.base.SpecializedRangedModifier;
import com.fg.tltmod.content.entity.NeutronArrowEntity;
import com.fg.tltmod.content.item.NeutronArrowItem;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

import static com.fg.tltmod.util.TltcoreConstants.NbtLocations.KEY_NOT_PRIMARY;

public class NeutronArrowModifier extends SpecializedRangedModifier implements BowAmmoModifierHook, CustomBarDisplayModifierHook {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ModifierTraitModule(TltCoreModifiers.CHEMICAL_STORAGE.getId(), 10,false));
        hookBuilder.addHook(this, ModifierHooks.BOW_AMMO, EtSTLibHooks.CUSTOM_BAR);
    }

    @Override
    public String barId(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return "tltmod_radioactive";
    }

    @Override
    public boolean showBar(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return getRadioactive(iToolStackView)>0;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        var total = iToolStackView.getStats().get(EtSTLibToolStat.CHEMICAL_TANK_CAPACITY).longValue();
        var count = getRadioactive(iToolStackView);
        return new Vec2(Math.min(13.0F, 13.0F * (float) count / (float) total), 1.0F);
    }

    @Override
    public int getBarRGB(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return 0xFFBBFF91;
    }

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo, Predicate<ItemStack> ammoPredicate) {
        return swapArrowIfPossible(tool,standardAmmo);
    }

    @Override
    public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
        if (!shrinkRadioactive(tool,needed,ammo)) BowAmmoModifierHook.super.shrinkAmmo(tool,modifier,shooter,ammo,needed);
    }

    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (projectile instanceof NeutronArrowEntity&&!primary) persistentData.putBoolean(KEY_NOT_PRIMARY,true);
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        if (persistentData.getBoolean(KEY_NOT_PRIMARY)) return damage*0.2f;
        return damage;
    }

    public static long getRadioactive(IToolStackView tool){
        if (((ToolStack)tool).createStack().getCapability(Capabilities.GAS_HANDLER).orElse(null) instanceof ToolBasicChemicalTankProvider.Gas gasHandler){
            return gasHandler.findChemical(gasStack->gasStack.has(GasAttributes.Radiation.class)&&gasStack.getAmount()>=10).getAmount();
        }
        return 0;
    }
    public static ItemStack swapArrowIfPossible(IToolStackView tool,ItemStack original){
        if (((ToolStack)tool).createStack().getCapability(Capabilities.GAS_HANDLER).orElse(null) instanceof ToolBasicChemicalTankProvider.Gas gasHandler){
            var gasStack = gasHandler.findChemical(gs->gs.has(GasAttributes.Radiation.class)&&gs.getAmount()>=20);
            if (!gasStack.isEmpty()) {
                int count = (int) Math.min(gasHandler.extractChemical(gasStack, Action.SIMULATE).getAmount() / 20L, Integer.MAX_VALUE);
                GasStack result = gasStack.copy();
                result.setAmount(20L *count);
                result = gasHandler.extractChemical(result,Action.SIMULATE);
                ItemStack arrowStack = new ItemStack(TltCoreItems.NEUTRON_ARROW.get());
                arrowStack.setCount(count);
                var nbt = new CompoundTag();
                result.ifAttributePresent(GasAttributes.Radiation.class, radiation -> nbt.putFloat(NeutronArrowItem.KEY_RADIOACTIVE, (float) radiation.getRadioactivity()));
                CompoundTag tag = new CompoundTag();
                result.write(tag);
                nbt.put(NeutronArrowItem.KEY_CHEMICAL_STACK,tag);
                arrowStack.setTag(nbt);
                return arrowStack;
            }
        }
        return original;
    }
    public static boolean shrinkRadioactive(IToolStackView tool,int needed,ItemStack ammo){
        if (!ammo.is(TltCoreItems.NEUTRON_ARROW.get())) return false;
        var nbt = ammo.getTag();
        if (nbt==null)return false;
        CompoundTag chemicalNbt = nbt.getCompound(NeutronArrowItem.KEY_CHEMICAL_STACK);
        if (((ToolStack)tool).createStack().getCapability(Capabilities.GAS_HANDLER).orElse(null) instanceof ToolBasicChemicalTankProvider.Gas gasHandler) {
            var toExtract = GasStack.readFromNBT(chemicalNbt);
            toExtract.setAmount(needed*20L);
            gasHandler.extractChemical(toExtract,Action.EXECUTE);
            return true;
        }
        return false;
    }
}
