package com.kr1s1s.subtlyd.mixin.client.renderer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Path;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @SuppressWarnings("DataFlowIssue")
    GameRenderer gameRenderer = (GameRenderer) (Object) this;

    @Inject(method = "takeAutoScreenshot", at = @At("HEAD"), cancellable = true)
    private void takeAutoScreenshot(Path path, CallbackInfo ci) {
        ci.cancel();
        if (gameRenderer.getMinecraft().levelRenderer.countRenderedSections() > 10 && gameRenderer.getMinecraft().levelRenderer.hasRenderedAllSections()) {
            Screenshot.takeScreenshot(gameRenderer.getMinecraft().getMainRenderTarget(), nativeImage -> Util.ioPool().execute(() -> {
                int i = nativeImage.getWidth();
                int j = nativeImage.getHeight();
                int k = 0;
                int l = 0;
                if (i > j) {
                    k = (i - j) / 2;
                    i = j;
                } else {
                    l = (j - i) / 2;
                    j = i;
                }

                try (NativeImage nativeImage2 = new NativeImage(455, 256, false)) {
                    nativeImage.resizeSubRectTo(k, l, i, j, nativeImage2);
                    nativeImage2.writeToFile(path);
                } catch (IOException var16) {
                    LogUtils.getLogger().warn("Couldn't save auto screenshot", var16);
                } finally {
                    nativeImage.close();
                }
            }));
        }
    }
}
