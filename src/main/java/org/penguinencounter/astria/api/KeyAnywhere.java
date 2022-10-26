package org.penguinencounter.astria.api;

import net.minecraft.client.option.KeyBinding;
import org.penguinencounter.astria.generics.DefaultMap;
import org.penguinencounter.astria.generics.Pair;

import java.util.*;

public class KeyAnywhere {
    public static final Map<String, KeyBinding> KEY_BINDINGS = new HashMap<>();

    public enum Type {
        HELD,
        UP_EDGE,
        NOT_PRESSED,
        DOWN_EDGE
    }

    public static final class KeyAction {
        // Generated from record
        private final String keyBinding;
        private final Type type;
        private final Runnable action;

        public KeyAction(String keyBinding, Type type, Runnable action) {
            this.keyBinding = keyBinding;
            this.type = type;
            this.action = action;
        }

        public String keyBinding() {
            return keyBinding;
        }

        public Type type() {
            return type;
        }

        public Runnable action() {
            return action;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (KeyAction) obj;
            return Objects.equals(this.keyBinding, that.keyBinding) &&
                    Objects.equals(this.type, that.type) &&
                    Objects.equals(this.action, that.action);
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyBinding, type, action);
        }

        @Override
        public String toString() {
            return "KeyAction[" +
                    "keyBinding=" + keyBinding + ", " +
                    "type=" + type + ", " +
                    "action=" + action + ']';
        }

        boolean pressedData = false;
    }

    public static final List<KeyAction> KEY_ACTIONS = new ArrayList<>();
    public static final DefaultMap<Pair<Integer, Integer>, Boolean> PRESSED = new DefaultMap<>(false);

    public static void registerKeyBinding(String id, KeyBinding keyBinding) {
        KEY_BINDINGS.put(id, keyBinding);
    }

    public static void registerOnDownEdge(String id, Runnable action) {
        KEY_ACTIONS.add(new KeyAction(id, Type.DOWN_EDGE, action));
    }

    public static void registerWhenHeld(String id, Runnable action) {
        KEY_ACTIONS.add(new KeyAction(id, Type.HELD, action));
    }

    public static void registerWhenNotHeld(String id, Runnable action) {
        KEY_ACTIONS.add(new KeyAction(id, Type.NOT_PRESSED, action));
    }

    public static void registerOnUpEdge(String id, Runnable action) {
        KEY_ACTIONS.add(new KeyAction(id, Type.UP_EDGE, action));
    }
}
