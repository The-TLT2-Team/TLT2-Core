package com.fg.tltmod.SomeModifiers.integration.botania;

import com.c2h6s.etstlib.util.EntityInRangeUtil;
import com.c2h6s.etstlib.util.ProjectileUtil;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.content.hook.modifier.ModifyBurstModifierHook;
import com.fg.tltmod.content.hook.modifier.UpdateBurstModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

public class ExcaliburBurst extends NoLevelsModifier implements ModifyBurstModifierHook, UpdateBurstModifierHook {
    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST,TltCoreModifierHook.UPDATE_BURST);
    }

    @Override
    public void modifyBurst(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, ToolStack dummyLens) {
        burst.entity().setDeltaMovement(burst.entity().getDeltaMovement().scale(1.5));
        burst.setColor(0xFFFF20);
        burst.setMana(burst.getMana()+50);
        var burstEntity = (IManaBurstMixin) burst;
        burstEntity.tltmod$setBaseDamage(burstEntity.tltmod$getBaseDamage()+9f);
        burstEntity.tltmod$setPerConsumption(burstEntity.tltmod$getPerConsumption()+50);
    }

    @Override
    public void updateBurst(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst) {
        var entity = EntityInRangeUtil.getNearestLivingEntity(burst.entity(),8,((IManaBurstMixin)burst).tltmod$getHitEntityIdList(),entity1 -> canHitEntity(entity1, (ManaBurstEntity) burst));
        if (entity!=null) ProjectileUtil.homingToward(burst.entity(),entity,1,4);
    }

    protected static boolean canHitEntity(Entity pTarget, ManaBurstEntity entity) {
        if (pTarget==entity.getOwner()) return false;
        if (((IManaBurstMixin)entity).tltmod$getHitEntityIdList().contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity ||pTarget instanceof ExperienceOrb) return false;
        if (pTarget instanceof Projectile) return false;
        return !(pTarget instanceof Player);
    }
}
