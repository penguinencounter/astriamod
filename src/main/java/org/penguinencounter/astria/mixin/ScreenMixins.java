package org.penguinencounter.astria.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.util.math.MatrixStack;
import org.penguinencounter.astria.AstriaClient;
import org.penguinencounter.astria.AstriasConstants;
import org.penguinencounter.astria.nicetohave.LoadingScreenTimers;
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
            AstriaClient.instance.titleScreenAdditions(matrices, instance);
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
            AstriaClient.instance.pauseScreenAdditions(matrices, instance);
        }
    }

    @Mixin(LevelLoadingScreen.class)
    public static class LoadingScreen1Addon {
        LoadingScreenTimers timer;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void ready(CallbackInfo ci) {
            timer = LoadingScreenTimers.get();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            timer.render(matrices, instance, 20);
        }
    }
    @Mixin(DownloadingTerrainScreen.class)
    public static class LoadingScreen2Addon {
        LoadingScreenTimers timer;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void ready(CallbackInfo ci) {
            timer = LoadingScreenTimers.get();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            timer.render(matrices, instance, 20);
        }
    }
    @Mixin(ConnectScreen.class)
    public static class LoadingScreen3Addon {
        LoadingScreenTimers timer;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void ready(CallbackInfo ci) {
            timer = LoadingScreenTimers.get();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            timer.render(matrices, instance, 20);
        }
    }
    @Mixin(ProgressScreen.class)
    public static class LoadingScreen4Addon {
        LoadingScreenTimers timer;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void ready(CallbackInfo ci) {
            timer = LoadingScreenTimers.get();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            timer.render(matrices, instance, 20);
        }
    }
    @Mixin(MessageScreen.class)
    public static class LoadingScreen5Addon {
        LoadingScreenTimers timer;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void ready(CallbackInfo ci) {
            timer = LoadingScreenTimers.get();
        }

        @Inject(method = "render", at = @At("TAIL"))
        private void addon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            Screen instance = (Screen) (Object) this;
            timer.render(matrices, instance, 20);
        }
    }

    @Mixin(MinecraftClient.class)
    public static class ScreenWatch {
        @Inject(method = "setScreen", at = @At("HEAD"))
        private void watch(Screen screen, CallbackInfo ci) {
            if (screen == null) AstriaClient.LOGGER.info("Screen closed");
            else {
                AstriaClient.LOGGER.info("Screen changed to " + screen.getClass().getName());
                if (!AstriasConstants.loadingScreens.contains(screen.getClass())) {
                    LoadingScreenTimers.discard();
                }
            }
        }
    }
}
