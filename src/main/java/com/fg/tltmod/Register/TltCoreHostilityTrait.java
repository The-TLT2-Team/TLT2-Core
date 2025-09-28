package com.fg.tltmod.Register;

import com.fg.tltmod.L2.*;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.ChatFormatting;


public class TltCoreHostilityTrait {
    public static final RegistryEntry<DefenseTrait> defense = L2Hostility.REGISTRATE.regTrait("defense", () -> new DefenseTrait(()->0xd2d2d2), (rl) -> new TraitConfig(rl, 40, 20, 5, 100))
            .desc("The Mob will be immune to damage less than 3% of their maximum health multiplied by %s.").lang("Defense").register();
    public static final RegistryEntry<FuriousTrait> furious = L2Hostility.REGISTRATE.regTrait("furious", () -> new FuriousTrait(()->0xff8000), (rl) -> new TraitConfig(rl, 60, 30, 5, 300))
            .desc("The damage dealt by the mob increases by %2$s%, and the damage caused increases by %1$s%").lang("Furious").register();
    public static final RegistryEntry<EvilKarmaTrait> evil_karma = L2Hostility.REGISTRATE.regTrait("evil_karma", () -> new EvilKarmaTrait(()->0x790061), (rl) -> new TraitConfig(rl, 10, 50, 5, 20))
            .desc("When the mob is killed by a player, the difficulty level of the killer will increase by 50+ %s% of the current difficulty level").lang("Evil Karma").register();
    public static final RegistryEntry<BloodyBattleTrait> bloody_battle = L2Hostility.REGISTRATE.regTrait("bloody_battle", () -> new BloodyBattleTrait(ChatFormatting.DARK_RED), (rl) -> new TraitConfig(rl, 200, 10, 1, 500))
            .desc("When the mob dies for the first time, it is immune to this death and restores its life to full. It continues to lose life for the next 25 seconds, while increasing the damage dealt to 400% and reducing the damage received to 25%").lang("Bloody Battle").register();
    public static final RegistryEntry<TransferTrait> transfer = L2Hostility.REGISTRATE.regTrait("transfer", () -> new TransferTrait(()->0xc300ff), (rl) -> new TraitConfig(rl, 100, 70, 3, 200))
            .desc("There is a %s% chance that the mob will randomly teleport the attacker to nearby locations when it is damaged").lang("Transfer").register();
    public static final RegistryEntry<ThudTrait> thud = L2Hostility.REGISTRATE.regTrait("thud", () -> new ThudTrait(()->0xffc800), (rl) -> new TraitConfig(rl, 150, 20, 3, 500))
            .desc("The attack produces a great counterattack and grants the target [Oscillation] effect of %s seconds and %s level").lang("Thud").register();
    public static final RegistryEntry<ContaminatedBloodTrait> contaminated_blood = L2Hostility.REGISTRATE.regTrait("contaminated_blood", () -> new ContaminatedBloodTrait(()->0xb20000), (rl) -> new TraitConfig(rl, 120, 40, 3, 300))
            .desc("The mob will only receive 50% damage, and %s% of the remaining 50% will reduce its life by bleeding").lang("Contaminated Blood").register();
    public static final RegistryEntry<BloodDebtTrait> blood_debt = L2Hostility.REGISTRATE.regTrait("blood_debt", () -> new BloodDebtTrait(()->0xdc0000), (rl) -> new TraitConfig(rl, 80, 30, 3, 200))
            .desc("The mob's attack will only deal 50% of the damage, while the other 50% will cause damage in a bleeding manner. It will ignore armor and magic defenses and increase the damage by %s%").lang("Blood Debt").register();
    public static final RegistryEntry<TitanBloodlineTrait> titan_bloodline = L2Hostility.REGISTRATE.regTrait("titan_bloodline", () -> new TitanBloodlineTrait(()->0x00dc12), (rl) -> new TraitConfig(rl, 300, 20, 3, 600))
            .desc("The damage caused increases by 5% of mob's maximum life.When the mob takes damage, it will increase its current maximum health by %s%, up to a maximum of 10 times").lang("Titan Bloodline").register();
    public static final RegistryEntry<ShowSwordTrait> show_sword = L2Hostility.REGISTRATE.regTrait("show_sword", () -> new ShowSwordTrait(()->0xdcfff8), (rl) -> new TraitConfig(rl, 150, 10, 3, 300))
            .desc("The damage caused by the mob increases by %2$s%, and they are immune to damage from creatures that are more than %1$s apart, but cannot deal damage to them").lang("Show Sword").register();
    public static final RegistryEntry<DevouringLifeTrait> devouring_life = L2Hostility.REGISTRATE.regTrait("devouring_life", () -> new DevouringLifeTrait(()->0x656565), (rl) -> new TraitConfig(rl, 20, 30, 5, 60))
            .desc("The damage caused by the mob will heal themselves according to their %s% value").lang("Devouring Life").register();
    public static final RegistryEntry<HarvestSharingTrait> harvest_sharing = L2Hostility.REGISTRATE.regTrait("harvest_sharing", () -> new HarvestSharingTrait(()->0xff5fdf), (rl) -> new TraitConfig(rl, 60, 20, 5, 200))
            .desc("If any creature within 20 squares recovers its life, it will recover its own life equivalent to %s% of its recovery amount").lang("Harvest Sharing").register();
    public static final RegistryEntry<CriticalHitTrait> critical_hit = L2Hostility.REGISTRATE.regTrait("critical_hit", () -> new CriticalHitTrait(()->0x925fff), (rl) -> new TraitConfig(rl, 120, 40, 3, 300))
            .desc("There is a %2$s% chance that each attack will increase the damage by %1$s%").lang("Critical Hit").register();
    public static final RegistryEntry<VectorStanceTrait> vector_stance = L2Hostility.REGISTRATE.regTrait("vector_stance", () -> new VectorStanceTrait(()->0x4bb1ff), (rl) -> new TraitConfig(rl, 60, 70, 3, 100))
            .desc("Organisms located in front of themselves will cause %s% less damage to themselves").lang("Vector Stance").register();
    public static final RegistryEntry<AbsorbManaTrait> absorb_mana = L2Hostility.REGISTRATE.regTrait("absorb_mana", () -> new AbsorbManaTrait(()->0x49ff39), (rl) -> new TraitConfig(rl, 150, 30, 3, 400))
            .desc("Continuously absorb the magic of items stored in the player's backpack within %1$s% squares at a rate of %3$s + the total magic limit of the item %2$s% per second").lang("Absorb Mana").register();


    public static final RegistryEntry<MagalaErode> magala_erode = L2Hostility.REGISTRATE.regTrait("magala_erode", () -> new MagalaErode(()->0x620591), (rl) -> new TraitConfig(rl, 50, 40, 1, 150))
            .desc("The self has overcome bloodlust and when attacking or being injured, inflicts bloodlust on the target").lang("Magala Erode").register();
    public static final RegistryEntry<BrokenArmor> broken_armor = L2Hostility.REGISTRATE.regTrait("broken_armor", () -> new BrokenArmor(()->0x620591), (rl) -> new TraitConfig(rl, 20, 60, 5, 40))
            .desc("Attacks apply %s-second armor-braking effect that reduces your armor value. Reapplying it will increase its level and extend its duration.").lang("Broken Armor").register();

    public static void register() {
    }
}
