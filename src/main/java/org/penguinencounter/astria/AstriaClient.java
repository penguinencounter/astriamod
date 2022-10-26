package org.penguinencounter.astria;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.api.Dynamo;
import org.penguinencounter.astria.api.KeyAnywhere;
import org.penguinencounter.astria.base.AstriaBaseComponent;
import org.penguinencounter.astria.base.ComponentManagerComponent;
import org.penguinencounter.astria.generics.Pair;
import org.penguinencounter.astria.ui.ConfigScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class AstriaClient implements ClientModInitializer {
    public static final ConfigScreen CONFIG_SCREEN = ConfigScreen.init(Text.translatable("astria.config"));

    public static String VERSION = "Unknown. Is the mod ID correct (\"astria\")?";
    public static final Logger LOGGER = LoggerFactory.getLogger("Astria Client");
    public static AstriaClient instance = null;
    private static Thread initializerThread = null;
    private static KeyBinding openConfig;

    private void registration() {
        ComponentManagerComponent.register();
        AstriaBaseComponent.register();
    }

    private boolean keyMatchesList(KeyBinding kb, List<Pair<Integer, Integer>> pressed) {
        for (var pair : pressed) {
            if (kb.matchesKey(pair.a(), pair.b())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInitializeClient() {
        // good luck!
        initializerThread = Thread.currentThread();
        openConfig = new KeyBinding(
                "key.astria.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "category.astria"
        );
        KeyAnywhere.registerKeyBinding("key.astria.config", openConfig);
        KeyAnywhere.registerOnDownEdge("key.astria.config", () -> {
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                LOGGER.info("Opening config screen");
            }
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.currentScreen == CONFIG_SCREEN) return;
            client.setScreen(CONFIG_SCREEN);
        });

        // Find this mod's version
        FabricLoader.getInstance().getModContainer("astria").ifPresent(modContainer -> {
            VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
            if (VERSION.equals("${version}")) VERSION = "(development)";
            AstriaBaseComponent.invalidateVersionText();
        });
        instance = this;

        // defer after loading
        ClientLifecycleEvents.CLIENT_STARTED.register(cli -> {
            Dynamo.enqueueTask(this::registration);
            Dynamo.enqueueTask(ComponentRegistry::readState);
            Dynamo.enqueueTask(ComponentRegistry::cueInitialization);

            LOGGER.info("Astria Client v" + VERSION + " init");
            Dynamo.lock();
            LOGGER.info("Astria Client: Dynamo loading " + Dynamo.tasks.size() + " tasks");
            for (Runnable task : Dynamo.tasks) {
                task.run();
            }
            Dynamo.tasks.clear();

        });

        ClientTickEvents.END_CLIENT_TICK.register(cli -> {
            List<Pair<Integer, Integer>> pressedKeys = new ArrayList<>();
            for (Map.Entry<Pair<Integer, Integer>, Boolean> entry : KeyAnywhere.PRESSED.entrySet()) {
                if (entry.getValue()) {
                    pressedKeys.add(entry.getKey());
                }
            }

            for (Map.Entry<String, KeyBinding> action : KeyAnywhere.KEY_BINDINGS.entrySet()) {
                String identifier = action.getKey();
                KeyBinding keyBinding = action.getValue();
                boolean isPressed = keyBinding.isPressed();
                List<KeyAnywhere.KeyAction> pressedActions = KeyAnywhere.KEY_ACTIONS.stream()
                        .filter(a -> a.keyBinding().equals(identifier) && a.type().equals(KeyAnywhere.Type.DOWN_EDGE))
                        .toList();
                List<KeyAnywhere.KeyAction> upEdgeActions = KeyAnywhere.KEY_ACTIONS.stream()
                        .filter(a -> a.keyBinding().equals(identifier) && a.type().equals(KeyAnywhere.Type.UP_EDGE))
                        .toList();
                List<KeyAnywhere.KeyAction> notDownActions = KeyAnywhere.KEY_ACTIONS.stream()
                        .filter(a -> a.keyBinding().equals(identifier) && a.type().equals(KeyAnywhere.Type.NOT_PRESSED))
                        .toList();
                List<KeyAnywhere.KeyAction> downActions = KeyAnywhere.KEY_ACTIONS.stream()
                        .filter(a -> a.keyBinding().equals(identifier) && a.type().equals(KeyAnywhere.Type.HELD))
                        .toList();
                while (keyBinding.wasPressed()) {
                    for (KeyAnywhere.KeyAction action1 : pressedActions) {
                        action1.action().run();
                    }
                }
                if (isPressed || keyMatchesList(keyBinding, pressedKeys)) {
                    for (KeyAnywhere.KeyAction action1 : downActions) {
                        action1.action().run();
                    }
                }
                if ((!isPressed) || (!keyMatchesList(keyBinding, pressedKeys))) {
                    for (KeyAnywhere.KeyAction action1 : notDownActions) {
                        action1.action().run();
                    }
                }
            }
        });
    }

    public static Thread getInitializerThread() {
        return initializerThread;
    }
}
