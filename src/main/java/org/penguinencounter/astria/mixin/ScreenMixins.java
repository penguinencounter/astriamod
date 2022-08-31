package org.penguinencounter.astria.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.base.AstriaBaseComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ScreenMixins {
    @Mixin(TitleScreen.class)
    public static class TitleScreenTextAddon {
        @Inject(
                method = "render",
                at = @At("TAIL")
        )
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            AstriaBaseComponent cpnt = ComponentRegistry.getInstance(AstriaBaseComponent.ID, AstriaBaseComponent.class);
            if (cpnt == null) return;
            cpnt.additions(matrices, instance);
        }
    }

    @Mixin(GameMenuScreen.class)
    public static class GameMenuScreenTextAddon {
        @Inject(
                method = "render",
                at = @At("TAIL")
        )
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            AstriaBaseComponent cpnt = ComponentRegistry.getInstance(AstriaBaseComponent.ID, AstriaBaseComponent.class);
            if (cpnt == null) return;
            cpnt.additions(matrices, instance);
        }
    }
}
