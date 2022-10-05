package org.penguinencounter.astria.api;

public abstract class ComponentSwitchBase implements Component {
    protected boolean state = false;
    @Override
    public void enable() {
        state = true;
    }

    @Override
    public void disable() {
        state = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        state = enabled;
    }

    @Override
    public boolean isEnabled() {
        return state;
    }
}
