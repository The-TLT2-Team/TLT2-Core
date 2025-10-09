package com.fg.tltmod.data.providers;

import com.fg.tltmod.TltCore;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.level.surface.ACSurfaceRules;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.SurfaceRules;
import slimeknights.mantle.data.GenericDataProvider;

import com.google.gson.JsonObject;
import java.util.concurrent.CompletableFuture;

public class TltCoreNoiseSettingProvider extends GenericDataProvider {
    public TltCoreNoiseSettingProvider(PackOutput output) {
        super(output, PackOutput.Target.DATA_PACK, "test");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return CompletableFuture.allOf(saveJson(pOutput, TltCore.getResource("magnetic"), createJson(SurfaceRules.ifTrue(SurfaceRules.isBiome(ACBiomeRegistry.MAGNETIC_CAVES) ,ACSurfaceRules.createMagneticCavesRules())) ));
    }

    public JsonObject createJson(SurfaceRules.RuleSource ruleSource){
        JsonObject object = new JsonObject();
        var result = SurfaceRules.RuleSource.CODEC.encodeStart(JsonOps.INSTANCE,ruleSource).result();
        result.ifPresent(jsonElement -> object.add("result", jsonElement));
        return object;
    }

    @Override
    public String getName() {
        return "TltCore Noise Setting Provider (For testing)";
    }
}
