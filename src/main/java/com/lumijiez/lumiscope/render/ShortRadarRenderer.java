package com.lumijiez.lumiscope.render;

import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.RadarPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ShortRadarRenderer {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ShortRadarRenderer INSTANCE = new ShortRadarRenderer();
    private final ResourceLocation radarTexture = new ResourceLocation("lumiscope", "textures/gui/radar.png");
    private final ResourceLocation radarArrowTexture = new ResourceLocation("lumiscope", "textures/gui/radar_arrow.png");
    private List<RadarPacket.PlayerInfo> playerInfos;

    private ShortRadarRenderer() {}

    public static ShortRadarRenderer getInstance() {
        return INSTANCE;
    }

    public void updatePlayerInfos(List<RadarPacket.PlayerInfo> playerInfos) {
        this.playerInfos = playerInfos;
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

    private boolean shouldRenderRadar() {
        return mc.player.getHeldItemMainhand().getItem() instanceof ShortRadar ||
                mc.player.getHeldItemOffhand().getItem() instanceof ShortRadar;
    }

    private void renderRadar(float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.01, 0);

        mc.getTextureManager().bindTexture(radarTexture);

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);

        drawTexturedCircle(1.4f);

        for (RadarPacket.PlayerInfo info : playerInfos) {
            double angle = info.direction - Math.toRadians(90);
            drawTexturedLine(1.4f, angle);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        for (RadarPacket.PlayerInfo info : playerInfos) {
            double angle = info.direction - Math.toRadians(90);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) (Math.cos(angle) * 1.2), 0.001, (float) (Math.sin(angle) * 1.2));
            GlStateManager.rotate(90f, 1, 0, 0);

            GlStateManager.rotate((float) Math.toDegrees(angle), 0, 0, 1);
            GlStateManager.scale(0.01, 0.01, 0.01);

            mc.fontRenderer.drawStringWithShadow(info.name, -mc.fontRenderer.getStringWidth(info.name) - 50 , -4, 0x808080);
            mc.fontRenderer.drawStringWithShadow("(" + (int) info.distance +"m)", -mc.fontRenderer.getStringWidth(info.name) + 5 , -4, interpolateColor(100, 0, (int) info.distance));

            GlStateManager.popMatrix();
        }
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        RenderHelper.enableStandardItemLighting();

        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawTexturedCircle(float radius) {
        mc.getTextureManager().bindTexture(radarTexture);

        GlStateManager.enableTexture2D();

        GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);

        GlStateManager.glTexCoord2f(0.5f, 0.5f);
        GlStateManager.glVertex3f(0, 0, 0);

        for (int i = 0; i <= 360; i++) {
            double rad = Math.toRadians(i);
            float x = (float) (Math.cos(rad) * radius);
            float y = (float) (Math.sin(rad) * radius);

            float u = (x / radius + 1.0f) / 2.0f;
            float v = (y / radius + 1.0f) / 2.0f;

            GlStateManager.glTexCoord2f(u, v);
            GlStateManager.glVertex3f(x, 0, y);
        }

        GlStateManager.glEnd();

        GlStateManager.disableTexture2D();
    }

    private void drawTexturedLine(float length, double angle) {
        mc.getTextureManager().bindTexture(radarArrowTexture);

        GlStateManager.enableTexture2D();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (Math.cos(angle) * length / 2), 0, (float) (Math.sin(angle) * length / 2));
        GlStateManager.rotate(90f, 1, 0, 0);
        GlStateManager.rotate((float) Math.toDegrees(angle) + 90, 0, 0, 1);
        GlStateManager.scale(0.2, length, 1);

        GlStateManager.glBegin(GL11.GL_QUADS);

        GlStateManager.glTexCoord2f(0, 0);
        GlStateManager.glVertex3f(-0.5f, -0.5f, 0);
        GlStateManager.glTexCoord2f(1, 0);
        GlStateManager.glVertex3f(0.5f, -0.5f, 0);
        GlStateManager.glTexCoord2f(1, 1);
        GlStateManager.glVertex3f(0.5f, 0.5f, 0);
        GlStateManager.glTexCoord2f(0, 1);
        GlStateManager.glVertex3f(-0.5f, 0.5f, 0);

        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.disableTexture2D();
    }

    private int interpolateColor(int maxDistance, int minDistance, int currentDistance) {
        int clampedDistance = Math.max(minDistance, Math.min(maxDistance, currentDistance));
        float ratio = (float) (maxDistance - clampedDistance) / (maxDistance - minDistance);

        int r = (int) (ratio * 255);
        int g = (int) ((1 - ratio) * 255);
        int b = 0;

        return new Color(r, g, b).getRGB();
    }
}
