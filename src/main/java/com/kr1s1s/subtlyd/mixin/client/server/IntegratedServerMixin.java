package com.kr1s1s.subtlyd.mixin.client.server;

import com.kr1s1s.subtlyd.client.util.WorldIconState;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
    @Shadow @Final private Minecraft minecraft;

    public IntegratedServerMixin(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, LevelLoadListener levelLoadListener) {
        super(thread, levelStorageAccess, packRepository, worldStem, proxy, dataFixer, services, levelLoadListener);
    }

    @Inject(method = "saveEverything", at = @At("RETURN"))
    private void saveWorldScreenshot(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        this.storageSource.getIconFile().ifPresent(path -> {
            WorldIconState.pathHolder = path;
        });
    }
}
