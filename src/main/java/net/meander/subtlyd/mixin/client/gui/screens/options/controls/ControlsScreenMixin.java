package net.meander.subtlyd.mixin.client.gui.screens.options.controls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.client.gui.screens.CommandMacrosScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ControlsScreen.class)
public class ControlsScreenMixin {
    @Inject(method = "options", at = @At("RETURN"), cancellable = true)
    private static void options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        List<OptionInstance<?>> optionInstanceList = new ArrayList<>(List.of(cir.getReturnValue().clone()));

        optionInstanceList.add(3, OptionsSD.shieldCrouch());
        cir.setReturnValue(optionInstanceList.toArray(new OptionInstance[0]));
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addOptions(CallbackInfo ci) {
        ControlsScreen screen = (ControlsScreen) (Object) this;

        if (screen.list != null) {
            screen.list.addSmall(Collections.singletonList(Button.builder(Component.translatable("options.command_macros"), _ -> Minecraft.getInstance().setScreenAndShow(new CommandMacrosScreen(screen))).build()));
        }
    }
}