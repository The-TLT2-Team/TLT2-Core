package com.fg.tltmod.L2;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;
import java.util.function.IntSupplier;

public class EvilKarmaTrait extends MobTrait {
    public EvilKarmaTrait(IntSupplier color) {
        super((color));
    }

    @Override
    public void onDeath(int a, LivingEntity entity, LivingDeathEvent event) {
        if (entity.level().isClientSide()) {
            return;
        }
        if (!validTarget(entity)) {
            return;
        }
        if (event.getSource().getEntity() instanceof Player player) {
            PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
            LevelEditor editor = cap.getLevelEditor();
            int b = (int) (editor.getTotal()*0.1f*a);
            editor.addBase(50+b);
            cap.sync();
        }
    }

    public boolean validTarget(LivingEntity le) {
        if (le instanceof EnderDragon) {
            return false;
        }
        return le.canBeAffected(new MobEffectInstance(LCEffects.CURSE.get(), 100));
    }
    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i*10 + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
