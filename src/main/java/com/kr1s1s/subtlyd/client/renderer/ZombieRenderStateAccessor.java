package com.kr1s1s.subtlyd.client.renderer;

public interface ZombieRenderStateAccessor {
    /**
     * @return Whether a zombie is a leader zombie or not.
     */
    boolean subtlyDungeons$isLeader();

    /**
     * Sets a zombie entity's leader status.
     * @param isLeader The leader status.
     */
    void subtlyDungeons$setLeader(boolean isLeader);
}