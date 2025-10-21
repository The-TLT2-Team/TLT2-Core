package com.fg.tltmod.network.packets;

import com.fg.tltmod.content.entityTicker.MagneticParticleTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.UUID;
import java.util.function.Supplier;

public class PMagneticParticleS2C {
    public final int duration;
    public final int level;
    public final int entityID;
    public final boolean isScarlet;

    public PMagneticParticleS2C(int duration, int level, int entityID,boolean isScarlet) {
        this.duration = duration;
        this.level = level;
        this.entityID = entityID;
        this.isScarlet = isScarlet;
    }
    public PMagneticParticleS2C(FriendlyByteBuf byteBuf){
        this.duration = byteBuf.readInt();
        this.level = byteBuf.readInt();
        this.entityID = byteBuf.readInt();
        this.isScarlet = byteBuf.readBoolean();
    }
    public void toByte(FriendlyByteBuf byteBuf){
        byteBuf.writeInt(this.duration);
        byteBuf.writeInt(this.level);
        byteBuf.writeInt(this.entityID);
        byteBuf.writeBoolean(this.isScarlet);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        var world = SafeClientAccess.getLevel();
        if (world!=null){
            Entity entity = world.getEntity(entityID);
            if (entity!=null){
                MagneticParticleTicker.clientTick(world,this.duration,this.level,entity,this.isScarlet);
            }
        }
        return true;
    }
}
