package com.kr1s1s.subtlyd.mixin.client.renderer.entity.state;

import com.kr1s1s.subtlyd.client.renderer.state.LivingEntityRenderStateAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateAccessor {
    private float climbProgress;
    private float climbRotation;
    private boolean jockey;

    /**
     * @return The progress of the climb animation.
     */
    @Override public float subtlyDungeons$getClimbProgress() {
        return this.climbProgress;
    }

    /**
     * @return The desired rotation angle.
     */
    @Override public float subtlyDungeons$getClimbRotation() {
        return this.climbRotation;
    }

    /**
     * @return Whether the render state is for a jockey or not.
     */
    @Override public boolean subtlyDungeons$isJockey() {
        return jockey;
    }

    /**
     * Sets the climb animation progress value.
     * @param progress The desired progress value.
     */
    @Override public void subtlyDungeons$setClimbProgress(float progress) {
        this.climbProgress = progress;
    }

    /**
     * Sets the climb rotation angle.
     * @param rotation The desired rotation angle.
     */
    @Override public void subtlyDungeons$setClimbRotation(float rotation) {
        this.climbRotation = rotation;
    }

    /**
     * Sets the "jockey" status of the render state.
     * @param isJockey The jockey status.
     */
    @Override public void subtlyDungeons$setJockey(boolean isJockey) {
        this.jockey = isJockey;
    }
}
