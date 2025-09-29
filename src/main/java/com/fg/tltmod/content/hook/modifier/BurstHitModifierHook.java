package com.fg.tltmod.content.hook.modifier;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.Collection;
import java.util.List;

public interface BurstHitModifierHook {
    default boolean burstHitBlock(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, Level level, BlockPos blockPos, Direction direction, boolean isManaBlock, boolean shouldKill, ManaBurst burst ){
        return shouldKill;
    }

    default void beforeBurstHitEntity(ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst,float damage){}

    default void afterBurstHitEntity(ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst ,float damage){}

    static boolean handleBurstHit(ManaBurst burst, HitResult result, boolean isManaBlock, boolean shouldKill, ItemStack stack){
        switch (result.getType()){
            case BLOCK -> {
                if (isManaBlock) return true;
                BlockHitResult hitResult = (BlockHitResult) result;
                BlockPos pos = hitResult.getBlockPos();
                Direction direction = hitResult.getDirection();
                Level level = burst.entity().level();
                if (level.getBlockState(pos).is(Blocks.AIR)||!level.getFluidState(pos).isEmpty()) return false;
                ToolStack tool = ToolStack.from(stack);
                var modifierList = tool.getModifierList();
                boolean b = false;
                for (var entry:tool.getModifierList()){
                    b = (entry.getHook(TltCoreModifierHook.BURST_HIT).burstHitBlock(entry,modifierList, burst.entity().getOwner(),level,pos,direction,isManaBlock,shouldKill,burst));
                    if (b) break;
                }
                burst.setMana(burst.getMana()-((IManaBurstMixin) burst).tltmod$getPerBlockConsumption());
                return b;
            }
            case ENTITY -> {
                EntityHitResult hitResult = (EntityHitResult) result;
                Entity entity = hitResult.getEntity();
                var hitList = ((IManaBurstMixin)burst.entity()).tltmod$getHitEntityIdList();
                hitList.add(entity.getId());
                Entity owner = burst.entity().getOwner();
                if (!(owner instanceof LivingEntity living)) return false;
                ToolStack tool = ToolStack.from(stack);
                var modifierList = tool.getModifierList();
                var burstEntity = burst.entity();
                float baseDamage = ((IManaBurstMixin)burstEntity).tltmod$getBaseDamage();
                if (baseDamage<=0||burst.getMana()<((IManaBurstMixin)burstEntity).tltmod$getPerConsumption()) return false;
                float damage = baseDamage;
                for (var entry:modifierList){
                    damage = entry.getHook(TltCoreModifierHook.BURST_DAMAGE).getBurstDamage(entry,modifierList,living,entity,burst,baseDamage,damage);
                }
                float finalDamage = damage;
                modifierList.forEach(entry -> entry.getHook(TltCoreModifierHook.BURST_HIT).beforeBurstHitEntity(entry,modifierList,living,entity,burst, finalDamage));
                float legacyHealth = entity instanceof LivingEntity target?target.getHealth():-1;
                if (entity.hurt(LegacyDamageSource.mobAttack(living).setBypassArmor(),damage)){
                    if (entity instanceof LivingEntity target){
                        float damageDealt =Math.max( legacyHealth-target.getHealth(),0);
                        modifierList.forEach(entry -> entry.getHook(TltCoreModifierHook.BURST_HIT).afterBurstHitEntity(entry,modifierList,living,target,burst, damageDealt));
                    }
                    burst.setMana(burst.getMana()-((IManaBurstMixin) burstEntity).tltmod$getPerConsumption());
                }
                return burst.getMana()<=0;
            }
            default -> {
                return false;
            }
        }
    }

    record merger(Collection<BurstHitModifierHook> modules) implements BurstHitModifierHook {
        @Override
        public boolean burstHitBlock(ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, Level level, BlockPos blockPos, Direction direction, boolean isManaBlock, boolean shouldKill, ManaBurst burst) {
            for (BurstHitModifierHook hook:this.modules){
                if (hook.burstHitBlock(modifier,modifierList,owner,level,blockPos,direction,isManaBlock,shouldKill,burst)) return true;
            }
            return false;
        }

        @Override
        public void beforeBurstHitEntity(ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst, float damage) {
            this.modules.forEach(hook->hook.beforeBurstHitEntity(modifier,modifierList,owner,target,burst,damage));
        }

        @Override
        public void afterBurstHitEntity(ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst, float damage) {
            this.modules.forEach(hook->hook.afterBurstHitEntity(modifier,modifierList,owner,target,burst,damage));
        }
    }
}
