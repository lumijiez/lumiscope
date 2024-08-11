package com.lumijiez.lumiscope.util;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLHelper {

    public static void setupForRadarRendering() {
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void cleanupAfterRadarRendering() {
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
    }

    public static void setupForTextRendering() {
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
    }

    public static void cleanupAfterTextRendering() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableTexture2D();
    }
}
