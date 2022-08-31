package org.penguinencounter.astria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class AstriaClient implements ClientModInitializer {
    public static final String VERSION = "(development 0.0.0)";
    public static final Logger LOGGER = LoggerFactory.getLogger("Astria Client");
    public static AstriaClient instance = null;

    @Override
    public void onInitializeClient() {
        // good luck!
        instance = this;

        ComponentRegistry.readState();
        LOGGER.info("Astria Client v" + VERSION + " init");
    }
}
