package com.fg.tltmod.SomeModifiers.integration.arsnouveau;

import com.fg.tltmod.content.capability.compat.ars.CastToolCapabilitiesProvider;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CasterToolModifier extends BaseModifier {

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity entity = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();
        ItemStack stack = attacker.getMainHandItem();
        stack.getCapability(CastToolCapabilitiesProvider.CASTER_TOOL).ifPresent(casterTool -> {
            ISpellCaster caster = casterTool.getSpellCaster(stack);
            IWrappedCaster wrappedCaster = context.getAttacker() instanceof Player player
                    ? new PlayerCaster(player)
                    : new LivingCaster(context.getAttacker());
            SpellContext spellContext = new SpellContext(
                    attacker.level(),
                    caster.modifySpellBeforeCasting(attacker.level(), entity, InteractionHand.MAIN_HAND, caster.getSpell()),
                    entity,
                    wrappedCaster,
                    stack
            );
            SpellResolver resolver = context.getAttacker() instanceof Player
                    ? new SpellResolver(spellContext)
                    : new EntitySpellResolver(spellContext);
            resolver.onCastOnEntity(stack, entity, InteractionHand.MAIN_HAND);
        });
    }
}
