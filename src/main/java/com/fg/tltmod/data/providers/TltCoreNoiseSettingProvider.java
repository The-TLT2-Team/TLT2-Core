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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TltCoreNoiseSettingProvider extends GenericDataProvider {
    public TltCoreNoiseSettingProvider(PackOutput output) {
        super(output, PackOutput.Target.DATA_PACK, "test");
    }

    static List<SurfaceRules.RuleSource> getCitadelSurfaceRuleRegistry() {
        try {
            var cl = Class.forName("com.github.alexthe666.citadel.server.generation.SurfaceRulesManager");
            var field = cl.getDeclaredField("OVERWORLD_REGISTRY");
            field.setAccessible(true);
            return (List<SurfaceRules.RuleSource>) field.get(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        ACSurfaceRules.setup();
        AtomicInteger integer = new AtomicInteger(0);
        return allOf(getCitadelSurfaceRuleRegistry().stream().map(rule-> saveJson(pOutput, TltCore.getResource("cave_rule_"+integer.addAndGet(1)), createJson(rule) )));
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
