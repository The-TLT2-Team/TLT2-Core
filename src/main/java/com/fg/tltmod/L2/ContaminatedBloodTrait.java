package com.fg.tltmod.L2;

import com.fg.tltmod.TltCore;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class ContaminatedBloodTrait extends MobTrait {
    public ContaminatedBloodTrait(IntSupplier color) {
        super((color));
    }
    public static String contaminated_blood_value = TltCore.getResource( "contaminated_blood_value").toString();
    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        entity.getPersistentData().putFloat(contaminated_blood_value, entity.getPersistentData().getFloat(contaminated_blood_value)+event.getAmount()*0.5f*(1f-a*0.2f));
        event.setAmount(event.getAmount()*0.5f);
    }
    @Override
    public void tick(LivingEntity mob, int a) {
        float b=mob.getPersistentData().getFloat(contaminated_blood_value);
        if (!mob.level().isClientSide()&&b>=1) {
            mob.setHealth(mob.getHealth() - b*0.02f);
            mob.getPersistentData().putFloat(contaminated_blood_value,b*0.98f);
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal((100-i*20) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
