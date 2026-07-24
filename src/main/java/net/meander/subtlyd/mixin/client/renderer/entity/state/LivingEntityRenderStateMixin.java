package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.entity.state.LivingEntityRenderStateSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateSD {
    private float climbProgress;
    private float climbYaw;
    private boolean isJockey;

    @Override
    public float getClimbProgress() {
        return climbProgress;
    }

    @Override
    public float getClimbYaw() {
        return climbYaw;
    }

    @Override
    public boolean isJockey() {
        return isJockey;
    }

    @Override
    public void setClimbProgress(float climbProgress) {
        this.climbProgress = climbProgress;
    }

    @Override
    public void setClimbYaw(float climbYaw) {
        this.climbYaw = climbYaw;
    }

    @Override
    public void setIsJockey(boolean isJockey) {
        this.isJockey = isJockey;
    }
}