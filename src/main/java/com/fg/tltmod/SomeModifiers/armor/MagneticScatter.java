package com.fg.tltmod.SomeModifiers.armor;

import com.c2h6s.etstlib.entity.specialDamageSources.LegacyDamageSource;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.util.ParticleContext;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class MagneticScatter extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_MAGNETIC_SCATTER = TinkerDataCapability.TinkerDataKey.of(TltCore.getResource("magnetic_scatter"));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_MAGNETIC_SCATTER,false,null));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if (!event.getSource().is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)&&event.getEntity().level() instanceof ServerLevel serverLevel){
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
                int level = Math.min(7,cap.get(KEY_MAGNETIC_SCATTER,0));
                if (level>0){
                    event.setAmount(event.getAmount()-0.1f*level);
                    event.getEntity().level().getEntitiesOfClass(LivingEntity.class,event.getEntity().getBoundingBox().inflate(4+level/2f),
                            living -> !(living instanceof Player)).forEach(living -> {
                                living.hurt(LegacyDamageSource.thorns(event.getEntity())
                                        .setBypassArmor().setBypassMagic().setBypassEnchantment(),
                                        event.getAmount()*0.1f*level);
                                ParticleContext.buildParticle(ACParticleRegistry.AZURE_SHIELD_LIGHTNING.get())
                                        .setPos(event.getEntity().getBoundingBox().getCenter())
                                        .setVelocity(living.getBoundingBox().getCenter())
                                        .build().sendToClient(serverLevel);
                            });
                }
            });
        }
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        world.getEntitiesOfClass(Projectile.class,holder.getBoundingBox().inflate(2+modifier.getLevel()*2),
                projectile -> !(projectile.getOwner()instanceof Player)).forEach(projectile -> {
            Vec3 force = projectile.position().subtract(holder.position()).normalize()
                    .scale(1/Math.pow(projectile.distanceTo(holder)/modifier.getLevel(),2));
            projectile.setDeltaMovement(projectile.getDeltaMovement().add(force));
        });
    }
}
