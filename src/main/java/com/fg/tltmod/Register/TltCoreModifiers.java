package com.fg.tltmod.Register;

import com.fg.tltmod.SomeModifiers.sevenCurse.NeverEndsStartup;
import com.fg.tltmod.SomeModifiers.sevenCurse.PiglinDefense;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import com.fg.tltmod.SomeModifiers.*;

import static com.fg.tltmod.TltCore.MODID;

public class TltCoreModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);


    public static final StaticModifier<Stop> stopStaticModifier = MODIFIERS.register("stop", Stop::new);
    public static final StaticModifier<EverFlamingCore> EVER_FLAMING_CORE = MODIFIERS.register("ever_flaming_core",EverFlamingCore::new);
    public static final StaticModifier<NeverEndsStartup> NEVER_ENDS_STARTUP = MODIFIERS.register("never_ends_startup", NeverEndsStartup::new);
    public static final StaticModifier<PiglinDefense> PIGLIN_DEFENSE = MODIFIERS.register("piglin_defense",PiglinDefense::new);

}
