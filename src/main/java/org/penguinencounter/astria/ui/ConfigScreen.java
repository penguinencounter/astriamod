package org.penguinencounter.astria.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class ConfigScreen extends Screen {
    public static ConfigScreen init(Text title) {
        return new ConfigScreen(title);
    }
    protected ConfigScreen(Text ignoredTitle) {
        super(Text.translatable("astria.config"));
    }

    /*
    if direction is VERTICAL, then c1 is x, c2 is y1, and c3 is y2
    if direction is HORIZONTAL, then c1 is x1, c2 is x2, and c3 is y
     */
    public record Line(Direction direction, int c1, int c2, int c3, int col) {
        public enum Direction {VERTICAL, HORIZONTAL}
    }

    private final Map<Integer, List<Line>> stack = new HashMap<>();

    private void populateLayers(int... layers) {
        for (int layer : layers) {
            if (stack.containsKey(layer)) return;
            stack.put(layer, new ArrayList<>());
        }
    }

    private void renderStack(MatrixStack matrices) {
        Set<Integer> entries = stack.keySet();
        List<Integer> entriesSorted = entries.stream().sorted().toList();
        for (int layer : entriesSorted) {
            List<Line> lines = stack.get(layer);
            for (Line line : lines) {
                switch (line.direction()) {
                    case VERTICAL -> fill(matrices, line.c1(), line.c2(), line.c1() + 1, line.c3(), line.col());
                    case HORIZONTAL -> fill(matrices, line.c1(), line.c3(), line.c2(), line.c3() + 1, line.col());
                }
            }
        }
        stack.clear();
    }

    private int darkenColor(int in) {
        int out = 0;
        for (int i = 0; i < 3; i++) {
            int c = (in >> (i * 8)) & 0xFF;
            c = (int) (c * 0.5);
            out |= c << (i * 8);
        }
        int a = (in >> 24) & 0xFF;
        out |= a << 24;
        return out;
    }

    private void drawVerticalLineWithLayer(int x, int y1, int y2, int color) {
        populateLayers(-1, 0);
        stack.get(-1).add(new Line(Line.Direction.VERTICAL, x+2, y1+2, y2+2, darkenColor(color)));
        stack.get(0).add(new Line(Line.Direction.VERTICAL, x, y1, y2, color));
    }

    private void drawHorizontalLineWithLayer(int x1, int x2, int y, int color) {
        populateLayers(-1, 0);
        stack.get(-1).add(new Line(Line.Direction.HORIZONTAL, x1+2, x2+2, y+2, darkenColor(color)));
        stack.get(0).add(new Line(Line.Direction.HORIZONTAL, x1, x2, y, color));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderBackground(matrices);
        drawVerticalLineWithLayer(width/4, height/4, height/4*3, 0xffffffff);
        drawVerticalLineWithLayer(width/4*3, height/4, height/4*3, 0xffffffff);
        drawHorizontalLineWithLayer(width/4, width/4*3, height/4, 0xffffffff);
        drawHorizontalLineWithLayer(width/4, width/4*3, height/4*3, 0xffffffff);
        renderStack(matrices);
    }

    @Override
    protected void init() {
    }
}
