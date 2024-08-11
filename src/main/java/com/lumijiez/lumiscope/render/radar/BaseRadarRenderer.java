package com.lumijiez.lumiscope.render.radar;

import com.lumijiez.lumiscope.util.GLHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class BaseRadarRenderer<T> {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected final ResourceLocation radarTexture = new ResourceLocation("lumiscope", "textures/gui/radar.png");
    protected final ResourceLocation radarArrowTexture = new ResourceLocation("lumiscope", "textures/gui/radar_arrow.png");
    protected List<T> playerInfos;

    protected abstract void drawTexturedCircle(float radius);

    protected abstract void drawTexturedLine(float length, double angle);

    protected abstract double getDirection(T info);

    protected abstract boolean shouldRenderRadar();

    public void updatePlayerInfos(List<T> playerInfos) {
        this.playerInfos = playerInfos;
    }

    protected void renderRadar(float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.01, 0);

        mc.getTextureManager().bindTexture(radarTexture);

        GLHelper.setupForRadarRendering();
        drawTexturedCircle(1.4f);

        for (T info : playerInfos) {
            double angle = getDirection(info) - Math.toRadians(90);
            drawTexturedLine(1.4f, angle);
        }

        GLHelper.cleanupAfterRadarRendering();
        GlStateManager.popMatrix();
    }

    protected void drawTexturedCircleBase(float radius) {
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

    protected void drawTexturedLineBase(float length, double angle) {
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
}
