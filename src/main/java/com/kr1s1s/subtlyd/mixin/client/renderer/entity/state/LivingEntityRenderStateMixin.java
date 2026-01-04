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
    private boolean isJockey;
    private boolean isUpsideDown;

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
     * @return Whether the render state is for a isJockey or not.
     */
    @Override public boolean subtlyDungeons$isJockey() {
        return isJockey;
    }

    /**
     * @return Whether the climbing entity is upside down or not.
     */
    @Override
    public boolean subtlyDungeons$isUpsideDown() {
        return isUpsideDown;
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
     * Sets the "isJockey" status of the render state.
     * @param bl The isJockey status.
     */
    @Override public void subtlyDungeons$setJockey(boolean bl) {
        this.isJockey = bl;
    }

    /**
     * Sets whether a climber entity is upside down or not.
     * @param bl The upside down status.
     */
    @Override
    public void subtlyDungeons$setUpsideDown(boolean bl) {
        isUpsideDown = bl;
    }
}