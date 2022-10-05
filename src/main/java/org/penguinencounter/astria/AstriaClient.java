package org.penguinencounter.astria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.base.AstriaBaseComponent;
import org.penguinencounter.astria.base.ComponentManagerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class AstriaClient implements ClientModInitializer {
    public static String VERSION = "Unknown. Is the mod ID correct (\"astria\")?";
    public static final Logger LOGGER = LoggerFactory.getLogger("Astria Client");
    public static AstriaClient instance = null;

    @Override
    public void onInitializeClient() {
        // good luck!
        instance = this;

        AstriaBaseComponent.register();
        ComponentManagerComponent.register();

        // Find this mod's version
        FabricLoader.getInstance().getModContainer("astria").ifPresent(modContainer -> {
            VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
            if (VERSION.equals("${version}")) VERSION = "(development)";
            AstriaBaseComponent.invalidateVersionText();
        });
        ComponentRegistry.readState();
        LOGGER.info("Astria Client v" + VERSION + " init");
    }
}
