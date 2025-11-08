package com.fg.tltmod.SomeModifiers.integration.mekanism;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.register.EtSTLibToolStat;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.tool.modifiers.capabilityProvider.MekIntegration.ToolBasicChemicalTankProvider;
import com.fg.tltmod.Register.TltCoreModifiers;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registries.MekanismDamageTypes;
import net.minecraft.world.phys.Vec2;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class GammaRay extends EtSTBaseModifier implements CustomBarDisplayModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.CUSTOM_BAR);
        hookBuilder.addModule(new ModifierTraitModule(TltCoreModifiers.CHEMICAL_STORAGE.getId(), 10,false));
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage*(1+shrinkRadioactive(tool,20*modifier.getLevel()));
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        float rad = shrinkRadioactive(tool,10*modifier.getLevel());
        if (rad>0){
            context.getTarget().hurt(LegacyDamageSource.any(MekanismDamageTypes.RADIATION.source(context.getLevel())).setBypassArmor().setBypassInvulnerableTime(),damageDealt*rad);
            context.getTarget().invulnerableTime=0;
        }
    }

    public static float shrinkRadioactive(IToolStackView tool, int needed){
        if (((ToolStack)tool).createStack().getCapability(Capabilities.GAS_HANDLER).orElse(null) instanceof ToolBasicChemicalTankProvider.Gas gasHandler) {
            GasStack radioactive = gasHandler.findChemical(stack -> stack.getAmount()>needed&&stack.has(GasAttributes.Radiation.class));
            var toExtract = gasHandler.createStack(radioactive.getType(),needed);
            gasHandler.extractChemical(toExtract, Action.EXECUTE);
            return (float)radioactive.get(GasAttributes.Radiation.class).getRadioactivity()*needed;
        }
        return 0;
    }
    public static long getRadioactive(IToolStackView tool){
        if (((ToolStack)tool).createStack().getCapability(Capabilities.GAS_HANDLER).orElse(null) instanceof ToolBasicChemicalTankProvider.Gas gasHandler){
            return gasHandler.findChemical(gasStack->gasStack.has(GasAttributes.Radiation.class)&&gasStack.getAmount()>=10).getAmount();
        }
        return 0;
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
}
