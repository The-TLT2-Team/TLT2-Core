package com.fg.tltmod.SomeModifiers.ranged;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.ToolEnergyUtil;
import com.fg.tltmod.Register.TltCoreItems;
import com.fg.tltmod.SomeModifiers.ranged.base.SpecializedRangedModifier;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.LaserBeamArrowEntity;
import com.github.L_Ender.cataclysm.init.ModSounds;
import com.github.L_Ender.cataclysm.util.CMDamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.function.Predicate;

import static com.fg.tltmod.util.TltcoreConstants.NbtLocations.KEY_NOT_PRIMARY;

public class RedstoneBeam extends SpecializedRangedModifier implements BowAmmoModifierHook {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.BOW_AMMO);
    }

    public static final ResourceLocation KEY_DAMAGE_TYPE = TltCore.getResource("laser_damage");
    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (!primary&& projectile instanceof LaserBeamArrowEntity){
            persistentData.putBoolean(KEY_NOT_PRIMARY,true);
            if (RANDOM.nextBoolean()){
                persistentData.putInt(KEY_DAMAGE_TYPE,1);
            } else if (RANDOM.nextBoolean()) persistentData.putInt(KEY_DAMAGE_TYPE,2);
        }
    }

    @Override
    public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable Entity target, LegacyDamageSource source) {
        int dt = persistentData.getInt(KEY_DAMAGE_TYPE);
        if (dt>0) {
            var lds = dt==1 ? LegacyDamageSource.any(CMDamageTypes.causeLaserDamage(arrow, attacker)) : LegacyDamageSource.any(CMDamageTypes.causeDeathLaserDamage(arrow,attacker));
            lds.damageTypes = source.damageTypes;
            lds.setProjectile().setBypassInvulnerableTime();
            return lds;
        }
        return source.setBypassInvulnerableTime();
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        if (persistentData.getBoolean(KEY_NOT_PRIMARY)) return damage*0.2f;
        return damage;
    }

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo, Predicate<ItemStack> ammoPredicate) {
        int energy = ToolEnergyCapability.getEnergy(tool);
        if (energy>=100){
            return new ItemStack(TltCoreItems.LASER_ARROW.get(),energy/100);
        }
        return standardAmmo;
    }

    @Override
    public void shrinkAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack ammo, int needed) {
        if (!ammo.is(TltCoreItems.LASER_ARROW.get())) BowAmmoModifierHook.super.shrinkAmmo(tool,modifier,shooter,ammo,needed);
        else {
            ToolEnergyUtil.extractEnergy(tool,needed*100,false);
        }
    }
}
