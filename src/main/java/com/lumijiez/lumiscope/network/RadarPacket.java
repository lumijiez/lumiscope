package com.lumijiez.lumiscope.network;

import com.lumijiez.lumiscope.render.ShortRadarRenderer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class RadarPacket implements IMessage {
    public static class PlayerInfo {
        public String name;
        public double direction;

        public PlayerInfo(String name, double direction) {
            this.name = name;
            this.direction = direction;
        }

    }

    private List<PlayerInfo> playerInfos;

    public RadarPacket() {
        this.playerInfos = new ArrayList<>();
    }

    public RadarPacket(List<PlayerInfo> playerInfos) {
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
            playerInfos.add(new PlayerInfo(name, direction));
        }
    }

    public static class Handler implements IMessageHandler<RadarPacket, IMessage> {
        @Override
        public IMessage onMessage(RadarPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ShortRadarRenderer renderer = ShortRadarRenderer.getInstance();
                renderer.updatePlayerInfos(message.playerInfos);
            });
            return null;
        }
    }
}
