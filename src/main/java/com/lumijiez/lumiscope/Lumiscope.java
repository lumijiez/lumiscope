package com.lumijiez.lumiscope;

import com.lumijiez.lumiscope.handlers.radar.LongRadarPacketHandler;
import com.lumijiez.lumiscope.handlers.radar.ShortRadarPacketHandler;
import com.lumijiez.lumiscope.render.radar.LongRadarRenderer;
import com.lumijiez.lumiscope.render.radar.ShortRadarRenderer;
import com.lumijiez.lumiscope.proxy.CommonProxy;
import com.lumijiez.lumiscope.util.Ref;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.lumijiez.lumiscope.util.Ref.logger;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class Lumiscope {
    @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderNameTag(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (event.getEntity() instanceof EntityPlayer) event.setCanceled(true);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        ShortRadarPacketHandler.registerMessages();
        LongRadarPacketHandler.registerMessages();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(ShortRadarRenderer.getInstance());
            MinecraftForge.EVENT_BUS.register(LongRadarRenderer.getInstance());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        logger.info("Radar turned on!");
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
