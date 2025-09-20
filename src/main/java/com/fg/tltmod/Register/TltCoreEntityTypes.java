package com.fg.tltmod.Register;

import com.fg.tltmod.TltCore;
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
}
