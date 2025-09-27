package com.fg.tltmod.L2;

import com.fg.tltmod.Register.TltCoreEffects;
import com.fg.tltmod.TltCore;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

public class TitanBloodlineTrait extends MobTrait {
    public TitanBloodlineTrait(IntSupplier color) {
        super((color));
    }
    public static String titan_bloodline_value = TltCore.getResource("titan_bloodline_value").toString();
    public static final UUID titan_bloodline_UUID = UUID.fromString("1E6FBD81-01B2-3151-F02C-F04623958205");
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        float b = attacker.getMaxHealth()*0.05f;
        cache.addHurtModifier(DamageModifier.add(b));
    }
    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        if (entity.getPersistentData().getInt(titan_bloodline_value)<10){
            entity.getPersistentData().putInt(titan_bloodline_value,entity.getPersistentData().getInt(titan_bloodline_value)+1);
            addMaxHealth(entity, entity.getMaxHealth() * 0.05f * a);
            entity.heal(entity.getMaxHealth() * 0.06f * a);
        }
    }
    private static void addMaxHealth(LivingEntity entity, float reductionAmount) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;
        AttributeModifier existingModifier = maxHealthAttr.getModifier(titan_bloodline_UUID);
        double currentReduction = (existingModifier != null) ? existingModifier.getAmount() : 0.0;
        double newTotalReduction = currentReduction + reductionAmount;
        maxHealthAttr.removeModifier(titan_bloodline_UUID);
        AttributeModifier newModifier = new AttributeModifier(
                titan_bloodline_UUID,
                Attributes.MAX_HEALTH.getDescriptionId(),
                newTotalReduction,
                AttributeModifier.Operation.ADDITION
        );
        maxHealthAttr.addPermanentModifier(newModifier);
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*5 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
