package net.meander.subtlyd.mixin.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {
    @Shadow @Final private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
    private void modifyChorusSoundDistance(int eventType, BlockPos pos, int data, CallbackInfo ci) {
        if (eventType == 1033 || eventType == 1034) { // CHORUS_FLOWER_GROW and CHORUS_FLOWER_DEATH
            SoundEvent sound = (eventType == 1033) ? SoundEvents.CHORUS_FLOWER_GROW : SoundEvents.CHORUS_FLOWER_DEATH;
            level.playLocalSound(pos, sound, SoundSource.BLOCKS, 3.0F, 1.0F, false);
            ci.cancel();
        }
    }
}