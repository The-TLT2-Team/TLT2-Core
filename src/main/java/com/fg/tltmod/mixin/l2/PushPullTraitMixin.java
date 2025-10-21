package com.fg.tltmod.mixin.l2;

import com.fg.tltmod.util.tinker.ModifierModuleUtil;
import dev.xkmc.l2hostility.content.traits.legendary.PushPullTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = PushPullTrait.class,remap = false)
public abstract class PushPullTraitMixin {

    @Shadow protected abstract double getStrength(double v);

    @Shadow protected abstract int getRange();

    /**
     * @author EtSH_C2H6S
     * @reason 添加排斥、吸引的免疫
     */
    @Overwrite
    public void tick(LivingEntity mob, int level){
        int r = getRange();
        List<? extends LivingEntity> list;
        if (mob.level().isClientSide()) {
            list = mob.level().getEntities(EntityTypeTest.forClass(Player.class), mob.getBoundingBox().inflate(r), (ex) ->{
                boolean b = ex.isLocalPlayer() && !ex.getAbilities().instabuild && !ex.isSpectator();
                if (getStrength(0)>0){
                    b = b && !ModifierModuleUtil.getTraitImmunity(LHTraits.REPELLING.get(),ex);
                } else b = b && !ModifierModuleUtil.getTraitImmunity(LHTraits.PULLING.get(),ex);
                return b;
            } );
        } else {
            list = mob.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), mob.getBoundingBox().inflate(r), (ex) -> {
                if (getStrength(0)>0){
                    if (ModifierModuleUtil.getTraitImmunity(LHTraits.REPELLING.get(), ex)) return false;
                } else if (ModifierModuleUtil.getTraitImmunity(LHTraits.PULLING.get(), ex)) return false;
                boolean var10000;
                label28: {
                    if (ex instanceof Player pl) {
                        if (!pl.getAbilities().instabuild && !ex.isSpectator()) {
                            break label28;
                        }
                    }
                    if (ex instanceof Mob m) {
                        if (m.getTarget() == mob) {
                            break label28;
                        }
                    }
                    var10000 = false;
                    return var10000;
                }
                var10000 = true;
                return var10000;
            });
        }
        for(LivingEntity e : list) {
            double dist = mob.distanceTo(e) / (float)r;
            if (dist > (double)1.0F) {
                return;
            }
            if (!LHItems.ABRAHADABRA.get().isOn(e)) {
                double strength = this.getStrength(dist);
                int lv = 0;
                for(ItemStack armor : e.getArmorSlots()) {
                    lv += armor.getEnchantmentLevel(LHEnchantments.INSULATOR.get());
                }
                if (lv > 0) {
                    strength *= Math.pow(LHConfig.COMMON.insulatorFactor.get(), lv);
                }
                Vec3 vec = e.position().subtract(mob.position()).normalize().scale(strength);
                e.push(vec.x, vec.y, vec.z);
            }
        }
    }

}
