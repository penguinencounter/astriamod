package org.penguinencounter.astria.mixin;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import org.penguinencounter.astria.api.KeyAnywhere;
import org.penguinencounter.astria.generics.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Special keybinding flow that runs on screens.
 */
@Mixin(Screen.class)
public abstract class KeyAnywhereMix extends AbstractParentElement implements Drawable {
    @Inject(method = "keyPressed", at = @At("RETURN"), cancellable = true)
    private void onKey(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        KeyAnywhere.PRESSED.put(new Pair<>(keyCode, scanCode), true);
        for (KeyAnywhere.KeyAction action : KeyAnywhere.KEY_ACTIONS) {
            KeyBinding binder = KeyAnywhere.KEY_BINDINGS.get(action.keyBinding());
            if (action.type() != KeyAnywhere.Type.DOWN_EDGE) continue;
            if (binder == null || !binder.matchesKey(keyCode, scanCode)) continue;
            action.action().run();
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        KeyAnywhere.PRESSED.put(new Pair<>(keyCode, scanCode), false);
        for (KeyAnywhere.KeyAction action : KeyAnywhere.KEY_ACTIONS) {
            KeyBinding binder = KeyAnywhere.KEY_BINDINGS.get(action.keyBinding());
            if (action.type() != KeyAnywhere.Type.UP_EDGE) continue;
            if (binder == null || !binder.matchesKey(keyCode, scanCode)) continue;
            action.action().run();
            return true;
        }
        return false;
    }
}
