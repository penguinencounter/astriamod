package org.penguinencounter.astria.api;

public interface Component {
    static void register() {
        throw new UnsupportedOperationException("This component does not support registration");
    }

    void enable();
    void disable();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    default void initializer() {}
}
