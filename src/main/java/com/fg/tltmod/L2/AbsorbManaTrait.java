package com.fg.tltmod.L2;

import com.google.common.collect.Iterables;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        if (!mob.level().isClientSide()) {
            List<Player> ls0 = mob.level().getEntitiesOfClass(Player.class, mob.getBoundingBox().inflate(8+a*4));
            for (Player player : ls0) {
                if (player != mob && player != null) {
                    List<ItemStack> items = ManaItemHandler.INSTANCE.getManaItems(player);
                    List<ItemStack> acc = ManaItemHandler.INSTANCE.getManaAccesories(player);
                    for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
                        var manaItem = XplatAbstractions.INSTANCE.findManaItem(stackInSlot);
                        if (manaItem != null) {
                            int b = (int) (10*a + manaItem.getMaxMana()*0.002f*a);
                            if (manaItem.getMana()==0)continue;
                            if (manaItem.getMana()<b)b=manaItem.getMana();
                            manaItem.addMana(-b);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*10*20 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(i*0.2*20 + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal((i*4+8) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
