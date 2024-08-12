package com.lumijiez.lumiscope.network.handlers;

import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.packets.ShortRadarPacket;
import com.lumijiez.lumiscope.network.records.PlayerInfo;
import com.lumijiez.lumiscope.potions.PotionManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lumijiez.lumiscope.util.CustomMath.getPlayerDirectionShort;

public class ShortRadarPacketHandler {
    private static final SimpleNetworkWrapper NETWORK_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("short_radar");

    public static void registerMessages() {
        NETWORK_CHANNEL.registerMessage(ShortRadarPacket.Handler.class, ShortRadarPacket.class, 0, Side.CLIENT);
    }

    public static void sendRadarUpdate(EntityPlayerMP player) {
        if (isHoldingShortRadar(player) && !isJammered(player)) {
            List<PlayerInfo> playerInfos = getNearbyPlayersInfo(player);
            NETWORK_CHANNEL.sendTo(new ShortRadarPacket(playerInfos), player);
        }
    }

    private static boolean isHoldingShortRadar(EntityPlayerMP player) {
        return player.getHeldItemMainhand().getItem() instanceof ShortRadar;
    }

    private static boolean isJammered(EntityPlayerMP player) {
        return player.isPotionActive(PotionManager.JAMMERED_POTION_EFFECT);
    }

    private static List<PlayerInfo> getNearbyPlayersInfo(EntityPlayerMP player) {
        return Objects.requireNonNull(player.getServerWorld().getMinecraftServer()).getPlayerList().getPlayers().stream()
                .filter(otherPlayer -> shouldIncludePlayer(player, otherPlayer))
                .map(otherPlayer -> new PlayerInfo(
                        otherPlayer.getName(),
                        getPlayerDirectionShort(player, otherPlayer),
                        player.getDistance(otherPlayer)
                ))
                .collect(Collectors.toList());
    }

    private static boolean shouldIncludePlayer(EntityPlayerMP player, EntityPlayerMP otherPlayer) {
        return !otherPlayer.equals(player) &&
                player.getDistance(otherPlayer) <= 100 &&
                !otherPlayer.isPotionActive(MobEffects.INVISIBILITY) &&
                !otherPlayer.isPotionActive(PotionManager.JAMMERED_POTION_EFFECT);
    }
}