package net.meander.subtlyd.client.entity;

public interface ScansorialEntity {
    float getClimbAnimationProgress(float partialTicks);

    float getRoll(float partialTicks);

    void tickAndLerpRoll(float targetRoll);
}