package com.fg.tltmod.SomeModifiers;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.OnHoldingPreventDeathHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import com.fg.tltmod.util.mixin.ILivingEntityMixin;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import mekanism.common.registries.MekanismDamageTypes;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;
import java.util.UUID;

public class EverFlamingCore extends EtSTBaseModifier implements OnHoldingPreventDeathHook , ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, EtSTLibHooks.PREVENT_DEATH, ModifierHooks.MODIFY_HURT);
    }
    public static final ResourceLocation KEY_CD = TltCore.getResource("ever_flaming_cd");
    public static final UUID EVER_FLAME_UUID = UUID.fromString("59a9cb4a-e5f2-f7fe-e41f-1b87368dfa29");

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getTarget() instanceof LivingEntity living){
            var ds = living.damageSources();
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
                living.hurt(source,damage*0.2f);
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
            holder.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST,400,10*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.REGENERATION,400,5*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,400,10*modifier.getLevel()-1,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.SATURATION,400,0,false,false));
            holder.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,400,3,false,false));
        }
    }

    @Override
    public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource) {
        if (iToolStackView.getDamage()<=iToolStackView.getCurrentDurability()*9){
            iToolStackView.setDamage((int) (iToolStackView.getDamage()-0.1f*(iToolStackView.getDamage()+iToolStackView.getCurrentDurability())));
            livingEntity.invulnerableTime = 200;
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
        if (!level.isClientSide&&player.getAttackStrengthScale(0)>0.9){
            createSlash(player,tool,level);
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
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
}
