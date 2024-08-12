package com.lumijiez.lumiscope;

import com.lumijiez.lumiscope.events.LumiEventHandler;
import com.lumijiez.lumiscope.events.RadarEventHandler;
import com.lumijiez.lumiscope.handlers.RegistryHandler;
import com.lumijiez.lumiscope.proxy.CommonProxy;
import com.lumijiez.lumiscope.render.radar.LongRadarRenderer;
import com.lumijiez.lumiscope.render.radar.ShortRadarRenderer;
import com.lumijiez.lumiscope.util.Ref;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.lumijiez.lumiscope.util.Ref.logger;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class Lumiscope {
    @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LumiEventHandler());
        MinecraftForge.EVENT_BUS.register(new RadarEventHandler());
        RegistryHandler.preInitRegistry();
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
        logger.info("Initialized!");
    }
}
