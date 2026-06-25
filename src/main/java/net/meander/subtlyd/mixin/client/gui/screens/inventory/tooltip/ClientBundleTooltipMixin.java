package net.meander.subtlyd.mixin.client.gui.screens.inventory.tooltip;

import net.meander.subtlyd.world.item.QuiverItemSD;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBundleTooltip.class)
public abstract class ClientBundleTooltipMixin {
    @Shadow @Final private static Identifier PROGRESSBAR_FULL_SPRITE;
    @Shadow @Final private static Identifier PROGRESSBAR_FILL_SPRITE;
    @Shadow @Final private static Component BUNDLE_FULL_TEXT;
    @Shadow @Final private static Component BUNDLE_EMPTY_TEXT;
    private boolean isQuiver = false;
    private static boolean renderingQuiver = false;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void catchQuiverFlag(BundleContents contents, CallbackInfo ci) {
        if (QuiverItemSD.renderingQuiverTooltip) {
            isQuiver = true;
            QuiverItemSD.renderingQuiverTooltip = false;
        }
    }

    @Inject(method = "extractImage", at = @At("HEAD"))
    private void startQuiverRender(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (isQuiver) {
            renderingQuiver = true;
        }
    }

    @Inject(method = "extractImage", at = @At("RETURN"))
    private void endQuiverRender(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics, CallbackInfo ci) {
        renderingQuiver = false;
    }

    @Inject(method = "getProgressBarFill", at = @At("HEAD"), cancellable = true)
    private static void scaleQuiverFill(Fraction weight, CallbackInfoReturnable<Integer> cir) {
        if (renderingQuiver) {
            Fraction normalizedWeight = weight.multiplyBy(Fraction.getFraction(1, 4));
            cir.setReturnValue(Mth.clamp(Mth.mulAndTruncate(normalizedWeight, 94), 0, 94));
        }
    }

    @Inject(method = "getProgressBarTexture", at = @At("HEAD"), cancellable = true)
    private static void scaleQuiverTexture(Fraction weight, CallbackInfoReturnable<Identifier> cir) {
        if (renderingQuiver) {
            cir.setReturnValue(weight.compareTo(Fraction.getFraction(4, 1)) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE);
        }
    }

    @Inject(method = "getProgressBarFillText", at = @At("HEAD"), cancellable = true)
    private static void scaleQuiverText(Fraction weight, CallbackInfoReturnable<Component> cir) {
        if (renderingQuiver) {
            if (weight.compareTo(Fraction.ZERO) == 0) {
                cir.setReturnValue(BUNDLE_EMPTY_TEXT);
            } else {
                cir.setReturnValue(weight.compareTo(Fraction.getFraction(4, 1)) >= 0 ? BUNDLE_FULL_TEXT : null);
            }
        }
    }

    @Inject(method = "extractEmptyBundleDescriptionText", at = @At("HEAD"), cancellable = true)
    private static void customEmptyDescription(int x, int y, Font font, GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (renderingQuiver) {
            graphics.textWithWordWrap(font, Component.translatable("item.subtlyd.quiver.empty.description"), x, y, 96, -5592406);
            ci.cancel();
        }
    }

    @Inject(method = "getEmptyBundleDescriptionTextHeight", at = @At("HEAD"), cancellable = true)
    private static void customEmptyDescriptionHeight(Font font, CallbackInfoReturnable<Integer> cir) {
        if (renderingQuiver) {
            int lines = font.split(Component.translatable("item.subtlyd.quiver.empty.description"), 96).size();

            cir.setReturnValue(lines * 9);
        }
    }
}