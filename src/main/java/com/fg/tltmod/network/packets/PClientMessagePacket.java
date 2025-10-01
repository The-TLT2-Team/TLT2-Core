package com.fg.tltmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PClientMessagePacket {
    private final Component component;

    public PClientMessagePacket(Component component) {
        this.component = component;
    }

    public PClientMessagePacket(FriendlyByteBuf byteBuf){
        this.component = byteBuf.readComponent();
    }

    public void toByte(FriendlyByteBuf byteBuf){
        byteBuf.writeComponent(this.component);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        var player = SafeClientAccess.getPlayer();
        if (player!=null){
            player.displayClientMessage(this.component,false);
        }
        return true;
    }
}
