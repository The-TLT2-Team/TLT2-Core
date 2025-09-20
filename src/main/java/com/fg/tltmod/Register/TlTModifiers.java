package com.fg.tltmod.Register;

import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.fg.tltmod.SomeModifiers.*;

import static com.fg.tltmod.TltCore.MODID;

public class TlTModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);


    public static final StaticModifier<Stop> stopStaticModifier = MODIFIERS.register("stop", Stop::new);
    public static final StaticModifier<EverFlamingCore> EVER_FLAMING_CORE = MODIFIERS.register("ever_flaming_core",EverFlamingCore::new);
}
