package net.meander.subtlyd.mixin.client.model.monster.zombie;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedModel.class)
public abstract class DrownedModelMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)V", at = @At("HEAD"))
    private void unsetSwingAnimationForThrow(ZombieRenderState state, CallbackInfo ci) {
        if (state.rightArmPose == HumanoidModel.ArmPose.THROW_TRIDENT || state.leftArmPose == HumanoidModel.ArmPose.THROW_TRIDENT) {
            state.swingAnimationType = SwingAnimationType.NONE;
        }
    }
}