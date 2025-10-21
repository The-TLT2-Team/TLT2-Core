package com.fg.tltmod.Register;

import com.fg.tltmod.L2.*;
import com.fg.tltmod.data.providers.l2hostility.TltTraitConfigRegistry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;


public class TltCoreHostilityTrait {
    private static <T extends MobTrait> RegistryEntry<T> register(
            String name,
            NonNullSupplier<T> sup,
            NonNullFunction<ResourceLocation, TraitConfig> configFactory,
            String desc,
            String lang) {

        return L2Hostility.REGISTRATE
                .regTrait(name, sup, rl -> {
                    TraitConfig cfg = configFactory.apply(rl);
                    TltTraitConfigRegistry.register(rl, cfg);
                    return cfg;
                })
                .desc(desc)
                .lang(lang)
                .register();
    }

    public static final RegistryEntry<DefenseTrait> defense =
            register("defense", () -> new DefenseTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 40, 20, 5, 100),
                    "The Mob will be immune to damage less than 3% of their maximum health multiplied by %s.",
                    "Defense");

    public static final RegistryEntry<FuriousTrait> furious =
            register("furious", () -> new FuriousTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 60, 30, 5, 300),
                    "The damage dealt by the mob increases by %2$s%, and the damage caused increases by %1$s%",
                    "Furious");

    public static final RegistryEntry<EvilKarmaTrait> evil_karma =
            register("evil_karma", () -> new EvilKarmaTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 10, 50, 5, 20),
                    "When the mob is killed by a player, the difficulty level of the killer will increase by 50+ %s% of the current difficulty level",
                    "Evil Karma");

    public static final RegistryEntry<BloodyBattleTrait> bloody_battle =
            register("bloody_battle", () -> new BloodyBattleTrait(ChatFormatting.DARK_RED),
                    rl -> new TraitConfig(rl, 200, 10, 1, 500),
                    "When the mob dies for the first time, it is immune to this death and restores its life to full. It continues to lose life for the next 25 seconds, while increasing the damage dealt to 400% and reducing the damage received to 25%",
                    "Bloody Battle");

    public static final RegistryEntry<TransferTrait> transfer =
            register("transfer", () -> new TransferTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 100, 70, 3, 200),
                    "There is a %s% chance that the mob will randomly teleport the attacker to nearby locations when it is damaged",
                    "Transfer");

    public static final RegistryEntry<ThudTrait> thud =
            register("thud", () -> new ThudTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 150, 20, 3, 500),
                    "The attack produces a great counterattack and grants the target [Oscillation] effect of %s seconds and %s level",
                    "Thud");

    public static final RegistryEntry<ContaminatedBloodTrait> contaminated_blood =
            register("contaminated_blood", () -> new ContaminatedBloodTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 120, 40, 3, 300),
                    "The mob will only receive 50% damage, and %s% of the remaining 50% will reduce its life by bleeding",
                    "Contaminated Blood");

    public static final RegistryEntry<BloodDebtTrait> blood_debt =
            register("blood_debt", () -> new BloodDebtTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 80, 30, 3, 200),
                    "The mob's attack will only deal 50% of the damage, while the other 50% will cause damage in a bleeding manner. It will ignore armor and magic defenses and increase the damage by %s%",
                    "Blood Debt");

    public static final RegistryEntry<TitanBloodlineTrait> titan_bloodline =
            register("titan_bloodline", () -> new TitanBloodlineTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 300, 20, 3, 600),
                    "The damage caused increases by 5% of mob's maximum life.When the mob takes damage, it will increase its current maximum health by %s%, up to a maximum of 10 times",
                    "Titan Bloodline");

    public static final RegistryEntry<ShowSwordTrait> show_sword =
            register("show_sword", () -> new ShowSwordTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 150, 10, 3, 300),
                    "The damage caused by the mob increases by %2$s%, and they are immune to damage from creatures that are more than %1$s apart, but cannot deal damage to them",
                    "Show Sword");

    public static final RegistryEntry<DevouringLifeTrait> devouring_life =
            register("devouring_life", () -> new DevouringLifeTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 20, 30, 5, 60),
                    "The damage caused by the mob will heal themselves according to their %s% value",
                    "Devouring Life");

    public static final RegistryEntry<HarvestSharingTrait> harvest_sharing =
            register("harvest_sharing", () -> new HarvestSharingTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 60, 20, 5, 200),
                    "If any creature within 20 squares recovers its life, it will recover its own life equivalent to %s% of its recovery amount",
                    "Harvest Sharing");

    public static final RegistryEntry<CriticalHitTrait> critical_hit =
            register("critical_hit", () -> new CriticalHitTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 120, 40, 3, 300),
                    "There is a %2$s% chance that each attack will increase the damage by %1$s%",
                    "Critical Hit");

    public static final RegistryEntry<VectorStanceTrait> vector_stance =
            register("vector_stance", () -> new VectorStanceTrait(() -> 0xd3fcff),
                    rl -> new TraitConfig(rl, 60, 70, 3, 100),
                    "Organisms located in front of themselves will cause %s% less damage to themselves",
                    "Vector Stance");

    public static final RegistryEntry<AbsorbManaTrait> absorb_mana =
            register("absorb_mana", () -> new AbsorbManaTrait(()->0x49ff39),
                    rl -> new TraitConfig(rl, 150, 30, 5, 400),
            "Extract %smana from player each 10 ticks.",
                    "Absorb Mana");

    public static final RegistryEntry<MagalaErode> magala_erode =
            register("magala_erode", () -> new MagalaErode(() -> 0x620591),
                    rl -> new TraitConfig(rl, 100, 40, 1, 150),
                    "The self has overcome bloodlust and when attacking or being injured, inflicts bloodlust on the target",
                    "Magala Erode");

    public static final RegistryEntry<BrokenArmor> broken_armor =
            register(
                    "broken_armor",
                    () -> new BrokenArmor(() -> 0x620591),
                    rl -> new TraitConfig(rl, 5, 60, 5, 40),
                    "Attacks apply %s-second armor-braking effect that reduces your armor value. " +
                            "Reapplying it will increase its level and extend its duration.",
                    "Broken Armor"
            );

    public static final RegistryEntry<DevouringElectricityTrait> devouring_electricity =
            register("devouring_electricity", () -> new DevouringElectricityTrait(()->0x49ff39),
                    rl -> new TraitConfig(rl, 200, 30, 5, 300),
                    "Extract %skFE of energy from player each 5 ticks.",
                    "Devouring Electricity");

    public static void register() {
    }
}
