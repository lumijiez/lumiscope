package com.lumijiez.lumiscope.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class LumiEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderNameTag(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (event.getEntity() instanceof EntityPlayer) event.setCanceled(true);
    }
}
