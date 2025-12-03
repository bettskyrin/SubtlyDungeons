package com.kr1s1s.subtlyd.mixin.client.renderer;

import com.kr1s1s.subtlyd.client.util.WorldIconState;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @SuppressWarnings("DataFlowIssue")
    GameRenderer gameRenderer = (GameRenderer) (Object) this;

    @Inject(method = "takeAutoScreenshot", at = @At("HEAD"), cancellable = true)
    private void cancelVanillaScreenshot(Path path, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.AFTER))
    private void captureScreenshot(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        if (WorldIconState.pathHolder != null) {
            Path path = WorldIconState.pathHolder;
            WorldIconState.pathHolder = null;

            if (gameRenderer.getMinecraft().levelRenderer.countRenderedSections() > 10 && gameRenderer.getMinecraft().levelRenderer.hasRenderedAllSections()) {
                Screenshot.takeScreenshot(gameRenderer.getMinecraft().getMainRenderTarget(), sourceImage -> Util.ioPool().execute(() -> {
                    int targetWidth = 455;
                    int targetHeight = 256;

                    int sourceWidth = sourceImage.getWidth();
                    int sourceHeight = sourceImage.getHeight();

                    int cropX = 0;
                    int cropY = 0;
                    int cropWidth = sourceWidth;
                    int cropHeight = sourceHeight;

                    float targetRatio = (float) targetWidth / targetHeight;
                    float sourceRatio = (float) sourceWidth / sourceHeight;

                    if (sourceRatio > targetRatio) {
                        cropWidth = (int) (sourceHeight * targetRatio);
                        cropX = (sourceWidth - cropWidth) / 2;
                    } else {
                        cropHeight = (int) (sourceWidth / targetRatio);
                        cropY = (sourceHeight - cropHeight) / 2;
                    }

                    try (NativeImage scaledImage = new NativeImage(targetWidth, targetHeight, false)) {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            LogUtils.getLogger().warn("Could not delete old world icon", e);
                        }
                        sourceImage.resizeSubRectTo(cropX, cropY, cropWidth, cropHeight, scaledImage);
                        scaledImage.writeToFile(path);
                    } catch (IOException var16) {
                        LogUtils.getLogger().warn("Couldn't save auto screenshot", var16);
                    } finally {
                        sourceImage.close();
                    }
                }));
            }
        }
    }
}
