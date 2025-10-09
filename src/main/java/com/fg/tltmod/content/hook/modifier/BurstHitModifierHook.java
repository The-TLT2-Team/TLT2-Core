package com.fg.tltmod.content.hook.modifier;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.util.AttackUtil;
import com.fg.tltmod.SomeModifiers.integration.botania.FartherSights;
import com.fg.tltmod.content.hook.TltCoreModifierHook;
import com.fg.tltmod.util.mixin.IManaBurstMixin;
import com.fg.tltmod.util.mixin.IToolProvider;
import com.fg.tltmod.util.tinker.AttackLogicExtra;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.lens.Lens;
import vazkii.botania.common.item.lens.LensItem;

import java.util.Collection;
import java.util.List;

public interface BurstHitModifierHook {
    default void burstHitBlock(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, Level level, BlockPos blockPos, Direction direction, boolean isManaBlock, boolean shouldKill, ManaBurst burst,IManaBurstMixin burstExtra){}

    default void beforeBurstHitEntity(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst,IManaBurstMixin burstExtra,float damage){}

    default void afterBurstHitEntity(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst ,IManaBurstMixin burstExtra,float damage){}

    static boolean handleBurstHit(ManaBurst burst, HitResult result, boolean isManaBlock, boolean shouldKill, ItemStack stack){
        IToolStackView actualTool = ((IToolProvider)burst).tltmod$getTool();
        ToolStack tool = ToolStack.from(stack);
        List<ItemStack> list = LensProviderModifierHook.gatherLens(tool,burst);
        var burstExtra = (IManaBurstMixin) burst;
        boolean didHitBlock = false;
        boolean didHitEntity = false;
        boolean b = switch (result.getType()){
            case BLOCK -> {
                if (isManaBlock) yield true;
                BlockHitResult hitResult = (BlockHitResult) result;
                BlockPos pos = hitResult.getBlockPos();
                Direction direction = hitResult.getDirection();
                Level level = burst.entity().level();
                if (level.getBlockState(pos).is(Blocks.AIR)||!level.getFluidState(pos).isEmpty()) yield false;

                var modifierList = tool.getModifierList();

                for (var entry:tool.getModifierList()){
                    entry.getHook(TltCoreModifierHook.BURST_HIT).burstHitBlock(actualTool,entry,modifierList, burst.entity().getOwner(),level,pos,direction, false,shouldKill,burst,(IManaBurstMixin) burst);
                }
                didHitBlock = true;
                yield false;
            }
            case ENTITY -> {
                EntityHitResult hitResult = (EntityHitResult) result;
                Entity entity = hitResult.getEntity();
                var hitList = burstExtra.tltmod$getHitEntityIdList();
                hitList.add(entity.getId());
                Entity owner = burst.entity().getOwner();
                if (!(owner instanceof LivingEntity living)) yield false;
                var modifierList = tool.getModifierList();
                var burstEntity = burst.entity();
                float baseDamage = burstExtra.tltmod$getBaseDamage();
                if (baseDamage<=0||burst.getMana()<burstExtra.tltmod$getPerConsumption()) yield false;
                float damage = baseDamage;
                for (var entry:modifierList){
                    damage = entry.getHook(TltCoreModifierHook.BURST_DAMAGE).getBurstDamage(actualTool,entry,modifierList,living,entity,burst,(IManaBurstMixin) burst,baseDamage,damage);
                }
                float finalDamage = damage;
                modifierList.forEach(entry -> entry.getHook(TltCoreModifierHook.BURST_HIT).beforeBurstHitEntity(actualTool,entry,modifierList,living,entity,burst,(IManaBurstMixin) burst, finalDamage));
                float legacyHealth = entity instanceof LivingEntity target?target.getHealth():-1;
                if (burstEntity.getTags().contains(FartherSights.KEY_TRIGGER_TOOL)&&actualTool!=null){
                    if (AttackLogicExtra.attackEntityWithDamageModifier(actualTool,living, InteractionHand.MAIN_HAND,entity,()->1,false, EquipmentSlot.MAINHAND,
                            true, tool.getStats().get(ToolStats.ATTACK_DAMAGE)+1+finalDamage,burstExtra.tltmod$getDamageModifier(),true)){
                        didHitEntity = true;
                    }
                }
                else{
                    LegacyDamageSource source = LegacyDamageSource.mobAttack(living);
                    for (var entry:tool.getModifierList()) source = entry.getHook(TltCoreModifierHook.MODIFY_BURST_DAMAGE_SOURCE).modifyBurstSource(tool,burst,burstExtra,owner,entity,source);
                    if (entity.hurt(source,damage*burstExtra.tltmod$getDamageModifier())){
                        didHitEntity = true;
                    }
                }
                if (entity instanceof LivingEntity target) {
                    if (didHitEntity) {
                        float damageDealt = Math.max(legacyHealth - target.getHealth(), 0);
                        modifierList.forEach(entry -> entry.getHook(TltCoreModifierHook.BURST_HIT).afterBurstHitEntity(actualTool, entry, modifierList, living, target, burst, (IManaBurstMixin) burst, damageDealt));
                    } else {
                        IManaBurstMixin burstMixin = (IManaBurstMixin)burst;
                        float damageAttempt = burstMixin.tltmod$getBaseDamage()*burstMixin.tltmod$getDamageModifier();
                        modifierList.forEach(entry -> entry.getHook(TltCoreModifierHook.BURST_HIT).afterBurstHitEntity(actualTool, entry, modifierList, living, target, burst, (IManaBurstMixin) burst, damageAttempt));
                    }
                }
                yield false;
            }
            default -> false;
        };
        if (!b){
            list.forEach(lensStack ->{
                if (lensStack.getItem() instanceof LensItem lens) lens.collideBurst(burst,result,isManaBlock,shouldKill,lensStack);
            });
        }
        if (!isManaBlock){
            switch (result.getType()){
                case ENTITY -> {
                    if (didHitEntity){
                        burst.setMana(burst.getMana()-burstExtra.tltmod$getPerConsumption());
                    }
                }
                case BLOCK -> {
                    if (didHitBlock){
                        burst.setMana(burst.getMana()-burstExtra.tltmod$getPerBlockConsumption());
                    }
                }
                default -> {}
            }
        }
        b = b||burst.getMana()<=0;
        return b;
    }

    record merger(Collection<BurstHitModifierHook> modules) implements BurstHitModifierHook {
        @Override
        public void burstHitBlock(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, Level level, BlockPos blockPos, Direction direction, boolean isManaBlock, boolean shouldKill, ManaBurst burst,IManaBurstMixin burstExtra) {
            this.modules.forEach(hook->hook.burstHitBlock(tool,modifier,modifierList,owner,level,blockPos,direction,isManaBlock,shouldKill,burst,(IManaBurstMixin) burst));
        }

        @Override
        public void beforeBurstHitEntity(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst,IManaBurstMixin burstExtra, float damage) {
            this.modules.forEach(hook->hook.beforeBurstHitEntity(tool,modifier,modifierList,owner,target,burst,(IManaBurstMixin) burst,damage));
        }

        @Override
        public void afterBurstHitEntity(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull LivingEntity target, ManaBurst burst,IManaBurstMixin burstExtra, float damage) {
            this.modules.forEach(hook->hook.afterBurstHitEntity(tool,modifier,modifierList,owner,target,burst,(IManaBurstMixin) burst,damage));
        }
    }
}
