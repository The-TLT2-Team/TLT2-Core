package com.fg.tltmod.content.event;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.living.MoonSlimeBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,modid = TltCore.MODID)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(TltCoreEntityTypes.MOON_SLIME_BOSS.get(), Monster.createMonsterAttributes().build());
    }
}
