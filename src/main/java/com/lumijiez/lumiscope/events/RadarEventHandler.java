package com.lumijiez.lumiscope.events;

import com.lumijiez.lumiscope.items.radars.LongRadar;
import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.handlers.LongRadarPacketHandler;
import com.lumijiez.lumiscope.network.handlers.ShortRadarPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber
public class RadarEventHandler {
    Random RANDOM = new Random();
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.PlayerTickEvent.Phase.START) {
            ItemStack heldItem = event.player.getHeldItemMainhand();
            if (heldItem.getItem() instanceof ShortRadar || heldItem.getItem() instanceof LongRadar) {
                if (!heldItem.isEmpty() && heldItem.isItemStackDamageable()) {
                    if (RANDOM.nextInt(100) < 5) {
                        heldItem.damageItem(1, event.player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                ShortRadarPacketHandler.sendRadarUpdate(player);
                LongRadarPacketHandler.sendRadarUpdate(player);
            }
        }
    }
}
