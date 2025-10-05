package com.fg.tltmod.content.event;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.util.mixin.IFallingBlockEntityMixin;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(modid = TltCore.MODID)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onEntityTravelDimension(EntityTravelToDimensionEvent event){
        if (event.getEntity() instanceof FallingBlockEntity entity){
            resetFallenBlock(entity);
        }
    }

    public static void resetFallenBlock(FallingBlockEntity entity){
        Random random = new Random();
        ArrayList<Block> list = new ArrayList<>(List.of(Blocks.SAND));
        if (random.nextFloat()>0.01) {
            Optional.ofNullable(ForgeRegistries.BLOCKS.tags()).ifPresent(iTags -> {
                list.addAll(iTags.getTag(Tags.Blocks.SAND).stream().toList());
                list.addAll(iTags.getTag(Tags.Blocks.COBBLESTONE).stream().toList());
                list.addAll(iTags.getTag(Tags.Blocks.STONE).stream().toList());
                list.addAll(iTags.getTag(Tags.Blocks.SANDSTONE).stream().toList());
            });
        } else {
            list.set(0,Blocks.DRAGON_EGG);
        }
        ((IFallingBlockEntityMixin)entity).tltmod$setBlockState(list.get(random.nextInt(list.size())).defaultBlockState());
    }


}
