package net.meander.subtlyd.mixin.client;

import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.commands.CommandMacroManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void listenForCmdMacro(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action == 1) {
            if ((event.modifiers() & GLFW.GLFW_MOD_ALT) != 0) {
                for (int i = 0; i < 10; i++) {
                    if (OptionsSD.MACRO_KEYS[i].matches(event)) {
                        CommandMacroManager.execute(i);
                        break;
                    }
                }
            }
        }
    }
}