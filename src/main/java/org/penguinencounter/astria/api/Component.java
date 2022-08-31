package org.penguinencounter.astria.api;

public interface Component {
    void enable();
    void disable();

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
