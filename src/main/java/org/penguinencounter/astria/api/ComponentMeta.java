package org.penguinencounter.astria.api;

import com.google.gson.GsonBuilder;
import net.minecraft.text.Text;

/**
 * ComponentMeta is a class that contains information about a component.
 */
public class ComponentMeta {
    private static class ComponentMetaContainer {
        String name;
        String description;
        boolean enableByDefault;
    }

    public Text name;
    public Text description = Text.translatable("astria.component.no_description");

    public ComponentMeta(Text name) {
        this.name = name;
    }

    public ComponentMeta(Text name, Text description) {
        this.name = name;
        this.description = description;
    }

    public static ComponentMeta fromJSON(String json) {
        ComponentMetaContainer cont = new GsonBuilder().create().fromJson(json, ComponentMetaContainer.class);
        return new ComponentMeta(Text.Serializer.fromJson(cont.name), Text.Serializer.fromJson(cont.description));
    }

    public boolean validate() {
        return name != null /* && ... != null */;
    }
}
