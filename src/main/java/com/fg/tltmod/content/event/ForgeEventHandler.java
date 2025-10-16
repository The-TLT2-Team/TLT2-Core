package com.fg.tltmod.content.event;

import com.fg.tltmod.Register.TltCoreEntityTypes;
import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.FoodEntity;
import com.fg.tltmod.util.mixin.IFallingBlockEntityMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
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

    @SubscribeEvent
    public static void OnWorldTick(TickEvent.LevelTickEvent event) {
        String s= "tofucraft:tofu_world";
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide) return;
        if (event.level instanceof ServerLevel level) {
            if (level.dimension().location().toString().equals(s)) {
                for (Player player : level.players()) {
                    if (!player.level().dimension().location().toString().equals(s)) return;
                    //if ( level.getGameTime() % 2 != 0) return;
                    Random random = new Random();
                    Vec2 pos = new Vec2((float) (player.getX() + random.nextInt(60) - random.nextInt(60)), (float) (player.getZ() + random.nextInt(60) - random.nextInt(60)));
//                    Vec2 pos = new Vec2((float) player.getX(), (float) player.getZ() );
                    FoodEntity entity = new FoodEntity(TltCoreEntityTypes.FOOD_ENTITY.get(), level);
                    entity.noPhysics = false;
                    entity.setPos(pos.x, player.getY() + 100, pos.y);
                    level.addFreshEntity(entity);
                }
            }
        }
    }
}
