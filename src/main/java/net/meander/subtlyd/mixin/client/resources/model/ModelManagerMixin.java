package net.meander.subtlyd.mixin.client.resources.model;

import net.meander.subtlyd.client.data.models.BlockModelGeneratorsSD;
import net.meander.subtlyd.server.packs.VirtualResourceRegistry;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    private void generateVirtualModelsBeforeBake(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        VirtualResourceRegistry.clear();
        BlockModelGeneratorsSD.generateRuntimeBlockModels(currentReload.resourceManager());
    }
}