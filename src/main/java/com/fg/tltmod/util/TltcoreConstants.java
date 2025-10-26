package com.fg.tltmod.util;

import com.c2h6s.tinkers_advanced.registery.TiAcToolStats;
import com.fg.tltmod.TltCore;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class TltcoreConstants {
    public static class NbtLocations{
        //使单个工具不被封印，以VolatileData的形式附加在匠魂工具上便能生效，这样无需遍历全词条。
        public static final ResourceLocation KEY_ANTI_RAGNAROK = TltCore.getResource("anti_ragnarok");
        //决定工具是否添加被诅咒者词条的路径
        public static final ResourceLocation KEY_IS_THE_CURSED_ONE = TltCore.getResource("is_the_cursed_one");
        //闪电的额外标签
        //工具召唤的闪电
        public static final String KEY_TOOL_LIGHTNING_BOLT = "tool_lightning";
        //闪电无视无敌帧
        public static final String KEY_IGNORE_COOLDOWN = "ignore_cooldown";
        //造成额外的玩家伤害
        public static final String KEY_EXTRA_PLAYER_DAMAGE = "deal_player_damage";
    }
    public static class TinkerDataKeys{
        //实体带有这个data后全身物品都不会被封印，用ArmorLevelingModule来添加。
        public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_TOTAL_ANTI_RAGNAROK = TinkerDataCapability.TinkerDataKey.of(TltCore.getResource("total_anti_ragnarok"));
    }
    public static class TagKeys {
    }

    public static final List<? extends INumericToolStat<?>> LIST_GENERAL_STATS_FOR_BONUS = List.of(
            ToolStats.ATTACK_DAMAGE,
            ToolStats.BLOCK_ANGLE,
            ToolStats.KNOCKBACK_RESISTANCE,
            ToolStats.ARMOR,
            ToolStats.ACCURACY,
            ToolStats.PROJECTILE_DAMAGE,
            ToolStats.ARMOR_TOUGHNESS,
            ToolStats.ATTACK_SPEED,
            ToolStats.BLOCK_AMOUNT,
            ToolStats.DRAW_SPEED,
            ToolStats.DURABILITY,
            ToolStats.MINING_SPEED,
            ToolStats.VELOCITY,
            TiAcToolStats.FLUID_EFFICIENCY,
            TiAcToolStats.POWER_MULTIPLIER,
            TiAcToolStats.RANGE,
            TiAcToolStats.SCALE,
            STToolStats.ARMOR,
            STToolStats.ARMOR_TOUGHNESS,
            STToolStats.RANGE,
            STToolStats.ATTACK_BUFF_TIME,
            STToolStats.DEFENCE_BUFF_TIME,
            STToolStats.MAX_HEALTH
    );

    public static boolean projectileShouldHit(Entity target){
        return !(target instanceof ItemEntity)&&!(target instanceof ExperienceOrb);
    }
}
