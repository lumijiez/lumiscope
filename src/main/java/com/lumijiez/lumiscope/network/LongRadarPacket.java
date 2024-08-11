package com.lumijiez.lumiscope.network;

import com.lumijiez.lumiscope.render.radar.LongRadarRenderer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class LongRadarPacket implements IMessage {
    public static class PlayerInfo {
        public String name;
        public double direction;
        public double distance;

        public PlayerInfo(String name, double direction, double distance) {
            this.name = "none";
            this.direction = direction;
            this.distance = 0;
        }
    }

    private List<PlayerInfo> playerInfos;

    public LongRadarPacket() {
        this.playerInfos = new ArrayList<>();
    }

    public LongRadarPacket(List<PlayerInfo> playerInfos) {
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

    public static class Handler implements IMessageHandler<LongRadarPacket, IMessage> {
        @Override
        public IMessage onMessage(LongRadarPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                LongRadarRenderer renderer = LongRadarRenderer.getInstance();
                renderer.updatePlayerInfos(message.playerInfos);
            });
            return null;
        }
    }
}
