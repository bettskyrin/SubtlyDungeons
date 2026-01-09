package com.kr1s1s.subtlyd.client.entity.monster;

public interface ClimberAccessor {
     /**
     * Used for smoothing climber animations.
     * @param partialTicks The partial ticks.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    float subtlyDungeons$getClimbTransition(float partialTicks);

    /**
     * @return The rotation angle of the climber entity.
     */
    float subtlyDungeons$getRotation(float partialTicks);

    /**
     * Used to move the targeted climber angle based on the targeted angle.
     * @param targetRot The target targetRot.
     */
    void subtlyDungeons$tickRotation(float targetRot);
}