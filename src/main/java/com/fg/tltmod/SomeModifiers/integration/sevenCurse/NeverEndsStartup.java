package com.fg.tltmod.SomeModifiers.integration.sevenCurse;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.TltCore;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class NeverEndsStartup extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_NEVER_END = TltCore.createKey("never_end");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_NEVER_END,false,null));
    }

    @SubscribeEvent
    public static void onTeleport(EntityTeleportEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player) return;
        AABB aabb = entity.getBoundingBox().inflate(16);
        var list = entity.level().getEntitiesOfClass(Player.class,aabb);
        for (Player player : list){
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
                if (cap.get(KEY_NEVER_END, 0) > 0) event.setCanceled(true);
            });
            if (event.isCanceled()) break;
        }
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getAttacker() instanceof Player player&& SuperpositionHandler.isTheCursedOne(player)) {
            Entity entity = context.getTarget();
            if (entity instanceof EnderMan || entity instanceof Endermite) return damage * 5;
        }
        return damage;
    }
}
