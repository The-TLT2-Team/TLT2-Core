package com.fg.tltmod.SomeModifiers.armor;

import com.fuyun.cloudertinker.extend.superclass.ArmorModifier;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import util.method.ModifierLevel;

public class Mushroomgicprotect extends ArmorModifier {
    public boolean havenolevel() {
        return true;
    }
    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (ModifierLevel.EquipHasModifierlevel(event.getEntity(), this.getId())){
            if (event.getSource().is(DamageTypes.MAGIC)){
                event.setAmount(event.getAmount()*0.4f);
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 2 ));
            }
        }
    }
}
