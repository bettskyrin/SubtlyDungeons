package net.meander.subtlyd.mixin.common.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.level.storage.WorldIconState;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.PngInfo;
import net.minecraft.world.level.storage.LevelStorageSource;
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
    @Shadow @Final protected LevelStorageSource.LevelStorageAccess storageSource;

    @Inject(method = "loadStatusIcon", at = @At("RETURN"), cancellable = true)
    private void changeIcon(CallbackInfoReturnable<Optional<ServerStatus.Favicon>> ci, @Local(name = "iconPath") Optional<Path> iconPath) {
        ci.setReturnValue(newLoadStatusIcon(iconPath));
    }

    /**
     * Prevents common check for favicon size of 64x64 pixels (in favor of the new 455x256 16:9 ratio).
     */
    private Optional<ServerStatus.Favicon> newLoadStatusIcon(Optional<Path> iconPath) {
        return iconPath.flatMap(path -> {
            try {
                byte[] readBytes = Files.readAllBytes(path);
                PngInfo pngInfo = PngInfo.fromBytes(readBytes);

                if (pngInfo.width() == 455 && pngInfo.height() == 256) {
                    return Optional.of(new ServerStatus.Favicon(readBytes));
                } else {
                    throw new IllegalArgumentException("Invalid world icon size [" + pngInfo.width() + ", " + pngInfo.height() + "], but expected [455, 256]");
                }
            } catch (Exception e) {
                UtilSD.LOGGER.error("Couldn't load common icon: {}", e.getMessage());
                return Optional.empty();
            }
        });
    }

    @Inject(method = "saveEverything", at = @At("RETURN"))
    private void saveWorldThumbnail(boolean silent, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        storageSource.getIconFile().ifPresent(path -> {
            synchronized (WorldIconState.class) {
                WorldIconState.pathHolder = path;
            }
        });
    }
}