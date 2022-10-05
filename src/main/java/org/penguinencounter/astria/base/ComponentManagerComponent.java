package org.penguinencounter.astria.base;

import net.minecraft.util.Identifier;
import org.penguinencounter.astria.api.ComponentRegistry;
import org.penguinencounter.astria.api.ComponentSwitchBase;

public class ComponentManagerComponent extends ComponentSwitchBase {
    public static final Identifier ID = Identifier.of("astria", "component_manager");

    public static void register() {
        ComponentRegistry.register(ID, new ComponentManagerComponent(), true);
    }
}
