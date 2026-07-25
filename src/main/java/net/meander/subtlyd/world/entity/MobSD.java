package net.meander.subtlyd.world.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;

public interface MobSD {
    default long getHuntingCooldownTicks() {
        return 20;
    }

    default void setHuntingCooldownTicks(long time) {}

     default boolean canStroll() {
        return (Mob) this instanceof PathfinderMob mob
                && ((mob instanceof TamableAnimal tamable && tamable.shouldNotFollowOwner()) || !(mob instanceof TamableAnimal))
                && !mob.hasControllingPassenger()
                && mob.getTarget() == null;
    }
}
