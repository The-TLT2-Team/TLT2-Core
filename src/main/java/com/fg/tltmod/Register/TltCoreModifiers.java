package com.fg.tltmod.Register;

import com.fg.tltmod.SomeModifiers.armor.*;
import com.fg.tltmod.SomeModifiers.harvest.*;
import com.fg.tltmod.SomeModifiers.hidden.*;
import com.fg.tltmod.SomeModifiers.integration.arsnouveau.CasterToolModifier;
import com.fg.tltmod.SomeModifiers.integration.botania.*;
import com.fg.tltmod.SomeModifiers.integration.botania.base.AddManaSlotModifier;
import com.fg.tltmod.SomeModifiers.integration.botania.mana.ManaRefactorModifier;
import com.fg.tltmod.SomeModifiers.integration.botania.specialized.ExcaliburBurst;
import com.fg.tltmod.SomeModifiers.integration.botania.specialized.ParticleAccelerate;
import com.fg.tltmod.SomeModifiers.integration.botania.specialized.Thunderstorm;
import com.fg.tltmod.SomeModifiers.interaction.*;
import com.fg.tltmod.SomeModifiers.melee.*;
import com.fg.tltmod.SomeModifiers.misc.*;
import com.fg.tltmod.SomeModifiers.integration.sevenCurse.*;
import com.fg.tltmod.SomeModifiers.rebalance.ArrowThrowerRebalance;
import com.fg.tltmod.SomeModifiers.rebalance.BlowPipeRebalance;
import com.fg.tltmod.SomeModifiers.rebalance.DashRebalance;
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
    public static final StaticModifier<CasterToolModifier> CASTER_TOOL = MODIFIERS.register("caster_tool",CasterToolModifier::new);
    public static final StaticModifier<ArrowThrowerRebalance> ARROW_THROWER_REBALANCE = MODIFIERS.register("arrow_thrower_rebalance",ArrowThrowerRebalance::new);
    public static final StaticModifier<DashRebalance> DASH_REBALANCE = MODIFIERS.register("dash_rebalance",DashRebalance::new);
    public static final StaticModifier<Thunderstorm> THUNDERSTORM = MODIFIERS.register("thunderstorm",Thunderstorm::new);
    public static final StaticModifier<ParticleAccelerate> PARTICLE_ACCELERATE = MODIFIERS.register("particle_accelerate",ParticleAccelerate::new);
    public static final StaticModifier<AddManaSlotModifier> LIGHT_ENGRAVE = MODIFIERS.register("light_engrave",AddManaSlotModifier::new);
    public static final StaticModifier<AddManaSlotModifier> DARK_EROSION = MODIFIERS.register("dark_erosion",AddManaSlotModifier::new);
    public static final StaticModifier<SourceObfuscate> SOURCE_OBFUSCATE = MODIFIERS.register("source_obfuscate",SourceObfuscate::new);
    public static final StaticModifier<BurstPierce> BURST_PIERCE = MODIFIERS.register("burst_pierce", BurstPierce::new);
    public static final StaticModifier<DestructionMagic> DESTRUCTION_MAGIC = MODIFIERS.register("destruction_magic",DestructionMagic::new);
    public static final StaticModifier<ElasticMemory> ELASTIC_MEMORY = MODIFIERS.register("elastic_memory",ElasticMemory::new);
    public static final StaticModifier<ExtraWarhead> EXTRA_WARHEAD = MODIFIERS.register("extra_warhead",ExtraWarhead::new);
    public static final StaticModifier<MoreCapacity> MORE_CAPACITY = MODIFIERS.register("more_capacity",MoreCapacity::new);
    public static final StaticModifier<OverCapacity> OVER_CAPACITY = MODIFIERS.register("over_capacity",OverCapacity::new);
    public static final StaticModifier<Overclock> OVERCLOCK = MODIFIERS.register("overclock",Overclock::new);
    public static final StaticModifier<PulseEngine> PULSE_ENGINE = MODIFIERS.register("pulse_engine",PulseEngine::new);
    public static final StaticModifier<BurstAccelerate> BURST_ACCELERATE = MODIFIERS.register("burst_accelerate",BurstAccelerate::new);
    public static final StaticModifier<BurstBypassCooldown> BURST_BYPASS_COOLDOWN = MODIFIERS.register("burst_bypass_cooldown",BurstBypassCooldown::new);
    public static final StaticModifier<BurstMassEffect> BURST_MASS_EFFECT = MODIFIERS.register("burst_mass_effect",BurstMassEffect::new);
    public static final StaticModifier<ManaTransmitter> MANA_TRANSMITTER = MODIFIERS.register("mana_transmitter",ManaTransmitter::new);
    public static final StaticModifier<WarpAttack> WARP_ATTACK = MODIFIERS.register("warp_attack",WarpAttack::new);
    public static final StaticModifier<MagneticFocus> MAGNETIC_FOCUS = MODIFIERS.register("magnetic_focus",MagneticFocus::new);
    public static final StaticModifier<MagneticScatter> MAGNETIC_SCATTER = MODIFIERS.register("magnetic_scatter",MagneticScatter::new);
    public static final StaticModifier<GravityInterrupt> GRAVITY_INTERRUPT = MODIFIERS.register("gravity_interrupt",GravityInterrupt::new);
    public static final StaticModifier<DaysAndNights> DAYS_AND_NIGHTS = MODIFIERS.register("days_and_nights",DaysAndNights::new);
    public static final StaticModifier<GravityManipulate> GRAVITY_MANIPULATE = MODIFIERS.register("gravity_manipulate",GravityManipulate::new);
    public static final StaticModifier<VibrioVulnificus> VIBRIO_VULNIFICUS = MODIFIERS.register("vibrio_vulnificus",VibrioVulnificus::new);
    public static final StaticModifier<EnvironmentalProtection> ENVIRONMENTAL_PROTECTION = MODIFIERS.register("environmental_protection",EnvironmentalProtection::new);
    public static final StaticModifier<IonizedArrowModifier> IONIZED_ARROW = MODIFIERS.register("ionized_arrow",IonizedArrowModifier::new);
    public static final StaticModifier<ValkyrieBless> VALKYRIE_BLESS = MODIFIERS.register("valkyrie_bless",ValkyrieBless::new);

}
