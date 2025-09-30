package com.fg.tltmod.Register;

import com.fg.tltmod.SomeModifiers.armor.BlessingFromFoxGod;
import com.fg.tltmod.SomeModifiers.hidden.BurstMiningModifier;
import com.fg.tltmod.SomeModifiers.hidden.LCManaBurstModifier;
import com.fg.tltmod.SomeModifiers.integration.botania.AlfBurst;
import com.fg.tltmod.SomeModifiers.integration.botania.ExcaliburBurst;
import com.fg.tltmod.SomeModifiers.integration.botania.FartherSights;
import com.fg.tltmod.SomeModifiers.integration.botania.TerraBurst;
import com.fg.tltmod.SomeModifiers.interaction.InTheNameOfLoveAndHate;
import com.fg.tltmod.SomeModifiers.melee.Stop;
import com.fg.tltmod.SomeModifiers.misc.EverFlamingCore;
import com.fg.tltmod.SomeModifiers.misc.PowerOfPrayer;
import com.fg.tltmod.SomeModifiers.integration.sevenCurse.NeverEndsStartup;
import com.fg.tltmod.SomeModifiers.integration.sevenCurse.PiglinDefense;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.fg.tltmod.TltCore.MODID;

public class TltCoreModifiers {

    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);


    public static final StaticModifier<Stop> stopStaticModifier = MODIFIERS.register("stop", Stop::new);
    public static final StaticModifier<EverFlamingCore> EVER_FLAMING_CORE = MODIFIERS.register("ever_flaming_core",EverFlamingCore::new);
    public static final StaticModifier<InTheNameOfLoveAndHate> LOVE_AND_HATE_STATIC = MODIFIERS.register("in_the_name_of_love_and_hate", InTheNameOfLoveAndHate::new);
    public static final StaticModifier<BlessingFromFoxGod> BLESSING_FROM_FOX_GOD = MODIFIERS.register("blessing_from_fox_god",BlessingFromFoxGod::new);
    public static final StaticModifier<PowerOfPrayer> POWER_OF_PRAYER = MODIFIERS.register("power_of_prayer",PowerOfPrayer::new);
    public static final StaticModifier<NeverEndsStartup> NEVER_ENDS_STARTUP = MODIFIERS.register("never_ends_startup", NeverEndsStartup::new);
    public static final StaticModifier<PiglinDefense> PIGLIN_DEFENSE = MODIFIERS.register("piglin_defense",PiglinDefense::new);
    public static final StaticModifier<LCManaBurstModifier> MANA_BURST_HANDLER = MODIFIERS.register("mana_burst_handler",LCManaBurstModifier::new);
    public static final StaticModifier<TerraBurst> TERRA_BEAM = MODIFIERS.register("terra_burst",TerraBurst::new);
    public static final StaticModifier<ExcaliburBurst> EXCALIBUR_BURST = MODIFIERS.register("excalibur_burst",ExcaliburBurst::new);
    public static final StaticModifier<AlfBurst> ALF_BURST = MODIFIERS.register("alf_burst",AlfBurst::new);
    public static final StaticModifier<FartherSights> FAR_SIGHTS = MODIFIERS.register("far_sights",FartherSights::new);
    public static final StaticModifier<BurstMiningModifier> BURST_MINING = MODIFIERS.register("burst_mining",BurstMiningModifier::new);

}
