package com.kr1s1s.subtlyd.mixin.server;

import com.kr1s1s.subtlyd.client.util.WorldIconState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PngInfo;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final protected LevelStorageSource.LevelStorageAccess storageSource;

    @Inject(method = "loadStatusIcon", at = @At("RETURN"), cancellable = true)
    private void changeIcon(CallbackInfoReturnable<Optional<ServerStatus.Favicon>> ci, @Local Optional<Path> optional) {
        ci.setReturnValue(newLoadStatusIcon(optional));
    }

    /**
     * Prevents server check for favicon size of 64x64 pixels (in favor of the new 455x256 16:9 ratio).
     */
    private Optional<ServerStatus.Favicon> newLoadStatusIcon(Optional<Path> optional) {
        return optional.flatMap(path -> {
            try {
                byte[] bs = Files.readAllBytes(path);
                PngInfo pngInfo = PngInfo.fromBytes(bs);
                if (pngInfo.width() == 455 && pngInfo.height() == 256) {
                    return Optional.of(new ServerStatus.Favicon(bs));
                } else {
                    throw new IllegalArgumentException("Invalid world icon size [" + pngInfo.width() + ", " + pngInfo.height() + "], but expected [455, 256]");
                }
            } catch (Exception var3) {
                LOGGER.error("Couldn't load server icon", var3);
                return Optional.empty();
            }
        });
    }

    /**
     * Sets the path for the world thumbnail.
     */
    @Inject(method = "saveEverything", at = @At("RETURN"))
    private void saveWorldScreenshot(boolean silent, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        this.storageSource.getIconFile().ifPresent(path -> {
            synchronized (WorldIconState.class) {
                WorldIconState.pathHolder = path;
            }
        });
    }

}
