package com.kr1s1s.subtlyd.client.entity.monster;

public interface SpiderAnimationAccessor {
    /**
     * Used for smoothing climber animations.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    float getClimbTransition(float partialTicks);

    /**
     * @return The rotation angle of the climber entity.
     */
    float getRotation(float partialTicks);

    /**
     * Used to move the targeted climber angle based on the current tick.
     * @param rotation The target rotation.
     */
    void tickRotation(float rotation);
}