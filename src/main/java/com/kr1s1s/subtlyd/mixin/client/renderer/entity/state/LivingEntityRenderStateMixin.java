package com.kr1s1s.subtlyd.mixin.client.renderer.entity.state;

import com.kr1s1s.subtlyd.client.renderer.state.SpiderRenderStateAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements SpiderRenderStateAccessor {
    private float climbProgress;
    private float climbYaw;
    private boolean jockey;

    @Override public float getClimbProgress() {
        return this.climbProgress;
    }

    @Override public float getClimbYaw() {
        return this.climbYaw;
    }

    @Override
    public boolean isJockey() {
        return jockey;
    }

    @Override public void setClimbProgress(float progress) {
        this.climbProgress = progress;
    }

    @Override public void setClimbYaw(float yaw) {
        this.climbYaw = yaw;
    }

    @Override
    public void setJockey(boolean isJockey) {
        this.jockey = isJockey;
    }

}
