package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.c2h6s.etstlib.util.UUIDUtil;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.util.TltmodCommonUtil;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class DevourerOfDragons extends EtSTBaseModifier implements AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        UUID uuid = UUIDUtil.UUIDFromSlot(slot,modifier.getId());
        int kills = getKills(tool);
        if (slot.isArmor()){
            consumer.accept(Attributes.MAX_HEALTH,
                    new AttributeModifier(uuid, TltmodCommonUtil.makeAttributeDesc(Attributes.MAX_HEALTH),
                            kills, AttributeModifier.Operation.ADDITION));
            consumer.accept(Attributes.ARMOR,
                    new AttributeModifier(uuid, TltmodCommonUtil.makeAttributeDesc(Attributes.ARMOR),
                            kills*0.25, AttributeModifier.Operation.ADDITION));
            consumer.accept(Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(uuid, TltmodCommonUtil.makeAttributeDesc(Attributes.ARMOR_TOUGHNESS),
                            kills*0.1, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return damage+getKills(tool)*2;
    }

    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        persistentData.putInt(KEY_DRAGON_KILLED,getKills(tool));
    }

    @Override
    public float onGetArrowDamage(ModDataNBT persistentData, ModifierEntry entry, ModifierNBT modifiers, AbstractArrow arrow, @Nullable LivingEntity attacker, @NotNull Entity target, float baseDamage, float damage) {
        int kills = persistentData.getInt(KEY_DRAGON_KILLED);
        if (kills>0){
            damage+=kills;
        }
        return damage;
    }

    public static final ResourceLocation KEY_DRAGON_KILLED = TltCore.getResource("dragon_killed");
    private static final int MAX_KILL = 404;

    public static void updateKills(IToolStackView tool, ServerPlayer serverPlayer){
        var stats = serverPlayer.getStats();
        var count = new AtomicInteger(0);
        List.of(IafEntityRegistry.FIRE_DRAGON.get(),
                IafEntityRegistry.ICE_DRAGON.get(),
                IafEntityRegistry.LIGHTNING_DRAGON.get()).forEach(entityType ->
                count.addAndGet(stats.getValue(Stats.ENTITY_KILLED.get(entityType))));
        tool.getPersistentData().putInt(KEY_DRAGON_KILLED,Math.min(count.get(),MAX_KILL));
    }
    public static int getKills(IToolStackView tool){
        return tool.getPersistentData().getInt(KEY_DRAGON_KILLED);
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isCorrectSlot&&holder instanceof ServerPlayer serverPlayer){
            if (world.getGameTime()%40==0){
                updateKills(tool,serverPlayer);
            }
        }
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        return super.getDisplayName().copy().append(" ["+getKills(tool)+"]");
    }
}
