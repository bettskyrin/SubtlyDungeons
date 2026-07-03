package net.meander.subtlyd.client.renderer.state;

public interface UndeadRenderStateAccessor {
    /**
     * @return Whether a zombie is a leader zombie or not.
     */
    boolean isLeader();

    /**
     * Sets a zombie entity's leader status.
     * @param isLeader The leader status.
     */
    void setLeader(boolean isLeader);
}