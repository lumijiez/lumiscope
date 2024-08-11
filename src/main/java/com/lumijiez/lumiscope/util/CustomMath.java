package com.lumijiez.lumiscope.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Random;

public class CustomMath {

    private static final Random RANDOM = new Random();

    public static double getPlayerDirectionShort(EntityPlayerMP player, EntityPlayerMP otherPlayer) {
        double angle = calculateRawAngle(player, otherPlayer);
        return Math.toRadians(normalizeAngle(angle));
    }

    public static double getPlayerDirectionLong(EntityPlayerMP player, EntityPlayerMP otherPlayer) {
        double angle = calculateRawAngle(player, otherPlayer);
        double errorAngle = applyDoublePerlinNoise(angle, 0.15);
        return Math.toRadians(normalizeAngle(errorAngle));
    }

    private static double calculateRawAngle(EntityPlayerMP player, EntityPlayerMP otherPlayer) {
        double deltaX = otherPlayer.posX - player.posX;
        double deltaZ = otherPlayer.posZ - player.posZ;

        double angle = MathHelper.atan2(deltaZ, deltaX) * (180 / Math.PI) - 90;
        if (angle < 0) angle += 360;

        return (angle + 180) % 360;
    }

    private static double normalizeAngle(double angle) {
        return (angle + 360) % 360;
    }

    private static double applyRandomError(double angle) {
        double errorPercentage = 1 + RANDOM.nextDouble() * 9;
        double error = angle * (errorPercentage / 100.0);
        double errorSign = RANDOM.nextBoolean() ? 1 : -1;
        return angle + errorSign * error;
    }

    private static double applyPerlinNoiseError(double baseValue, double scale) {
        double time = System.currentTimeMillis() / 1000.0;
        double noiseValue = PerlinNoise.noise(time);
        double error = baseValue * scale * noiseValue;
        return baseValue + error;
    }

    private static double applyDoublePerlinNoise(double baseValue, double scale) {
        double time = System.currentTimeMillis() / 1000.0;
        double firstNoise = PerlinNoise.noise(time);
        double noiseInput = baseValue + scale * firstNoise;

        double secondNoise = PerlinNoise.noise(noiseInput);
        double error = baseValue * scale * secondNoise;

        return baseValue + error;
    }

    public static int interpolateColor(int maxDistance, int minDistance, int currentDistance) {
        int clampedDistance = Math.max(minDistance, Math.min(maxDistance, currentDistance));
        float ratio = (float) (maxDistance - clampedDistance) / (maxDistance - minDistance);

        int r = (int) (ratio * 255);
        int g = (int) ((1 - ratio) * 255);
        int b = 0;

        return new Color(r, g, b).getRGB();
    }
}
