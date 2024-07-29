package com.lumijiez.lumiscope.handlers;

import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.RadarPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class RadarPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("lumiscope");

    public static void registerMessages() {
        INSTANCE.registerMessage(RadarPacket.Handler.class, RadarPacket.class, 0, Side.CLIENT);
    }

    public static void sendRadarUpdate(EntityPlayerMP player) {
        if (!(player.getHeldItemMainhand().getItem() instanceof ShortRadar)) {
            return;
        }
        List<RadarPacket.PlayerInfo> playerInfos = new ArrayList<>();
        for (EntityPlayerMP otherPlayer : player.getServerWorld().getMinecraftServer().getPlayerList().getPlayers()) {
            if (!otherPlayer.equals(player) && player.getDistance(otherPlayer) <= 100) {
                double direction = getPlayerDirection(player, otherPlayer);
                playerInfos.add(new RadarPacket.PlayerInfo(otherPlayer.getName(), direction));
            }
        }
        INSTANCE.sendTo(new RadarPacket(playerInfos), player);
    }

    private static double getPlayerDirection(EntityPlayerMP player, EntityPlayerMP otherPlayer) {
        double deltaX = otherPlayer.posX - player.posX;
        double deltaZ = otherPlayer.posZ - player.posZ;

        double angle = MathHelper.atan2(deltaZ, deltaX) * (180 / Math.PI) - 90;
        if (angle < 0) angle += 360;

        angle = (angle + 180) % 360;
        return Math.toRadians(angle);
    }
}
