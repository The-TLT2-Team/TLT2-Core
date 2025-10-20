package com.fg.tltmod.network;

import com.fg.tltmod.TltCore;
import com.fg.tltmod.network.packets.PAddParticleS2C;
import com.fg.tltmod.network.packets.PClientMessagePacket;
import com.fg.tltmod.network.packets.PMagneticParticleS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class TltCorePacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE ;
    static int id = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(TltCore.MODID, "tlt_tech_message")).networkProtocolVersion(() -> "1").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE.messageBuilder(PClientMessagePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PClientMessagePacket::new)
                .encoder(PClientMessagePacket::toByte).consumerMainThread(PClientMessagePacket::handle).add();
        INSTANCE.messageBuilder(PMagneticParticleS2C.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PMagneticParticleS2C::new)
                .encoder(PMagneticParticleS2C::toByte).consumerMainThread(PMagneticParticleS2C::handle).add();
        INSTANCE.messageBuilder(PAddParticleS2C.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(PAddParticleS2C::new)
                .encoder(PAddParticleS2C::toByte).consumerMainThread(PAddParticleS2C::handle).add();

    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }

    public static <MSG> void sendToClient(MSG msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
