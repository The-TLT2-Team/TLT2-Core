package com.fg.tltmod.client.event;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.client.renderer.entity.RenderWaveSlash;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TltCore.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(TltCoreEntityTypes.WAVE_SLASH.get(), RenderWaveSlash::new);
    }
}
