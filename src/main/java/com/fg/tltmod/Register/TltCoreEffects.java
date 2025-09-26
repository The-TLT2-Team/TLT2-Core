package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.effects.Oscillation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TltCoreEffects {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TltCore.MODID);

    public static final RegistryObject<MobEffect> oscillation = EFFECT.register("oscillation", Oscillation::new);

}
