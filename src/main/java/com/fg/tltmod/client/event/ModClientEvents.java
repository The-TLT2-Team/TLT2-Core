package com.fg.tltmod.client.event;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.Register.TltCoreKeys;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.client.renderer.entity.*;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonFireCharge;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.NoopRenderer;
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
        event.registerEntityRenderer(TltCoreEntityTypes.THUNDER_BURST.get(), NoopRenderer::new);
        event.registerEntityRenderer(TltCoreEntityTypes.FOOD_ENTITY.get(), FoodEntityRenderer::new);
        event.registerEntityRenderer(TltCoreEntityTypes.IONIZED_ARROW.get(), pContext ->
                new RenderIonizedArrow(pContext,0xFFFFFF,0x6549FF,1,0.4f));
        event.registerEntityRenderer(TltCoreEntityTypes.NEUTRON_ARROW.get(), pContext ->
                new RenderIonizedArrow(pContext,0xFFFFFF,0xC4FFDE,1,0.4f));
        event.registerEntityRenderer(TltCoreEntityTypes.BURNING_SKY_PROJ.get(), pContext -> new RenderDragonFireCharge(pContext,true));
        event.registerEntityRenderer(TltCoreEntityTypes.LASER_BEAM_ARROW.get(), RenderLaserBeamArrow::new);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        TltCoreKeys.registerKeyBindings();
        Collection<KeyMapping> var10000 = TltCoreKeys.KEY_MAPPINGS.values();
        Objects.requireNonNull(event);
        var10000.forEach(event::register);
    }

}
