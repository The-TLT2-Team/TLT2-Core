package com.fg.tltmod.content.item;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

public class SevenCurseRemovalItem extends Item {
    public SevenCurseRemovalItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (SuperpositionHandler.isTheCursedOne(pPlayer)){
            if (!pLevel.isClientSide){
                CuriosApi.getCuriosInventory(pPlayer).ifPresent(curioInventory->{

                        for (var curioType:curioInventory.getCurios().keySet()) {
                            curioInventory.getStacksHandler(curioType)
                                    .ifPresent(stackHandler -> {
                                        var dynamicStacks = stackHandler.getStacks();
                                        for (int i = 0; i < dynamicStacks.getSlots(); i++) {
                                            ItemStack ring = dynamicStacks.getStackInSlot(i);
                                            if (ring.is(EnigmaticItems.CURSED_RING)) {
                                                CurioUnequipEvent event = new CurioUnequipEvent(ring,new SlotContext(curioType,pPlayer,i,false,true));
                                                MinecraftForge.EVENT_BUS.post(event);
                                                dynamicStacks.setStackInSlot(i, ItemStack.EMPTY);
                                                pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);
                                                break;
                                            }
                                        }
                                    });
                        }
                });
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }
}
