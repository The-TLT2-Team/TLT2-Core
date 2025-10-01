package com.fg.tltmod.mixin.l2;

import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import dev.xkmc.l2hostility.content.item.curio.core.MultiSlotItem;
import dev.xkmc.l2hostility.content.item.curio.misc.AbyssalThorn;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

@Mixin(value = AbyssalThorn.class, remap = false)
public class CurioAbyssalThornMixin extends MultiSlotItem implements ICursed {
    public CurioAbyssalThornMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext context, ItemStack stack) {
        boolean original = CuriosApi.getCuriosInventory(context.entity())
                .resolve()
                .flatMap(e -> e.findFirstCurio(this))
                .map(slot -> {
                    SlotContext rep = slot.slotContext();
                    return rep.identifier().equals(context.identifier()) && rep.index() == context.index();
                })
                .orElse(true);
        boolean cursed = context.entity() instanceof Player player && SuperpositionHandler.isTheCursedOne(player);

        return original && cursed;
    }

    /**
     * @author ssakura49
     * @reason 重写tooltip
     */
    @Overwrite
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ABYSSAL_THORN.get(new Object[0]).withStyle(ChatFormatting.RED));
        ItemLoreHelper.indicateCursedOnesOnly(list);
    }
}
