package com.fg.tltmod.Register;

import com.fg.tltmod.L2.*;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.init.L2Hostility;


public class TltCoreHostilityTrait {
    public static final RegistryEntry<DefenseTrait> defense = L2Hostility.REGISTRATE.regTrait("defense", () -> new DefenseTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 40, 20, 5, 100))
            .desc("The Mob will be immune to damage less than 3% of their maximum health multiplied by %s.").lang("Defense").register();
    public static final RegistryEntry<FuriousTrait> furious = L2Hostility.REGISTRATE.regTrait("furious", () -> new FuriousTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 60, 30, 5, 300))
            .desc("The damage dealt by the mob increases by %2$s%, and the damage caused increases by %1$s%").lang("Furious").register();
    public static final RegistryEntry<EvilKarmaTrait> evil_karma = L2Hostility.REGISTRATE.regTrait("evil_karma", () -> new EvilKarmaTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 10, 50, 5, 20))
            .desc("When the mob is killed by a player, the difficulty level of the killer will increase by 50+ %s% of the current difficulty level").lang("Evil Karma").register();
    public static final RegistryEntry<BloodyBattleTrait> bloody_battle = L2Hostility.REGISTRATE.regTrait("bloody_battle", () -> new BloodyBattleTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 200, 10, 1, 500))
            .desc("When the mob dies for the first time, it is immune to this death and restores its life to full. It continues to lose life for the next 25 seconds, while increasing the damage dealt to 400% and reducing the damage received to 25%").lang("Bloody Battle").register();
    public static final RegistryEntry<TransferTrait> transfer = L2Hostility.REGISTRATE.regTrait("transfer", () -> new TransferTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 100, 70, 3, 200))
            .desc("There is a %s% chance that the mob will randomly teleport the attacker to nearby locations when it is damaged").lang("Transfer").register();
    public static final RegistryEntry<ThudTrait> thud = L2Hostility.REGISTRATE.regTrait("thud", () -> new ThudTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 150, 20, 3, 500))
            .desc("The attack produces a great counterattack and grants the target [Oscillation] effect of %s seconds and %s level").lang("Thud").register();
    public static final RegistryEntry<ContaminatedBloodTrait> contaminated_blood = L2Hostility.REGISTRATE.regTrait("contaminated_blood", () -> new ContaminatedBloodTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 120, 40, 3, 300))
            .desc("The mob will only receive 50% damage, and %s% of the remaining 50% will reduce its life by bleeding").lang("Contaminated Blood").register();
    public static final RegistryEntry<BloodDebtTrait> blood_debt = L2Hostility.REGISTRATE.regTrait("blood_debt", () -> new BloodDebtTrait(()->0xd3fcff), (rl) -> new TraitConfig(rl, 80, 30, 3, 200))
            .desc("The mob's attack will only deal 50% of the damage, while the other 50% will cause damage in a bleeding manner. It will ignore armor and magic defenses and increase the damage by %s%").lang("Blood Debt").register();

    public static void register() {
    }
}
