package org.penguinencounter.astria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.api.Dynamo;
import org.penguinencounter.astria.base.AstriaBaseComponent;
import org.penguinencounter.astria.base.ComponentManagerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class AstriaClient implements ClientModInitializer {
    public static String VERSION = "Unknown. Is the mod ID correct (\"astria\")?";
    public static final Logger LOGGER = LoggerFactory.getLogger("Astria Client");
    public static AstriaClient instance = null;

    private static Thread initializerThread = null;

    private void registration() {
        ComponentManagerComponent.register();
        AstriaBaseComponent.register();
    }

    @Override
    public void onInitializeClient() {
        // good luck!
        initializerThread = Thread.currentThread();
        instance = this;
        Dynamo.enqueueTask(this::registration);
        Dynamo.enqueueTask(ComponentRegistry::readState);
        Dynamo.enqueueTask(ComponentRegistry::cueInitialization);

        // Find this mod's version
        FabricLoader.getInstance().getModContainer("astria").ifPresent(modContainer -> {
            VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
            if (VERSION.equals("${version}")) VERSION = "(development)";
            AstriaBaseComponent.invalidateVersionText();
        });
        LOGGER.info("Astria Client v" + VERSION + " init");
        Dynamo.lock();
        LOGGER.info("Astria Client: Dynamo loading " + Dynamo.tasks.size() + " tasks");
        for (Runnable task : Dynamo.tasks) {
            task.run();
        }
        Dynamo.tasks.clear();
    }

    public static Thread getInitializerThread() {
        return initializerThread;
    }
}
