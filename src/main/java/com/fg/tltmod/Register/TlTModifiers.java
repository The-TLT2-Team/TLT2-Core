package com.fg.tltmod.Register;

import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.fg.tltmod.SomeModifiers.*;

import static com.fg.tltmod.TltCore.MODID;

public class TlTModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);


    public static StaticModifier<Stop> stopStaticModifier = MODIFIERS.register("stop", Stop::new);
}
