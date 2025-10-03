package com.fg.tltmod.Register;

import com.fg.tltmod.SomeModifiers.armor.*;
import com.fg.tltmod.SomeModifiers.harvest.*;
import com.fg.tltmod.SomeModifiers.hidden.*;
import com.fg.tltmod.SomeModifiers.integration.botania.*;
import com.fg.tltmod.SomeModifiers.integration.botania.mana.ManaRefactorModifier;
import com.fg.tltmod.SomeModifiers.interaction.*;
import com.fg.tltmod.SomeModifiers.melee.*;
import com.fg.tltmod.SomeModifiers.misc.*;
import com.fg.tltmod.SomeModifiers.integration.sevenCurse.*;
import com.fg.tltmod.SomeModifiers.rebalance.BlowPipeRebalance;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.tools.modifiers.traits.general.TannedModifier;

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
    public static final StaticModifier<HeatTendency> HEAT_TENDENCY = MODIFIERS.register("heat_tendency",HeatTendency::new);
    public static final StaticModifier<PrimeOfGarbageStones> PRIME_OF_GARBAGE_STONES = MODIFIERS.register("prime_of_garbage_stones",PrimeOfGarbageStones::new);
    public static final StaticModifier<TannedModifier> REINFORCED = MODIFIERS.register("reinforced",TannedModifier::new);
    public static final StaticModifier<TheCursedOne> THE_CURSED_ONE = MODIFIERS.register("the_cursed_one",TheCursedOne::new);
    public static final StaticModifier<RageOfSparta> RAGE_OF_SPARTA = MODIFIERS.register("rage_of_sparta",RageOfSparta::new);
    public static final StaticModifier<ManaRefactorModifier> MANA_REFACTOR = MODIFIERS.register("mana_refactor", ManaRefactorModifier::new);
    public static final StaticModifier<BlowPipeRebalance> BLOW_PIPE_REBALANCE = MODIFIERS.register("blowpipe_rebalance", BlowPipeRebalance::new);
}
