package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.LaserEntity;
import com.fg.tltmod.content.entity.ThunderBurstEntity;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import com.fg.tltmod.content.entity.living.MoonSlimeBoss;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.mantle.registration.object.EntityObject;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.EnderSlimeEntity;
import vazkii.botania.common.entity.ManaBurstEntity;

public class TltCoreEntityTypes {
    public static final EntityTypeDeferredRegister ENTITY_TYPES = new EntityTypeDeferredRegister(TltCore.MODID);
    public static final RegistryObject<EntityType<WaveSlashEntity>> WAVE_SLASH = ENTITY_TYPES.register("wave_slash",()->
            EntityType.Builder.<WaveSlashEntity>of(WaveSlashEntity::new, MobCategory.MISC)
                    .sized(2f,0.25f)
                    .setCustomClientFactory(((spawnEntity, level) -> new WaveSlashEntity(level)))
                    .setTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(4));
    public static final RegistryObject<EntityType<LaserEntity>> LASER_A = ENTITY_TYPES.register("laser_a",()->
            EntityType.Builder.<LaserEntity>of(LaserEntity::new, MobCategory.MISC)
                    .sized(1.0f,1.0f)
                    .setCustomClientFactory(LaserEntity::new)
                    .setTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(-1));
    public static final EntityObject<MoonSlimeBoss> MOON_SLIME_BOSS = ENTITY_TYPES.registerWithEgg("moon_slime_boss",()->
            EntityType.Builder.of(MoonSlimeBoss::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(32)
                    .sized(2.0F, 2.0F)
                    .setCustomClientFactory((spawnEntity, world) -> TltCoreEntityTypes.MOON_SLIME_BOSS.get().create(world)), 0xCCBBB0, 0xFFEECC);
    public static final RegistryObject<EntityType<ManaBurstEntity>> THUNDER_BURST = ENTITY_TYPES.register("thunder_burst",()->
            EntityType.Builder.<ManaBurstEntity>of(ThunderBurstEntity::new, MobCategory.MISC)
                    .sized(2f,2f)
                    .setCustomClientFactory(((spawnEntity, level) -> new ThunderBurstEntity(level)))
                    .setTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(4));
}
