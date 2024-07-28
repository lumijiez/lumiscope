package com.lumijiez.lumiscope.handlers;

import com.lumijiez.lumiscope.items.radars.ShortRadar;
import com.lumijiez.lumiscope.network.RadarPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RadarRenderer {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final RadarRenderer INSTANCE = new RadarRenderer();
    private final ResourceLocation radarTexture = new ResourceLocation("lumiscope", "textures/gui/radar.png");
    private final ResourceLocation radarArrowTexture = new ResourceLocation("lumiscope", "textures/gui/radar_arrow.png");
    private List<RadarPacket.PlayerInfo> playerInfos;

    private RadarRenderer() {}

    public static RadarRenderer getInstance() {
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

    private boolean shouldRenderRadar() {
        return mc.player.getHeldItemMainhand().getItem() instanceof ShortRadar ||
                mc.player.getHeldItemOffhand().getItem() instanceof ShortRadar;
    }

    private void renderRadar(float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 0);

        mc.getTextureManager().bindTexture(radarTexture);

        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        drawTexturedCircle(1.4f);

        for (RadarPacket.PlayerInfo info : playerInfos) {
            double angle = info.direction - Math.toRadians(90);
            drawTexturedLine(1.4f, angle, radarArrowTexture);
        }

        for (RadarPacket.PlayerInfo info : playerInfos) {
            double angle = info.direction - Math.toRadians(90);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) (Math.cos(angle) * 1.2), 0.001, (float) (Math.sin(angle) * 1.2));
            GlStateManager.rotate(90f, 1, 0, 0);

            GlStateManager.rotate((float) Math.toDegrees(angle), 0, 0, 1);
            GlStateManager.scale(0.01, 0.01, 0.01);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableTexture2D();
            mc.fontRenderer.drawStringWithShadow(info.name, -mc.fontRenderer.getStringWidth(info.name) - 50 , -4, 0xAAFF00);
            GlStateManager.disableTexture2D();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }

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

    private void drawTexturedLine(float length, double angle, ResourceLocation texture) {
        mc.getTextureManager().bindTexture(texture);

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
}
