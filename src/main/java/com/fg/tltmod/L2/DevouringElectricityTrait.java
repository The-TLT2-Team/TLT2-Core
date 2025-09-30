package com.fg.tltmod.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class DevouringElectricityTrait extends MobTrait {
    public DevouringElectricityTrait(IntSupplier color) {
        super((color));
    }
    @Override
    public void tick(LivingEntity mob, int a) {
        float radius = 6;
        if (!mob.level().isClientSide()&&mob.level().getGameTime()%5==0) {
            int extract = a*25000;
            mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(radius),
                    player -> player!=mob&&!player.isCreative()&&mob.distanceTo(player)<=6).forEach(player -> {
                Inventory inventory = player.getInventory();
                List<ItemStack> stacks = new ArrayList<>(inventory.armor);
                stacks.addAll(inventory.offhand);
                for (int i = 0; i < 9; i++) {
                    stacks.add(inventory.getItem(i));
                }
                stacks.stream().filter(stack -> !stack.isEmpty())
                        .forEach(stack -> stack.getCapability(ForgeCapabilities.ENERGY)
                                .ifPresent(cap -> cap.extractEnergy(extract, false)));
            });
        }
        if (mob.level().isClientSide()) {
            Vec3 center = mob.position();
            float tpi = ((float)Math.PI * 2F);
            Vec3 v0 = new Vec3(0.0F, radius, 0.0F);
            v0 = v0.xRot(tpi / 4.0F).yRot(mob.getRandom().nextFloat() * tpi);
            int k = 0xFF7700;
            mob.level().addAlwaysVisibleParticle(ParticleTypes.EFFECT, center.x + v0.x, center.y + v0.y + (double)0.5F, center.z + v0.z, (double)(k >> 16 & 255) / (double)255.0F, (double)(k >> 8 & 255) / (double)255.0F, (double)(k & 255) / (double)255.0F);
        }
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*25 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
