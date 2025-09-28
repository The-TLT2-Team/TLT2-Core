package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.content.entity.LaserEntity;
import com.fg.tltmod.content.entity.WaveSlashEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

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
}
