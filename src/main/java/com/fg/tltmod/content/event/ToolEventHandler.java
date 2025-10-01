package com.fg.tltmod.content.event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.util.TltcoreConstants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class ToolEventHandler {
    @SubscribeEvent
    public static void onToolCrafted(PlayerEvent.ItemCraftedEvent event){
        if (event.getCrafting().getItem() instanceof IModifiable){
            ItemStack stack = event.getCrafting();
            Player player = event.getEntity();
            if (SuperpositionHandler.isTheCursedOne(player)){
                ToolStack toolStack = ToolStack.from(stack);
                toolStack.getPersistentData().putBoolean(TltcoreConstants.NbtLocations.KEY_IS_THE_CURSED_ONE,true);
            }
        }
    }
}
