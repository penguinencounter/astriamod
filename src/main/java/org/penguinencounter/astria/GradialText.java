package org.penguinencounter.astria;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class GradialText {
    public static class BadArgumentError extends RuntimeException {
        public BadArgumentError(String s) {
            super(s);
        }
    }

    public static int RGBFromComponents(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public static int RGBFromComponents(int[] rgb) {
        if (rgb.length != 3) {
            throw new BadArgumentError("rgb must have length 3");
        }
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];
        return RGBFromComponents(r, g, b);
    }

    public static int[] ComponentsFromRGB(int rgb) {
        return new int[] {rgb >> 16 & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
    }

    public static class GradientStop {
        public int color;
        public float position;
        public GradientStop(int color, float position) {
            this.color = color;
            this.position = position;
            if (position < 0 || position > 1) {
                throw new BadArgumentError("Bad \"position\" argument: " + position + " (must be between 0 and 1)");
            }
        }

        public static int lerp(float t, GradientStop stop1, GradientStop stop2) {
            int[] c1 = ComponentsFromRGB(stop1.color);
            int[] c2 = ComponentsFromRGB(stop2.color);
            int[] c = new int[3];
            for (int i = 0; i < 3; i++) {
                c[i] = (int) (c1[i] + t * (c2[i] - c1[i]));
            }
            return RGBFromComponents(c);
        }

        public static int lerp(float t, GradientStop... stops) {
            if (t < 0 || t > 1) throw new BadArgumentError("Bad t (not in 0 < t < 1");

            GradientStop lowerStop = null;
            float lowerScore = 1;
            GradientStop upperStop = null;
            float upperScore = 1;
            for (GradientStop stop : stops) {
                float lowerDiff = stop.position - t;
                float upperDiff = t - stop.position;
                if (lowerDiff >= 0 && lowerDiff < lowerScore) {
                    lowerStop = stop;
                    lowerScore = lowerDiff;
                }
                if (upperDiff >= 0 && upperDiff < upperScore) {
                    upperStop = stop;
                    upperScore = upperDiff;
                }
            }

            if (upperStop == null || lowerStop == null) throw new BadArgumentError("Stops do not cover the requested t");
            if (t == 0) return lowerStop.color;
            if (t == 1) return upperStop.color;

            return lerp((t - lowerStop.position) / (upperStop.position - lowerStop.position), lowerStop, upperStop);
        }
    }

    public static MutableText build(MutableText source, GradientStop... stops) {
        if (stops.length < 2) {
            throw new BadArgumentError("Bad \"stops\" argument: must have at least 2 stops");
        }
        String text = source.getString();
        MutableText build = Text.empty();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float t = (float) i / text.length();
            int color = GradientStop.lerp(t, stops);
            Style styling = source.getStyle().withColor(TextColor.fromRgb(color));
            build.append(Text.literal(String.valueOf(c)).setStyle(styling));
        }
        return build;
    }
}
