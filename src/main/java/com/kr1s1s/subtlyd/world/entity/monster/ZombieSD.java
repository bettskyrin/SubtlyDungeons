package com.kr1s1s.subtlyd.world.entity.monster;

import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class ZombieSD extends Zombie {
    public ZombieSD(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean isLeader(Zombie zombie) {
        return zombie.getEntityData().get(SynchedEntityDataSD.DATA_LEADER_ID);
    }
}
