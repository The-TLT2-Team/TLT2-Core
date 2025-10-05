package com.fg.tltmod.client.event;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.Register.TltCoreKeys;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.client.renderer.entity.LaserEntityRenderer;
import com.fg.tltmod.client.renderer.entity.RenderWaveSlash;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = TltCore.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(TltCoreEntityTypes.WAVE_SLASH.get(), RenderWaveSlash::new);
        event.registerEntityRenderer(TltCoreEntityTypes.LASER_A.get(), LaserEntityRenderer::new);
        event.registerEntityRenderer(TltCoreEntityTypes.MOON_SLIME_BOSS.get(), SlimeRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        TltCoreKeys.registerKeyBindings();
        Collection<KeyMapping> var10000 = TltCoreKeys.KEY_MAPPINGS.values();
        Objects.requireNonNull(event);
        var10000.forEach(event::register);
    }

}
