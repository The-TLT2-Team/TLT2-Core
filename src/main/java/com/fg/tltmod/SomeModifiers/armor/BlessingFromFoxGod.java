package com.fg.tltmod.SomeModifiers.armor;

import com.fg.tltmod.TltCore;
import com.google.common.collect.Iterables;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STSlots;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.LootingModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class BlessingFromFoxGod extends BaseModifier implements DamageBlockModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA,ModifierHooks.DAMAGE_BLOCK);
        hookBuilder.addModule(new ArmorLevelModule(KEY_BLESS, false, TinkerTags.Items.MODIFIABLE));
        hookBuilder.addModule(LootingModule.builder().level(2).weapon());
        hookBuilder.addModule(new EnchantmentModule.Constant(Enchantments.BLOCK_FORTUNE, 2));
    }

    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_BLESS = TinkerDataCapability.TinkerDataKey.of(TltCore.getResource("fox_bless"));

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ToolDataNBT volatileData) {
        volatileData.addSlots(SlotType.UPGRADE, modifier.getLevel());
        volatileData.addSlots(SlotType.ABILITY, modifier.getLevel());
        if (context.hasTag(TinkerTags.Items.ARMOR)) {
            volatileData.addSlots(SlotType.DEFENSE,  modifier.getLevel());
            volatileData.addSlots(STSlots.SOUL_SAKURA, modifier.getLevel());
        }
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount) {
        return context.getEntity().invulnerableTime > 0
                || damageSource.is(DamageTypeTags.IS_EXPLOSION)
                || damageSource.is(DamageTypeTags.IS_FIRE)
                || damageSource.is(DamageTypeTags.IS_DROWNING)
                || damageSource.is(DamageTypeTags.IS_FREEZING)
                || damageSource.is(DamageTypeTags.IS_LIGHTNING)
                || damageSource.is(DamageTypeTags.WITCH_RESISTANT_TO)
                || damageSource.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isCorrectSlot || isSelected) {
            int level = modifier.getLevel();
            if (holder instanceof Player player) {
                List<ItemStack> items = ManaItemHandler.INSTANCE.getManaItems(player);
                List<ItemStack> acc = ManaItemHandler.INSTANCE.getManaAccesories(player);
                for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
                    if (stackInSlot == stack) {
                        continue;
                    }
                    var manaItem = XplatAbstractions.INSTANCE.findManaItem(stackInSlot);
                    if (manaItem != null && manaItem.canExportManaToItem(stack)) {
                        var requestor = XplatAbstractions.INSTANCE.findManaItem(stack);
                        if (requestor != null && !requestor.canReceiveManaFromItem(stackInSlot)) {
                            continue;
                        }
                        int maxMana = manaItem.getMaxMana();
                        int currentMana = manaItem.getMana();
                        if (currentMana < maxMana) {
                            manaItem.addMana(20 * level);
                        }
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public static void tryBlockDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getAmount();
        entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent( cap -> {
            int lvl = cap.get(KEY_BLESS,0);
            if (lvl > 0 && entity instanceof Player player) {
                List<ItemStack> items = ManaItemHandler.INSTANCE.getManaItems(player);
                List<ItemStack> acc = ManaItemHandler.INSTANCE.getManaAccesories(player);
                for (ItemStack stackInSlot : Iterables.concat(items, acc)) {
                    var manaItem = XplatAbstractions.INSTANCE.findManaItem(stackInSlot);
                    if (manaItem == null) continue;
                    int currentMana = manaItem.getMana();
                    if (currentMana <= 0) continue;
                    int cost = Mth.ceil(damage);
                    if (currentMana >= cost) {
                        manaItem.addMana(-cost*100);
                        event.setCanceled(true);
                        return;
                    } else {
                        manaItem.addMana(-currentMana);
                        float damageLeft = damage - currentMana;
                        event.setAmount(damageLeft);
                        return;
                    }
                }
            }
        });
    }
}
