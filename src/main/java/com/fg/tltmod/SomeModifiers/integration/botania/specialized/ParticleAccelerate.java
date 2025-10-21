package com.fg.tltmod.SomeModifiers.integration.botania.specialized;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerInstance;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.util.DynamicComponentUtil;
import com.c2h6s.etstlib.util.EntityInRangeUtil;
import com.c2h6s.etstlib.util.ProjectileUtil;
import com.fg.tltmod.Register.TltCoreEntityTickers;
import com.fg.tltmod.SomeModifiers.integration.botania.base.SpecializedBurstModifier;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.BurstHitModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstDamageSourceModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.content.hook.modifier.UpdateBurstModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

public class ParticleAccelerate extends SpecializedBurstModifier implements BurstHitModifierHook, ModifyBurstModifierHook, ModifyBurstDamageSourceModifierHook, UpdateBurstModifierHook {
    public static String KEY_SCARLET = "particle_scarlet";
    public static String KEY_AZURE = "particle_azure";
    public static ResourceLocation KEY_BURST_PARTICLE = TltCore.getResource("particle_scarlet");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST_DAMAGE_SOURCE,TltCoreModifierHook.MODIFY_BURST,TltCoreModifierHook.BURST_HIT,TltCoreModifierHook.UPDATE_BURST);
    }

    @Override
    public void updateBurst(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtra) {
        var entity = EntityInRangeUtil.getNearestLivingEntity(burst.entity(),12,((IManaBurstMixin)burst).tltmod$getHitEntityIdList(), entity1 -> canHitEntity(entity1, (ManaBurstEntity) burst));
        if (entity!=null) ProjectileUtil.homingToward(burst.entity(),entity,1.2f,6);
    }
    protected static boolean canHitEntity(Entity pTarget, ManaBurstEntity entity) {
        if (!(pTarget instanceof LivingEntity)) return false;
        CompoundTag nbt = entity.getPersistentData();
        CompoundTag targetNbt = pTarget.getPersistentData();
        if (nbt.getBoolean(KEY_SCARLET)&&!targetNbt.getBoolean(KEY_AZURE)) return false;
        if (!nbt.getBoolean(KEY_SCARLET)&&!targetNbt.getBoolean(KEY_SCARLET)) return false;
        if (pTarget==entity.getOwner()) return false;
        if (((IManaBurstMixin)entity).tltmod$getHitEntityIdList().contains(pTarget.getId())) return false;
        return !(pTarget instanceof Player);
    }

    @Override
    public @NotNull LegacyDamageSource modifyBurstSource(IToolStackView tool, ManaBurst burst, IManaBurstMixin burstExtra, @NotNull Entity owner, @NotNull Entity target, LegacyDamageSource source) {
        return source.setBypassMagic().setBypassEnchantment().setMsgId("tltmod.magnetic_magic_"+burst.entity().getPersistentData().getByte(KEY_SCARLET));
    }

    @Override
    public void beforeBurstHitEntity(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst, IManaBurstMixin burstExtra, float damage) {
        if (target instanceof LivingEntity living){
            var instance = EntityTickerManager.getInstance(living);
            var scarletInstance = instance.getTicker(TltCoreEntityTickers.SCARLET_PARTICLE.get());
            var azureInstance = instance.getTicker(TltCoreEntityTickers.AZURE_PARTICLE.get());
            if ((scarletInstance!=null&&scarletInstance.duration>10)||(azureInstance!=null&&azureInstance.duration>10)){
                boolean targetScarlet = scarletInstance!=null&&scarletInstance.duration>10;
                if (targetScarlet!=burst.entity().getPersistentData().getBoolean(KEY_SCARLET)){
                    burstExtra.addDamageModifier(1);
                    EntityTicker ticker = targetScarlet?TltCoreEntityTickers.SCARLET_PARTICLE.get() : TltCoreEntityTickers.AZURE_PARTICLE.get();
                    instance.addTicker(new EntityTickerInstance(ticker, 1,10),Integer::min,Integer::min);
                }
            }
            EntityTicker ticker = burst.entity().getPersistentData().getBoolean(KEY_SCARLET)?TltCoreEntityTickers.SCARLET_PARTICLE.get() : TltCoreEntityTickers.AZURE_PARTICLE.get();
            instance.addTicker(new EntityTickerInstance(ticker,1,200),Integer::max,Integer::max);
            var key = burst.entity().getPersistentData().getBoolean(KEY_SCARLET)?KEY_SCARLET:KEY_AZURE;
            target.getPersistentData().putBoolean(key,true);
        }
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
        boolean isScarlet = tool.getPersistentData().getBoolean(KEY_BURST_PARTICLE);
        int color = isScarlet?0xFF2020:0x2020FF;
        String key = isScarlet?KEY_SCARLET:KEY_AZURE;
        burst.setColor(color);
        burst.entity().getPersistentData().putBoolean(key,true);
        burstExtras.addBaseDamage(16);
        burstExtras.addEntityPerConsumption(50);
        tool.getPersistentData().putBoolean(KEY_BURST_PARTICLE,!isScarlet);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicComponentUtil.ScrollColorfulText.getColorfulText(getTranslationKey(),null,new int[]{0xFF4040,0x4040FF},2,500,true);
    }
}
