package com.fg.tltmod.SomeModifiers.armor;

import com.fuyun.cloudertinker.extend.superclass.ArmorModifier;
import com.fuyun.cloudertinker.register.CloudertinkerEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import util.method.ModifierLevel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class Hardarmor extends ArmorModifier {
    public boolean havenolevel() {
        return true;
    }
    public UUID UUIDFromWeapon(EquipmentSlot solt, ModifierId modifierId){
        return UUID.nameUUIDFromBytes((solt.getName()+modifierId.toString()).getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (tool.hasTag(TinkerTags.Items.ARMOR)){
            consumer.accept(Attributes.ARMOR, new AttributeModifier(UUIDFromWeapon(slot,this.getId()), String.valueOf(Attributes.ARMOR), 2, AttributeModifier.Operation.ADDITION));
        }
    }
    public void MobEffectEvent(MobEffectEvent.Applicable event) {
        if (event.getEntity() != null && event.getEntity() instanceof LivingEntity) {
            if (ModifierLevel.EquipHasModifierlevel(event.getEntity(), this.getId())) {
                if (event.getEffectInstance().getEffect() == CloudertinkerEffects.Armorbroken.get()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isSelected || isCorrectSlot) {
            Collection<MobEffectInstance> effects = entity.getActiveEffects();
            List<MobEffectInstance> effectInstances = new ArrayList<>(effects);
            for (MobEffectInstance effectlist : effectInstances) {
                   if (effectlist.getEffect() == CloudertinkerEffects.Armorbroken.get()) {
                        entity.removeEffect(effectlist.getEffect());
                    }
            }
        }
    }
}
