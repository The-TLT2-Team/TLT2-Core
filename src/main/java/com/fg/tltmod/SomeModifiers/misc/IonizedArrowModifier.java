package com.fg.tltmod.SomeModifiers.misc;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.tool.modifiers.base.EtSTBaseModifier;
import com.fg.tltmod.Register.TltCoreItems;
import com.fg.tltmod.Register.TltCoreModifiers;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.IonizedArrowEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class IonizedArrowModifier extends EtSTBaseModifier implements BowAmmoModifierHook, CustomBarDisplayModifierHook,ConditionalStatModifierHook {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    public static final ResourceLocation KEY_COOLDOWN = TltCore.getResource("ionized_arrow_cooldown");
    public static final ResourceLocation KEY_ARROW_SHOT = TltCore.getResource("ionized_arrow_shot");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.BOW_AMMO,ModifierHooks.CONDITIONAL_STAT, EtSTLibHooks.CUSTOM_BAR);
    }

    @Override
    public Component onModifierRemoved(IToolStackView tool, Modifier modifier) {
        var nbt = tool.getPersistentData();
        nbt.remove(KEY_COOLDOWN);
        nbt.remove(KEY_ARROW_SHOT);
        return null;
    }

    @Override
    public ItemStack findAmmo(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, ItemStack standardAmmo, Predicate<ItemStack> ammoPredicate) {
        if (tool.getPersistentData().getInt(KEY_ARROW_SHOT)>=10) return ItemStack.EMPTY;
        return new ItemStack(TltCoreItems.IONIZED_ARROW.get(),10-tool.getPersistentData().getInt(KEY_ARROW_SHOT));
    }

    @Override
    public String barId(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return "ionized_arrow_cooldown";
    }

    @Override
    public boolean showBar(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return true;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        if (tool.getPersistentData().getInt(KEY_ARROW_SHOT)>=10) {
            int total = getTotalCooldown(tool, null);
            int progress =Math.max(total - tool.getPersistentData().getInt(KEY_COOLDOWN),0);
            return new Vec2(Math.min(13.0F, 13.0F * (float) progress / (float) total), 1.0F);
        }
        else {
            int total = 10;
            int progress =Math.max(total - tool.getPersistentData().getInt(KEY_ARROW_SHOT),0);
            return new Vec2(Math.min(13.0F, 13.0F * (float) progress / (float) total), 1.0F);
        }
    }

    @Override
    public int getBarRGB(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i) {
        return iToolStackView.getPersistentData().getInt(KEY_ARROW_SHOT)>=10? 0xFFFF9000 : 0xFF6866FF;
    }

    @Override
    public void modifierProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @org.jetbrains.annotations.Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        var nbt = tool.getPersistentData();
        int existing = nbt.getInt(KEY_ARROW_SHOT);
        if (arrow instanceof IonizedArrowEntity entity){
            existing++;
            if (!primary) {
                if (existing > 10) {
                    entity.discard();
                    return;
                }
            }
            nbt.putInt(KEY_ARROW_SHOT,existing);
            entity.setOffhand(shooter.getUsedItemHand()== InteractionHand.OFF_HAND);
        }
        nbt.putInt(KEY_COOLDOWN,getTotalCooldown(tool,shooter));
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (world.getGameTime()%4==0&&!world.isClientSide){
            var nbt = tool.getPersistentData();
            int cd = nbt.getInt(KEY_COOLDOWN);
            if (cd>0){
                cd--;
                nbt.putInt(KEY_COOLDOWN,cd);
                if (cd<=0){
                    nbt.putInt(KEY_ARROW_SHOT,0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(LivingEntityUseItemEvent.Start event){
        var stack = event.getItem();
        if (stack.getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(stack);
            if (toolStack.getModifierLevel(TltCoreModifiers.IONIZED_ARROW.get())>0){
                var nbt = toolStack.getPersistentData();
                if (nbt.getInt(KEY_ARROW_SHOT)>=10) event.setDuration(-1);
            }
        }
    }

    @SubscribeEvent
    public static void onArrowNock(ArrowNockEvent event){
        var stack = event.getBow();
        if (stack.getItem() instanceof IModifiable){
            ToolStack toolStack = ToolStack.from(stack);
            if (toolStack.getModifierLevel(TltCoreModifiers.IONIZED_ARROW.get())>0){
                var nbt = toolStack.getPersistentData();
                if (nbt.getInt(KEY_ARROW_SHOT)>=10) event.setAction(InteractionResultHolder.pass(stack));
            }
        }
    }

    public static int getTotalCooldown(IToolStackView tool, @Nullable LivingEntity living){
        return (int) Math.max( living==null? 50f/tool.getStats().get(ToolStats.DRAW_SPEED) : 50f/ ConditionalStatModifierHook.getModifiedStat(tool,living,ToolStats.DRAW_SPEED),5);
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
        if (stat==ToolStats.DRAW_SPEED&&tool.getPersistentData().getInt(KEY_ARROW_SHOT)<10) return baseValue*20;
        return baseValue;
    }
}
