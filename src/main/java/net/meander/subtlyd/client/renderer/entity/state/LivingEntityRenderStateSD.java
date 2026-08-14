package net.meander.subtlyd.client.renderer.entity.state;

public interface LivingEntityRenderStateSD {
    float getClimbProgress();

    float getClimbYaw();

    boolean isJockey();

    void setClimbProgress(float climbProgress);

    void setClimbYaw(float climbYaw);

    void setIsJockey(boolean isJockey);
}
