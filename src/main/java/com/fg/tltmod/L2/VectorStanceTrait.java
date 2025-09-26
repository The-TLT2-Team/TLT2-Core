package com.fg.tltmod.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class VectorStanceTrait extends MobTrait {
    public VectorStanceTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onHurtByOthers(int a, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living&&isInFront(entity,living)){
            event.setAmount(event.getAmount()*(1f-a*0.25f));
        }
    }
    private boolean isInFront(LivingEntity target, LivingEntity attacker) {
        if (target == null || attacker == null) {
            return false;
        }
        Vec3 targetLookVec = target.getLookAngle();
        Vec3 targetLookVecHorizontal = new Vec3(targetLookVec.x, 0, targetLookVec.z).normalize();
        Vec3 toAttackerVec = new Vec3(
                attacker.getX() - target.getX(),
                0,
                attacker.getZ() - target.getZ()
        ).normalize();
        double dotProduct = targetLookVecHorizontal.dot(toAttackerVec);
        return dotProduct > 0;
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*25 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
