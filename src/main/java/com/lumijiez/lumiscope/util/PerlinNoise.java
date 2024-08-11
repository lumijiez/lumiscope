package com.lumijiez.lumiscope.util;

public class PerlinNoise {
    private static final int PERMUTATION_SIZE = 256;
    private static final int[] PERMUTATION = new int[PERMUTATION_SIZE * 2];
    private static final int[] P = new int[PERMUTATION_SIZE];

    static {
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            P[i] = i;
        }

        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            int swap = (int) (Math.random() * PERMUTATION_SIZE);
            int temp = P[i];
            P[i] = P[swap];
            P[swap] = temp;
        }

        System.arraycopy(P, 0, PERMUTATION, 0, PERMUTATION_SIZE);
        System.arraycopy(P, 0, PERMUTATION, PERMUTATION_SIZE, PERMUTATION_SIZE);
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x) {
        int h = hash & 15;
        double grad = 1 + (h & 7);
        return (h & 8) == 0 ? grad * x : -grad * x;
    }

    public static double noise(double x) {
        int xi = (int) Math.floor(x) & 255;
        double xf = x - Math.floor(x);
        double u = fade(xf);

        return lerp(u, grad(PERMUTATION[xi], xf), grad(PERMUTATION[xi + 1], xf - 1));
    }
}
