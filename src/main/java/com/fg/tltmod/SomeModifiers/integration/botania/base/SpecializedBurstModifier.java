package com.fg.tltmod.SomeModifiers.integration.botania.base;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.DynamicComponentUtil;
import com.fg.tltmod.Register.TltCoreModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class SpecializedBurstModifier extends EtSTBaseModifier implements ValidateModifierHook {
    @Override
    public int getPriority() {
        return -100;
    }

    @Override
    public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        if (tool.getModifierLevel(TltCoreModifiers.FAR_SIGHTS.get())>0) source = this.modifySourceWhenTriggerTool(tool,entry,attacker
                ,hand,target,sourceSlot,isFullyCharged
                ,isExtraAttack,isCritical,source);
        return source;
    }

    public LegacyDamageSource modifySourceWhenTriggerTool(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source){
        return source;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VALIDATE);
    }

    @Override
    public @Nullable Component validate(IToolStackView tool, ModifierEntry modifier) {
        int i = 0;
        List<Modifier> unsupported = new ArrayList<>();
        for (ModifierEntry entry:tool.getModifiers()){
            if (entry.getModifier() instanceof SpecializedBurstModifier){
                unsupported.add(entry.getModifier());
                i++;
            }
            if (i>1){
                var comp =Component.translatable("tooltip.tltmod.error.specialized_burst_modifier");
                for (int j=0;j<unsupported.size();j++){
                    if (j!=0) comp.append("、");
                    comp.append(unsupported.get(j).getDisplayName());
                }
                return comp;
            }
        }
        return null;
    }

    @Override
    public List<Component> getDescriptionList() {
        var list =new ArrayList<>(super.getDescriptionList());
        list.add(Component.translatable("info.tltmod.specialized_burst_modifier"));
        return list;
    }
}
