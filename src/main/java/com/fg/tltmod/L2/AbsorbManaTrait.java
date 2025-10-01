package com.fg.tltmod.L2;

import com.google.common.collect.Iterables;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.IntSupplier;

public class AbsorbManaTrait extends MobTrait {
    public AbsorbManaTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void tick(LivingEntity mob, int a) {
        float range = 5;
        if (!mob.level().isClientSide()&&mob.level().getGameTime()%10==0) {
            int extract = a*100;
            mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(range),
                    player -> player!=mob&&!player.isCreative()&&mob.distanceTo(player)<=6)
                    .forEach(player -> ManaItemHandler.instance()
                            .requestManaForTool(player.getMainHandItem(),player,extract,true));
        }
        if (mob.level().isClientSide()) {
            Vec3 center = mob.position();
            float tpi = ((float)Math.PI * 2F);
            Vec3 v0 = new Vec3(0.0F, range, 0.0F);
            v0 = v0.xRot(tpi / 4.0F).yRot(mob.getRandom().nextFloat() * tpi);
            int k = 0x0099FF;
            mob.level().addAlwaysVisibleParticle(ParticleTypes.EFFECT, center.x + v0.x, center.y + v0.y + (double)0.5F, center.z + v0.z, (double)(k >> 16 & 255) / (double)255.0F, (double)(k >> 8 & 255) / (double)255.0F, (double)(k & 255) / (double)255.0F);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*100 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
