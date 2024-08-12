package com.lumijiez.lumiscope.render.radar;

import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.records.PlayerInfo;
import com.lumijiez.lumiscope.util.CustomMath;
import com.lumijiez.lumiscope.util.GLHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShortRadarRenderer extends BaseRadarRenderer {
    private static final ShortRadarRenderer INSTANCE = new ShortRadarRenderer();

    private ShortRadarRenderer() {}

    public static ShortRadarRenderer getInstance() {
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
        if (mc.player.getHeldItemMainhand().getItem() instanceof ShortRadar) {
            event.setCanceled(true);
        }
    }

    @Override
    protected void renderRadar(float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.01, 0);

        mc.getTextureManager().bindTexture(radarTexture);

        GLHelper.setupForRadarRendering();
        drawTexturedCircle(1.4f);

        for (PlayerInfo info : playerInfos) {
            double angle = getDirection(info) - Math.toRadians(90);
            drawTexturedLine(1.4f, angle);
        }

        GLHelper.setupForTextRendering();

        for (PlayerInfo info : playerInfos) {
            double angle = info.direction - Math.toRadians(90);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) (Math.cos(angle) * 1.2), 0.001, (float) (Math.sin(angle) * 1.2));
            GlStateManager.rotate(90f, 1, 0, 0);

            GlStateManager.rotate((float) Math.toDegrees(angle), 0, 0, 1);
            GlStateManager.scale(0.01, 0.01, 0.01);

            mc.fontRenderer.drawStringWithShadow(info.name, -mc.fontRenderer.getStringWidth(info.name) - 50 , -4, 0x808080);
            mc.fontRenderer.drawStringWithShadow("(" + (int) info.distance +"m)", -mc.fontRenderer.getStringWidth(info.name) + 5 , -4, CustomMath.interpolateColor(100, 0, (int) info.distance));

            GlStateManager.popMatrix();
        }

        GLHelper.cleanupAfterTextRendering();
        GLHelper.cleanupAfterRadarRendering();
        GlStateManager.popMatrix();
    }

    @Override
    protected boolean shouldRenderRadar() {
        return mc.player.getHeldItemMainhand().getItem() instanceof ShortRadar ||
                mc.player.getHeldItemOffhand().getItem() instanceof ShortRadar;
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
