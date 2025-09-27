package com.fg.tltmod.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
        if (event.getSource().getEntity() instanceof LivingEntity){
            if (entity.level().isClientSide)return;
            if (isInFront(event.getSource(),entity)) {
                event.setAmount(event.getAmount() * (1f - a * 0.25f));
            }
        }
    }
    private boolean isInFront(DamageSource damageSourceIn,LivingEntity living) {

            Vec3 vector3d2 = damageSourceIn.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = living.getViewVector(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(living.position()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0D, vector3d1.z);
                return vector3d1.dot(vector3d) < 0.0D;
            }

        return false;
    }


    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*25 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
