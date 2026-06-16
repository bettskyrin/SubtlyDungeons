package net.meander.subtlyd.client.renderer.state;

public interface LivingEntityRenderStateAccessor {
    /**
     * @return The progress of the climb animation.
     */
    float subtlyd$getClimbProgress();

    /**
     * @return The desired rotation angle.
     */
    float subtlyd$getClimbRotation();

    /**
     * @return Whether the render state is for a jockey or not.
     */
    boolean subtlyd$isJockey();

    /**
     * Sets the climb animation progress value.
     * @param progress The desired progress value.
     */
    void subtlyd$setClimbProgress(float progress);

    /**
     * Sets the climb rotation angle.
     * @param rotation The desired rotation angle.
     */
    void subtlyd$setClimbRotation(float rotation);

    /**
     * Sets the "jockey" status of the render state.
     * @param bl The jockey status.
     */
    void subtlyd$setJockey(boolean bl);
}
