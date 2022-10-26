package org.penguinencounter.astria.ui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    public static ConfigScreen init(Text title) {
        return new ConfigScreen(title);
    }
    protected ConfigScreen(Text title) {
        super(Text.translatable("astria.config"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        this.addDrawableChild(
                new ButtonWidget(this.width / 2 - 100, this.height / 2 - 10, 200, 20,
                        Text.literal("what"),
                        button -> {
                            if (this.client == null || this.client.player == null) return;
                            this.client.player.sendCommand("me HELP", null);
                        }
                )
        );
    }
}
