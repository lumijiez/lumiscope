package com.lumijiez.lumiscope.network.packets;

import com.lumijiez.lumiscope.network.records.PlayerInfo;
import com.lumijiez.lumiscope.render.radar.ShortRadarRenderer;
import com.lumijiez.lumiscope.util.Ref;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class ShortRadarPacket implements IMessage {
    private List<PlayerInfo> playerInfos;

    public ShortRadarPacket() {
        this.playerInfos = new ArrayList<>();
    }

    public ShortRadarPacket(List<PlayerInfo> playerInfos) {
        this.playerInfos = playerInfos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(playerInfos.size());
        for (PlayerInfo info : playerInfos) {
            byte[] nameBytes = info.name.getBytes();
            buf.writeInt(nameBytes.length);
            buf.writeBytes(nameBytes);
            buf.writeDouble(info.direction);
            buf.writeDouble(info.distance);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        playerInfos = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int nameLength = buf.readInt();
            byte[] nameBytes = new byte[nameLength];
            buf.readBytes(nameBytes);
            String name = new String(nameBytes);
            double direction = buf.readDouble();
            double distance = buf.readDouble();
            playerInfos.add(new PlayerInfo(name, direction, distance));
        }
    }

    public static class Handler implements IMessageHandler<ShortRadarPacket, IMessage> {
        @Override
        public IMessage onMessage(ShortRadarPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ShortRadarRenderer renderer = ShortRadarRenderer.getInstance();
                renderer.updatePlayerInfos(message.playerInfos);
                Ref.logger.info("RECEIVED SHORT PACKET");
            });
            return null;
        }
    }
}