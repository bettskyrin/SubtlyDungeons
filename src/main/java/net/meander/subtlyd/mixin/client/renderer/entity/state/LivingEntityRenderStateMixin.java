package net.meander.subtlyd.mixin.client.renderer.entity.state;

import net.meander.subtlyd.client.renderer.entity.state.LivingEntityRenderStateSD;
import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateSD, QuiverRenderState {
    private boolean isJockey;
    private boolean hasQuiver;
    private float climbProgress;
    private float climbYaw;
    private Identifier quiverTexture = UtilSD.identifier("textures/entity/equipment/quiver.png");

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
    public boolean hasQuiver() {
        return hasQuiver;
    }

    @Override
    public Identifier getQuiverTexture() {
        return quiverTexture;
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

    @Override
    public void setHasQuiver(boolean hasQuiver) {
        this.hasQuiver = hasQuiver;
    }

    @Override
    public void setQuiverTexture(Identifier texture) {
        this.quiverTexture = texture;
    }
}