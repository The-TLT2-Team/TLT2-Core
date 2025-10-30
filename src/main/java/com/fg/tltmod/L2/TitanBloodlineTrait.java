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
    public static final UUID titan_bloodline_UUID = UUID.fromString("0A825DF7-410A-4E95-08E5-0F9A7F3FEE93");
    @Override
    public void onHurtTarget(int a, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache) {
        float b = attacker.getMaxHealth()*0.05f;
        cache.addHurtModifier(DamageModifier.add(b));
    }
    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        int currentLevel = entity.getPersistentData().getInt(titan_bloodline_value);
        if (currentLevel < 10) {
            entity.getPersistentData().putInt(titan_bloodline_value, currentLevel + 1);

            applyMultiplicativeHealthBonus(entity, a);
            entity.heal(entity.getMaxHealth() * 0.06f * a);
        }
    }

    private static void applyMultiplicativeHealthBonus(LivingEntity entity, int a) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr == null) return;

        maxHealthAttr.removeModifier(titan_bloodline_UUID);

        int currentLevel = entity.getPersistentData().getInt(titan_bloodline_value);
        float totalMultiplier = 0.05f * currentLevel * a;

        AttributeModifier newModifier = new AttributeModifier(
                titan_bloodline_UUID,
                Attributes.MAX_HEALTH.getDescriptionId(),
                totalMultiplier,
                AttributeModifier.Operation.MULTIPLY_TOTAL
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
