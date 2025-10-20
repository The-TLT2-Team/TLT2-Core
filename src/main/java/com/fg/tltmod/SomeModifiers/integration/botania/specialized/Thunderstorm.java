package com.fg.tltmod.SomeModifiers.integration.botania.specialized;

import cofh.core.client.particle.options.CylindricalParticleOptions;
import cofh.core.init.CoreParticles;
import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.hooks.ModifyDamageSourceModifierHook;
import com.c2h6s.etstlib.util.DynamicComponentUtil;
import com.fg.tltmod.SomeModifiers.integration.botania.base.SpecializedBurstModifier;
import com.fg.tltmod.content.entity.ThunderBurstEntity;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.BurstHitModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstDamageSourceModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.content.item.DummyToolManaLens;
import com.fg.tltmod.util.ParticleContext;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;

import java.util.List;

import static com.fg.tltmod.SomeModifiers.integration.botania.FartherSights.KEY_TRIGGER_TOOL;

public class Thunderstorm extends SpecializedBurstModifier implements BurstHitModifierHook, ModifyBurstModifierHook , ModifyBurstDamageSourceModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST,TltCoreModifierHook.BURST_HIT,TltCoreModifierHook.MODIFY_BURST_DAMAGE_SOURCE);
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
        burstExtras.addEntityPerConsumption(50);
        burstExtras.addBaseDamage(16);
        burst.setMana(burst.getMana()+200);
        burst.setColor(0xABFFE8);
    }

    @Override
    public LegacyDamageSource modifySourceWhenTriggerTool(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
        source.setMsgId("tltmod.thunderstorm_"+RANDOM.nextInt(3)).setBypassArmor();
        return source;
    }

    @Override
    public void afterBurstHitEntity(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst,IManaBurstMixin burstExtra, float damage) {
        if (!(owner instanceof Player player))return;
        if (burstExtra.tltmod$getGeneration()>0) return;
        if (burst.getMana()<burstExtra.tltmod$getPerConsumption()*2) return;
        if (owner.level() instanceof ServerLevel serverLevel) ParticleContext
                .buildParticle(new CylindricalParticleOptions(CoreParticles.SHOCKWAVE.get(),
                        5,5,0,0xAEFFFAFF,2))
                .setVelocity(0,0,0).setPos(target.getBoundingBox().getCenter()).build().sendToClient(serverLevel);
        burst.entity().playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE);
        BurstProperties properties = new BurstProperties(burst.getStartingMana(),burst.getMinManaLoss(),
                burst.getManaLossPerTick(),burst.getBurstGravity(),1,0xFFFFFF);
        int i = 0;
        do {
            ThunderBurstEntity entity = new ThunderBurstEntity(player);
            entity.setMana(burstExtra.tltmod$getPerConsumption());
            entity.setStartingMana(entity.getMana());
            entity.setGravity(properties.gravity);
            entity.setManaLossPerTick(properties.manaLossPerTick);
            entity.setMinManaLoss(properties.ticksBeforeManaLoss);
            entity.setPos(target.position().add(RANDOM.nextFloat()*8-4,9,RANDOM.nextFloat()*8-4));
            entity.setDeltaMovement(0,-3,0);
            ItemStack dummyLens = DummyToolManaLens.getDummyLens((ToolStack) tool);
            entity.setSourceLens(dummyLens);
            entity.tltmod$setTool(tool);
            if (burst.entity().getTags().contains(KEY_TRIGGER_TOOL)){
                entity.addTag(KEY_TRIGGER_TOOL);
            }
            var extras = (IManaBurstMixin) entity;
            extras.tltmod$setBaseDamage(burstExtra.tltmod$getBaseDamage());
            extras.tltmod$setGeneration(1);
            extras.tltmod$setPerConsumption(50);
            extras.tltmod$setPerBlockConsumption(0);
            extras.tltmod$setDamageModifier(0.25f);
            owner.level().addFreshEntity(entity);
            burst.setMana(burst.getMana()-burstExtra.tltmod$getPerConsumption());
            i++;
        } while (burst.getMana()>=burstExtra.tltmod$getPerConsumption()&&i<8);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicComponentUtil.ScrollColorfulText.getColorfulText(getTranslationKey(),null,
                new int[]{0x85C2FF,0x85C2FF,0xD5FFFE},30,10,true);
    }

    @Override
    public @NotNull LegacyDamageSource modifyBurstSource(IToolStackView tool, ManaBurst burst, IManaBurstMixin burstExtra, @NotNull Entity owner, @NotNull Entity target, LegacyDamageSource source) {
        if (burstExtra.tltmod$getGeneration()>0) source.setMsgId("tltmod.thunderstorm_"+RANDOM.nextInt(3)).setBypassArmor();
        return source;
    }
}
