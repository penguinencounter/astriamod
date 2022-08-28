package org.penguinencounter.astria;

import net.minecraft.client.gui.screen.*;

import java.util.List;

public class AstriasConstants {
    public static final List<Class<? extends Screen>> loadingScreens = List.of(
            LevelLoadingScreen.class,
            DownloadingTerrainScreen.class,
            ConnectScreen.class,
            ProgressScreen.class,
            MessageScreen.class
    );
}
