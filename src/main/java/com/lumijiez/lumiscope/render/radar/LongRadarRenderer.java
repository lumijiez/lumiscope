package com.lumijiez.lumiscope.render.radar;

import com.lumijiez.lumiscope.items.radars.LongRadar;
import com.lumijiez.lumiscope.network.records.PlayerInfo;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LongRadarRenderer extends BaseRadarRenderer {
    private static final LongRadarRenderer INSTANCE = new LongRadarRenderer();

    private LongRadarRenderer() {}

    public static LongRadarRenderer getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (shouldRenderRadar()) {
            if (playerInfos != null && !playerInfos.isEmpty()) {
                renderRadar(event.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayerHand(RenderHandEvent event) {
        if (mc.player.getHeldItemMainhand().getItem() instanceof LongRadar) {
            event.setCanceled(true);
        }
    }

    @Override
    protected boolean shouldRenderRadar() {
        return mc.player.getHeldItemMainhand().getItem() instanceof LongRadar ||
                mc.player.getHeldItemOffhand().getItem() instanceof LongRadar;
    }

    @Override
    protected void drawTexturedCircle(float radius) {
        drawTexturedCircleBase(radius);
    }

    @Override
    protected void drawTexturedLine(float length, double angle) {
        drawTexturedLineBase(length, angle);
    }

    @Override
    protected double getDirection(PlayerInfo info) {
        return info.direction;
    }
}
