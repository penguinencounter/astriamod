package org.penguinencounter.astria.base;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.penguinencounter.astria.GradialText;
import org.penguinencounter.astria.api.Component;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.api.ComponentSwitchBase;

import static org.penguinencounter.astria.AstriaClient.VERSION;

public class AstriaBaseComponent extends ComponentSwitchBase {
    public static final Identifier ID = Identifier.of("astria", "base");

    public static GradialText.GradientStop ASTRAL_STOP_1 = new GradialText.GradientStop(0x8000ff, 0);
    public static GradialText.GradientStop ASTRAL_STOP_2 = new GradialText.GradientStop(0x00ff80, 1);
    public static MutableText astralText = GradialText.build(Text.literal("Astria v" + VERSION), ASTRAL_STOP_1, ASTRAL_STOP_2);

    public static void register() {
        ComponentRegistry.register(ID, new AstriaBaseComponent(), true);
    }

    public static void invalidateVersionText() {
        astralText = GradialText.build(Text.literal("Astria v" + VERSION), ASTRAL_STOP_1, ASTRAL_STOP_2);
    }

    private void renderText(MatrixStack target, MutableText content, Screen inst, int yOffset) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        DrawableHelper.drawCenteredText(target, tr, content, inst.width / 2, inst.height - yOffset, 0xffffff);
    }

    public void additions(MatrixStack target, Screen inst) {
        if (!isEnabled()) return;
        renderText(target, astralText, inst, 11);

        int activeCount = 0;
        for (Component component : ComponentRegistry.components.values()) {
            if (component.isEnabled()) activeCount++;
        }

        MutableText componentCount = GradialText.build(Text.literal(
                ComponentRegistry.components.size() + " components loaded (" + activeCount + " active)")
                , ASTRAL_STOP_1, ASTRAL_STOP_2);
        renderText(target, componentCount, inst, 21);
    }
}
