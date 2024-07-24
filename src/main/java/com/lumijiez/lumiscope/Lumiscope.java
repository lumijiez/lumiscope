package com.lumijiez.lumiscope;

import com.lumijiez.lumiscope.proxy.CommonProxy;
import com.lumijiez.lumiscope.util.Ref;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.lumijiez.lumiscope.util.Ref.logger;

@Mod(modid = Ref.MODID, name = Ref.NAME, version = Ref.VERSION)
public class Lumiscope {
    @SidedProxy(clientSide = Ref.CLIENT_PROXY_CLASS, serverSide = Ref.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Radar turned on!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        logger.info("Radar turned on!");
    }
}
