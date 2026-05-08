package net.meander.subtlyd.client.entity.monster;

public interface ClimberAccessor {
     /**
     * Used for smoothing climber animations.
     * @param partialTicks The partial ticks.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    float subtlyd$getClimbTransition(float partialTicks);

    /**
     * @return The rotation angle of the climber entity.
     */
    float subtlyd$getRotation(float partialTicks);

    /**
     * Used to move the targeted climber angle based on the targeted angle.
     * @param targetRot The target rotation.
     */
    void subtlyd$tickRotation(float targetRot);
}