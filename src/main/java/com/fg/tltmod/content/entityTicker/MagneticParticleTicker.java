package com.fg.tltmod.content.entityTicker;

import com.c2h6s.etstlib.content.misc.entityTicker.EntityTicker;
import com.c2h6s.etstlib.util.ProjectileUtil;
import com.fg.tltmod.SomeModifiers.integration.botania.specialized.ParticleAccelerate;
import com.fg.tltmod.network.TltCorePacketHandler;
import com.fg.tltmod.network.packets.PMagneticParticleS2C;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class MagneticParticleTicker extends EntityTicker {
    public final String id;
    public MagneticParticleTicker(String id){
        this.id = id;
    }
    @Override
    public void onTickerEnd(int level, Entity entity) {
        entity.getPersistentData().remove(this.id);
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        TltCorePacketHandler.sendToClient(new PMagneticParticleS2C(duration,level,entity.getId(),this.id.equals(ParticleAccelerate.KEY_SCARLET)));
        return true;
    }

    public static void clientTick(Level world,int duration, int level, Entity entity,boolean isScarlet){
        var type = isScarlet ? ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get() : ACParticleRegistry.AZURE_MAGNETIC_ORBIT.get();
        world.addParticle(type,entity.getX(),entity.getY()+0.5*entity.getBbHeight(),entity.getZ(),entity.getX(),entity.getY()+0.5*entity.getBbHeight(),entity.getZ());
    }
}
