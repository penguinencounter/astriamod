package org.penguinencounter.astria.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    public static ConfigScreen init(Text title) {
        return new ConfigScreen(title);
    }
    protected ConfigScreen(Text title) {
        super(Text.translatable("astria.config"));
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

    private void drawVerticalLineWithLayer(MatrixStack matrices, int x, int y1, int y2, int color) {
        drawVerticalLine(matrices, x+2, y1+2, y2+2, darkenColor(color));
        drawVerticalLine(matrices, x, y1, y2, color);
    }

    private void drawHorizontalLineWithLayer(MatrixStack matrices, int x1, int x2, int y, int color) {
        drawHorizontalLine(matrices, x1+2, x2+2, y+2, darkenColor(color));
        drawHorizontalLine(matrices, x1, x2, y, color);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderBackground(matrices);
        drawVerticalLineWithLayer(matrices, width/4, height/4, height/4*3, 0xffffffff);
        drawVerticalLineWithLayer(matrices, width/4*3, height/4, height/4*3, 0xffffffff);
        drawHorizontalLineWithLayer(matrices, width/4, width/4*3, height/4, 0xffffffff);
        drawHorizontalLineWithLayer(matrices, width/4, width/4*3, height/4*3, 0xffffffff);
    }

    @Override
    protected void init() {
    }
}
