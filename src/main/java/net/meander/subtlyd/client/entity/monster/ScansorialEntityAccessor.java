package net.meander.subtlyd.client.entity.monster;

public interface ScansorialEntityAccessor {
     /**
     * Used for smoothing climbing animations.
     * @param partialTicks The partial ticks.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    float getClimbTransition(float partialTicks);

    /**
     * @return The rotation angle of the climbing entity.
     */
    float getRotation(float partialTicks);

    /**
     * Used to move the targeted climbing angle based on the targeted angle.
     * @param targetRot The target rotation.
     */
    void tickRotation(float targetRot);
}