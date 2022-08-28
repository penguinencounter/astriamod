package org.penguinencounter.astria.nicetohave;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.penguinencounter.astria.GradialText;

public class LoadingScreenTimers {
    public final long startTime;
    public static GradialText.GradientStop LOADING_STOP_1 = new GradialText.GradientStop(0xe62200, 0);
    public static GradialText.GradientStop LOADING_STOP_2 = new GradialText.GradientStop(0xe6db00, 1);
    public static LoadingScreenTimers instance = null;

    private LoadingScreenTimers() {
        startTime = System.currentTimeMillis();
    }

    public static LoadingScreenTimers get(boolean discard) {
        if (instance == null || discard) {
            instance = new LoadingScreenTimers();
        }
        return instance;
    }
    public static LoadingScreenTimers get() { return get(false); }

    public static void discard() {
        instance = null;
    }

    public void render(MatrixStack target, Screen inst, int yOffset) {
        long now = System.currentTimeMillis();
        long s = (now - startTime)/1000;

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        String timer_text = String.format("Loading... %02d:%02d", s / 60, (s % 60));
        MutableText timer = GradialText.build(Text.literal(timer_text), LOADING_STOP_1, LOADING_STOP_2);
        int w = tr.getWidth(timer);
        DrawableHelper.drawTextWithShadow(target, tr, timer, inst.width - w - 2, inst.height - yOffset, 0xffffff);
    }
}
