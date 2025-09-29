package com.fg.tltmod.L2;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        if (!mob.level().isClientSide()) {
            List<Player> ls0 = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(8+a*4));
            for (Player player : ls0) {
                if (player != mob && player != null) {
                    List<ItemStack> items = getStacks(player);
                    for (ItemStack stack:items){
                        int b = (int) (5*a+getEnergyInfo(stack).getMaxEnergy()*0.0001f*a);
                        if (getEnergyInfo(stack).getCurrentEnergy()==0)continue;
                        if (b<getEnergyInfo(stack).getCurrentEnergy())b=getEnergyInfo(stack).getCurrentEnergy();
                        drainEnergy(stack,b);
                        break;
                    }
                }
            }
        }
    }
    public static List<ItemStack> getStacks(Player player) {
        List<ItemStack> list = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent((handler) -> handler.getCurios().forEach((id, curios) -> {
            for(int i = 0; i < curios.getSlots(); ++i) {
                ItemStack stack = curios.getStacks().getStackInSlot(i);
                if (!stack.isEmpty()) {
                    list.add(stack);
                }
            }
        }));
        for (int j = 0; j < player.getInventory().items.size(); j++) {
            ItemStack stack = player.getInventory().items.get(j);
            if (!stack.isEmpty()&&!list.contains(stack)) {
                list.add(stack);
            }
        }
        for (int slot = 0; slot < player.getInventory().armor.size(); slot++) {
            ItemStack stack = player.getInventory().armor.get(slot);
            if (!stack.isEmpty()&&!list.contains(stack)) {
                list.add(stack);
            }
        }
        for (int slot = 0; slot < player.getInventory().offhand.size(); slot++) {
            ItemStack stack = player.getInventory().offhand.get(slot);
            if (!stack.isEmpty()&&!list.contains(stack)){
                list.add(stack);
            }
        }
        return list;
    }
    private static boolean drainEnergy(ItemStack stack,int amount) {
        if (stack.isEmpty()) {
            return false;
        }
        return stack.getCapability(ForgeCapabilities.ENERGY).resolve()
                .map(energyStorage -> {
                    int energyExtracted = energyStorage.extractEnergy(amount, true);
                    if (energyExtracted > 0) {
                        energyStorage.extractEnergy(energyExtracted, false);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
    public static EnergyInfo getEnergyInfo(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.getCapability(ForgeCapabilities.ENERGY).resolve()
                .map(energyStorage -> {
                    int currentEnergy = energyStorage.getEnergyStored();
                    int maxEnergy = energyStorage.getMaxEnergyStored();
                    return new EnergyInfo(currentEnergy, maxEnergy);
                })
                .orElse(null);
    }
    public static class EnergyInfo {
        private final int currentEnergy;
        private final int maxEnergy;

        public EnergyInfo(int current, int max) {
            this.currentEnergy = current;
            this.maxEnergy = max;
        }

        public int getCurrentEnergy() { return currentEnergy; }
        public int getMaxEnergy() { return maxEnergy; }

    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*5 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(i*0.01*20 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal((8+i*4) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
