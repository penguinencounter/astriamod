package org.penguinencounter.astria.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.penguinencounter.astria.AstriaClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {
    public static final Map<Identifier, Component> components = new HashMap<>();
    public static Map<Identifier, Boolean> inherentState = new HashMap<>();

    public static <T extends Component> T getInstance(Identifier id, Class<T> type) {
        Component component = components.get(id);
        if (component == null) {
            AstriaClient.LOGGER.error("Component " + id + " not found!");
            return null;
        }
        if (type.isInstance(component)) {
            return type.cast(component);
        } else {
            AstriaClient.LOGGER.error("Component " + id + " is not of type " + type.getName());
            return null;
        }
    }

    public static void register(Identifier name, Component component, boolean initEnabled) {
        components.put(name, component);
        component.setEnabled(inherentState.getOrDefault(name, initEnabled));
        AstriaClient.LOGGER.info("Registered component " + name);
        writeState();
    }
    public static void register(Identifier name, Component component) {
        register(name, component, false);
    }

    public static Map<Identifier, Boolean> buildStateList() {
        Map<Identifier, Boolean> stateList = new HashMap<>();
        for (Map.Entry<Identifier, Component> entry : components.entrySet()) {
            stateList.put(entry.getKey(), entry.getValue().isEnabled());
        }
        return stateList;
    }

    public static void writeState() {
        Map<Identifier, Boolean> stateList = buildStateList();
        // Write state to <GAME DIRECTORY>/config/astria/components.json
        File astriaConfigDir = FabricLoader.getInstance().getConfigDir().resolve("astria").toFile();
        if (astriaConfigDir.mkdir()) AstriaClient.LOGGER.info("Created Astria config directory");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(stateList, Map.class);
        File stateFile = astriaConfigDir.toPath().resolve("components.json").toFile();

        if (stateFile.delete()) AstriaClient.LOGGER.info("Deleted old Astria component state file");

        try (FileWriter writer = new FileWriter(stateFile)) {
            writer.append(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void transfer() {
        for (Map.Entry<Identifier, Boolean> entry : inherentState.entrySet()) {
            if (!components.containsKey(entry.getKey())) {
                AstriaClient.LOGGER.warn("Component " + entry.getKey() + " is not registered, but has a state in the config file");
            } else {
                components.get(entry.getKey()).setEnabled(entry.getValue());
            }
        }
    }

    public static void readState() {
        // Read state from <GAME DIRECTORY>/config/astria/components.json
        File astriaConfigDir = FabricLoader.getInstance().getConfigDir().resolve("astria").toFile();
        if (!astriaConfigDir.exists()) {
            AstriaClient.LOGGER.info("Astria config directory does not exist, cancelling load!");
            return;
        }

        File stateFile = astriaConfigDir.toPath().resolve("components.json").toFile();
        if (!stateFile.exists()) {
            AstriaClient.LOGGER.info("Astria component state file does not exist, cancelling load!");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader(stateFile)) {
            inherentState = new HashMap<>();
            Map<?, ?> imported = gson.fromJson(reader, Map.class);
            for (Map.Entry<?, ?> entry : imported.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof Boolean) {
                    inherentState.put(new Identifier((String) entry.getKey()), (Boolean) entry.getValue());
                } else {
                    AstriaClient.LOGGER.warn("Invalid component state entry: " + entry.getKey() + " = " + entry.getValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        transfer();
    }
}
