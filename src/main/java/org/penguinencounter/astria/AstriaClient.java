package org.penguinencounter.astria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class AstriaClient implements ClientModInitializer {
    public static final String VERSION = "(development 0.0.0)";
    public static final Logger LOGGER = LoggerFactory.getLogger("Astria Client");
    public static GradialText.GradientStop ASTRAL_STOP_1 = new GradialText.GradientStop(0x8000ff, 0);
    public static GradialText.GradientStop ASTRAL_STOP_2 = new GradialText.GradientStop(0x00ff80, 1);
    public static GradialText.GradientStop ASTRAL_STOP_3 = new GradialText.GradientStop(0x00ff80, 0);
    public static GradialText.GradientStop ASTRAL_STOP_4 = new GradialText.GradientStop(0x8000ff, 1);
    MutableText astralText = GradialText.build(Text.literal("Astria v" + VERSION), ASTRAL_STOP_1, ASTRAL_STOP_2);
    public static AstriaClient instance = null;
    private final long startTime = System.currentTimeMillis();

    @Override
    public void onInitializeClient() {
        // good luck!
        instance = this;
    }

    private void renderTSText(MatrixStack target, Screen inst, int yOffset) {
        long s = (System.currentTimeMillis() - startTime)/1000;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        String timer_text = String.format("Playtime: %dh %02dm %02ds", s / 3600, (s % 3600) / 60, (s % 60));
        MutableText timer = GradialText.build(Text.literal(timer_text), ASTRAL_STOP_3, ASTRAL_STOP_4);
        int w = tr.getWidth(timer);
        DrawableHelper.drawTextWithShadow(target, tr, astralText, 2, inst.height - yOffset, 0xffffff);
        DrawableHelper.drawTextWithShadow(target, tr, timer, inst.width - w - 2, inst.height - yOffset, 0xffffff);
    }

    public void titleScreenAdditions(MatrixStack target, Screen inst) {
        renderTSText(target, inst, 20);
    }

    public void pauseScreenAdditions(MatrixStack target, Screen inst) {
        renderTSText(target, inst, 10);
    }
}
