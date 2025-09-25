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

    public static void register() {
    }
}
