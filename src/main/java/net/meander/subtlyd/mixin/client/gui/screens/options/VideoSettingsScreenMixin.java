package net.meander.subtlyd.mixin.client.gui.screens.options;

import net.meander.subtlyd.client.OptionsSD;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(VideoSettingsScreen.class)
public class VideoSettingsScreenMixin { @Inject(method = "displayOptions", at = @At("RETURN"), cancellable = true)
    private static void displayOptions(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        List<OptionInstance<?>> optionInstanceList = new ArrayList<>(List.of(cir.getReturnValue().clone()));

        optionInstanceList.add(8, OptionsSD.gui());
        cir.setReturnValue(optionInstanceList.toArray(new OptionInstance[0]));
    }

    @Inject(method = "qualityOptions", at = @At("RETURN"), cancellable = true)
    private static void qualityOptions(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        List<OptionInstance<?>> optionInstanceList = new ArrayList<>(List.of(cir.getReturnValue().clone()));

        optionInstanceList.add(10, OptionsSD.advancedEntityAnimations());
        cir.setReturnValue(optionInstanceList.toArray(new OptionInstance[0]));
    }
}
