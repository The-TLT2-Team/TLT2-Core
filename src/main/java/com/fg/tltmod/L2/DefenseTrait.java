package com.fg.tltmod.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class DefenseTrait extends MobTrait {
    public DefenseTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        float a = entity.getMaxHealth()*0.03f*level;
        if (event.getAmount()<a) {
            event.setAmount(0);
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
