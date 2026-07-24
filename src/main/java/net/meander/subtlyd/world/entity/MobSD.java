package net.meander.subtlyd.world.entity;

public interface MobSD {
    default long getHuntingCooldownTicks() {
        return 20;
    }

    default void setHuntingCooldownTicks(long time) {}
}
