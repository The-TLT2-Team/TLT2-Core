package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.UUIDUtil;
import com.fg.tltmod.util.TltcoreConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.UUID;
import java.util.function.BiConsumer;

public class ValkyrieBless extends EtSTBaseModifier implements AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
        hookBuilder.addModule(new ModifierTraitModule(TinkerModifiers.expanded.getId(), 1,false));
    }

    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        if (arrow.isCritArrow()&&attacker!=null){
            Level level = arrow.level();
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
            if (attacker instanceof ServerPlayer serverPlayer) bolt.setCause(serverPlayer);
            bolt.addTag(TltcoreConstants.NbtLocations.KEY_TOOL_LIGHTNING_BOLT);
            bolt.setDamage(damageDealt);
            bolt.setPos(target.position());
            level.addFreshEntity(bolt);
        }
    }
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.isFullyCharged()){
            Level level = context.getLevel();
            LivingEntity attacker = context.getAttacker();
            Entity target = context.getTarget();
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
            if (attacker instanceof ServerPlayer serverPlayer) bolt.setCause(serverPlayer);
            bolt.addTag(TltcoreConstants.NbtLocations.KEY_TOOL_LIGHTNING_BOLT);
            bolt.setDamage(damageDealt);
            bolt.setPos(target.position());
            level.addFreshEntity(bolt);
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        UUID uuid = UUIDUtil.UUIDFromSlot(slot,modifier.getId());
        if (slot==EquipmentSlot.MAINHAND){
            consumer.accept(ForgeMod.ENTITY_REACH.get(),new AttributeModifier(uuid,"desc.tltmod.valkyrie_bless",modifier.getLevel(), AttributeModifier.Operation.ADDITION));
            consumer.accept(ForgeMod.BLOCK_REACH.get(),new AttributeModifier(uuid,"desc.tltmod.valkyrie_bless",modifier.getLevel(), AttributeModifier.Operation.ADDITION));
        }
    }
}
