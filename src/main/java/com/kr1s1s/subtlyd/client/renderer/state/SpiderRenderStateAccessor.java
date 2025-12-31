package com.kr1s1s.subtlyd.client.renderer.state;

public interface SpiderRenderStateAccessor {
    void setClimbProgress(float progress);
    void setClimbYaw(float yaw);
    void setJockey(boolean isJockey);
    float getClimbProgress();
    float getClimbYaw();
    boolean isJockey();
}
