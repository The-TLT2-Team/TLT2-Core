package com.fg.tltmod.SomeModifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.OnHoldingPreventDeathHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.DynamicComponentUtil;
import com.c2h6s.tinkers_advanced.TinkersAdvanced;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import com.fg.tltmod.util.mixin.ILivingEntityMixin;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import mekanism.common.registries.MekanismDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.LootingModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.*;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.RomanNumeralHelper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EverFlamingCore extends TotalRagnarokDefense implements OnHoldingPreventDeathHook , ModifyDamageModifierHook , VolatileDataModifierHook, ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PREVENT_DEATH, ModifierHooks.MODIFY_HURT,ModifierHooks.TOOL_STATS,ModifierHooks.VOLATILE_DATA);
        hookBuilder.addModule(LootingModule.builder().level(10).weapon());
        hookBuilder.addModule(LootingModule.builder().level(10).armor());
        hookBuilder.addModule(new EnchantmentModule.Constant(Enchantments.BLOCK_FORTUNE, 10));
        hookBuilder.addModule(new EnchantmentModule.ArmorHarvest(Enchantments.BLOCK_FORTUNE, 10, ModifierCondition.ANY_TOOL, Set.of(EquipmentSlot.CHEST,EquipmentSlot.HEAD,EquipmentSlot.LEGS,EquipmentSlot.FEET), BlockPredicate.ANY, LivingEntityPredicate.ANY));
    }
    public static final ResourceLocation KEY_CD = TltCore.getResource("ever_flaming_cd");
    public static final UUID EVER_FLAME_UUID = UUID.fromString("59a9cb4a-e5f2-f7fe-e41f-1b87368dfa29");
    public float cachedDamage=0;

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        List.of(SlotType.DEFENSE,SlotType.UPGRADE,SlotType.ABILITY).forEach(slotType -> volatileData.addSlots(slotType,modifier.getLevel()*2));
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.getAllStats().forEach(iToolStat -> {
            if (iToolStat instanceof INumericToolStat<?> toolStat &&(iToolStat.getName().getNamespace().equals(TConstruct.MOD_ID)||iToolStat.getName().getNamespace().equals(TinkersAdvanced.MODID))){
                toolStat.percent(builder,0.5*modifier.getLevel());
            }
        });
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (damage<cachedDamage) damage = cachedDamage;
        if (context.getTarget() instanceof LivingEntity living){
            var ds = living.damageSources();
            float finalDamage = damage;
            List.of(LegacyDamageSource.indirectMagic(living),
                    LegacyDamageSource.explosion(living,living),
                    LegacyDamageSource.any(ds.mobProjectile(living,living)),
                    LegacyDamageSource.any(ds.lightningBolt().typeHolder(),living),
                    LegacyDamageSource.any(ds.dragonBreath().typeHolder(),living),
                    LegacyDamageSource.any(ds.wither().typeHolder(),living),
                    LegacyDamageSource.any(ds.fallingBlock(living).typeHolder(),living),
                    LegacyDamageSource.any(MekanismDamageTypes.LASER.source(living.level()).typeHolder(),living),
                    LegacyDamageSource.any(MekanismDamageTypes.RADIATION.source(living.level()).typeHolder(),living),
                    LegacyDamageSource.any(IafDamageRegistry.causeDragonFireDamage(living)),
                    LegacyDamageSource.any(IafDamageRegistry.causeDragonLightningDamage(living)),
                    LegacyDamageSource.any(IafDamageRegistry.causeDragonIceDamage(living))
            ).forEach(source -> {
                source.setBypassInvulnerableTime().setBypassMagic().setBypassShield().setBypassEnchantment().setBypassArmor();
                living.invulnerableTime = 0;
                living.hurt(source, finalDamage *0.2f);
            });
            if (!(living instanceof Player)) {
                List.of(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS).forEach(attribute -> {
                    var instance = living.getAttribute(attribute);
                    if (instance != null) {
                        if (instance.getModifier(EVER_FLAME_UUID) != null) instance.removeModifier(EVER_FLAME_UUID);
                        instance.addTransientModifier(new AttributeModifier(EVER_FLAME_UUID, attribute.getDescriptionId(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
                    }
                });
                var instance = living.getAttribute(Attributes.MAX_HEALTH);
                if (instance != null) {
                    double d0 = 0;
                    if (instance.getModifier(EVER_FLAME_UUID) != null) {
                        d0 += instance.getModifier(EVER_FLAME_UUID).getAmount();
                        instance.removeModifier(EVER_FLAME_UUID);
                    }
                    instance.addTransientModifier(new AttributeModifier(EVER_FLAME_UUID,Attributes.MAX_HEALTH.getDescriptionId(), d0-living.getMaxHealth()*0.05, AttributeModifier.Operation.ADDITION));
                }
                if (context.getAttacker() instanceof Player player) {
                    if (!living.isDeadOrDying())
                        ((ILivingEntityMixin) living).tltmod$hurt(LegacyDamageSource.playerAttack(player),living.getMaxHealth()*0.05f);
                }
            }
            living.invulnerableTime = 0;
        }
    }

    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        var ds = target.damageSources();
        List.of(LegacyDamageSource.indirectMagic(target),
                LegacyDamageSource.explosion(target,target),
                LegacyDamageSource.any(ds.mobProjectile(target,target)),
                LegacyDamageSource.any(ds.lightningBolt().typeHolder(),target),
                LegacyDamageSource.any(ds.dragonBreath().typeHolder(),target),
                LegacyDamageSource.any(ds.wither().typeHolder(),target),
                LegacyDamageSource.any(ds.fallingBlock(target).typeHolder(),target),
                LegacyDamageSource.any(MekanismDamageTypes.LASER.source(target.level()).typeHolder(),target),
                LegacyDamageSource.any(MekanismDamageTypes.RADIATION.source(target.level()).typeHolder(),target),
                LegacyDamageSource.any(IafDamageRegistry.causeDragonFireDamage(target)),
                LegacyDamageSource.any(IafDamageRegistry.causeDragonLightningDamage(target)),
                LegacyDamageSource.any(IafDamageRegistry.causeDragonIceDamage(target))
        ).forEach(source -> {
            source.setBypassInvulnerableTime().setBypassMagic().setBypassShield().setBypassEnchantment().setBypassArmor();
            target.hurt(source,damageDealt*0.2f);
        });
        if (!(target instanceof Player)) {
            List.of(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS).forEach(attribute -> {
                var instance = target.getAttribute(attribute);
                if (instance != null) {
                    if (instance.getModifier(EVER_FLAME_UUID) != null) instance.removeModifier(EVER_FLAME_UUID);
                    instance.addTransientModifier(new AttributeModifier(EVER_FLAME_UUID, attribute.getDescriptionId(), 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            });
            var instance = target.getAttribute(Attributes.MAX_HEALTH);
            if (instance != null) {
                double d0 = 0;
                if (instance.getModifier(EVER_FLAME_UUID) != null) {
                    d0 += instance.getModifier(EVER_FLAME_UUID).getAmount();
                    instance.removeModifier(EVER_FLAME_UUID);
                }
                instance.addTransientModifier(new AttributeModifier(EVER_FLAME_UUID,Attributes.MAX_HEALTH.getDescriptionId(), d0-target.getMaxHealth()*0.05, AttributeModifier.Operation.ADDITION));
            }
            if (attacker instanceof Player player) {
                if (!target.isDeadOrDying())
                    ((ILivingEntityMixin) target).tltmod$hurt(LegacyDamageSource.playerAttack(player),target.getMaxHealth()*0.05f);
            }
        }
        target.invulnerableTime = 0;
    }

    @Override
    public int modifierDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        return 0;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.getGameTime()%10==0&&isCorrectSlot){
            holder.addEffect(new MobEffectInstance(MobEffects.LUCK,400,10*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,400,10*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.REGENERATION,400,5*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,400,10*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.SATURATION,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,400,3,false,false));
            if (tool.getPersistentData().getInt(KEY_CD)>0){
                tool.getPersistentData().putInt(KEY_CD,tool.getPersistentData().getInt(KEY_CD)-1);
            }
        }
    }

    @Override
    public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource) {
        if (iToolStackView.getPersistentData().getInt(KEY_CD)>0){
            return 0;
        }
        if (iToolStackView.getDamage()<=iToolStackView.getCurrentDurability()*9){
            iToolStackView.setDamage((int) (iToolStackView.getDamage()-0.1f*(iToolStackView.getDamage()+iToolStackView.getCurrentDurability())));
            livingEntity.invulnerableTime = 200;
            iToolStackView.getPersistentData().putInt(KEY_CD,120);
            return livingEntity.getMaxHealth();
        }
        return 0;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        return amount*0.25f;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8){
            createSlash(player,tool,level);
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.8&&!tool.getItem().isCorrectToolForDrops(state)){
            createSlash(player,tool,level);
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        cachedDamage = damage;
        if (!context.isExtraAttack()&&context.isFullyCharged()){
            createSlash(context.getAttacker(),tool,context.getLevel());
        }
        return knockback;
    }

    public static void createSlash(LivingEntity living, IToolStackView tool, Level level){
        WaveSlashEntity entity = new WaveSlashEntity(level);
        entity.setPos(living.position().add(0,living.getBbHeight()/2,0));
        Vec3 direction = living.getLookAngle();
        entity.shoot(direction.x,direction.y,direction.z,2.5f,0);
        entity.setOwner(living);
        entity.tool = (slimeknights.tconstruct.library.tools.nbt.ToolStack) tool;
        level.addFreshEntity(entity);
    }

    @Override
    public Component getDisplayName(int level) {
        return DynamicComponentUtil.ScrollColorfulText.getColorfulText(getTranslationKey()," "+ RomanNumeralHelper.getNumeral(level).getString(),new int[]{0xFF9999,0xFFFF99},50,10,true);
    }
}
