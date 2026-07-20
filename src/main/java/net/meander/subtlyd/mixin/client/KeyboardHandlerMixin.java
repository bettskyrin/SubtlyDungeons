package net.meander.subtlyd.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.commands.CommandMacroManager;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void listenForCmdMacro(long handle, int action, KeyEvent event, CallbackInfo ci) {
        if (action == 1) {
            if ((event.modifiers() & InputConstants.MOD_ALT) != 0) {
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