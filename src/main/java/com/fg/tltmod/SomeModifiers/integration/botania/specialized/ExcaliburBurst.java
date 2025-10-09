package com.fg.tltmod.SomeModifiers.integration.botania.specialized;

import com.c2h6s.etstlib.util.EntityInRangeUtil;
import com.c2h6s.etstlib.util.ProjectileUtil;
import com.fg.tltmod.SomeModifiers.integration.botania.base.SpecializedBurstModifier;
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
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.entity.ManaBurstEntity;

import java.util.List;

public class ExcaliburBurst extends SpecializedBurstModifier implements ModifyBurstModifierHook, UpdateBurstModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, TltCoreModifierHook.MODIFY_BURST,TltCoreModifierHook.UPDATE_BURST);
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstMixin burstExtras, ToolStack dummyLens) {
        burst.entity().setDeltaMovement(burst.entity().getDeltaMovement().scale(1.5));
        burst.setColor(0xFFFF20);
        burst.setMana(burst.getMana()+100);
        burstExtras.addBaseDamage(16);
        burstExtras.addEntityPerConsumption(50);
    }

    @Override
    public void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst,IManaBurstMixin burstExtra) {
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
