package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerInstance;
import com.c2h6s.etstlib.content.misc.entityTicker.EntityTickerManager;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.Register.TltCoreEntityTickers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class GravityInterrupt extends EtSTBaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (!context.getLevel().isClientSide){
            var instance = EntityTickerManager.getInstance(context.getTarget());
            if (!instance.hasTicker(TltCoreEntityTickers.ZERO_GRAVITY_CD.get())){
                instance.addTicker(new EntityTickerInstance(TltCoreEntityTickers.ZERO_GRAVITY.get(), 1,modifier.getLevel()*50),
                        Integer::max,Integer::max);
            }
        }
    }

    @Override
    public void afterArrowHit(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull LivingEntity target, float damageDealt) {
        if (!target.level().isClientSide){
            var instance = EntityTickerManager.getInstance(target);
            if (!instance.hasTicker(TltCoreEntityTickers.ZERO_GRAVITY_CD.get())){
                instance.addTicker(new EntityTickerInstance(TltCoreEntityTickers.ZERO_GRAVITY.get(), 1,entry.getLevel()*50),
                        Integer::max,Integer::max);
            }
        }
    }
}
