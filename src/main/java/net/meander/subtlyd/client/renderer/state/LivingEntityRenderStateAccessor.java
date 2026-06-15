package net.meander.subtlyd.client.renderer.state;

public interface LivingEntityRenderStateAccessor {
    /**
     * @return The progress of the climb animation.
     */
    float getClimbProgress();

    /**
     * @return The desired rotation angle.
     */
    float getClimbRotation();

    /**
     * @return Whether the render state is for a jockey or not.
     */
    boolean isJockey();

    /**
     * Sets the climb animation progress value.
     * @param progress The desired progress value.
     */
    void setClimbProgress(float progress);

    /**
     * Sets the climb rotation angle.
     * @param rotation The desired rotation angle.
     */
    void setClimbRotation(float rotation);

    /**
     * Sets the "jockey" status of the render state.
     * @param bl The jockey status.
     */
    void setIsJockey(boolean bl);
}
