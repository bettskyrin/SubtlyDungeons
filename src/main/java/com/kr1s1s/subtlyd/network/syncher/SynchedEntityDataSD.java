package com.kr1s1s.subtlyd.network.syncher;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;

public class SynchedEntityDataSD {
    public static final EntityDataAccessor<Boolean> DATA_LEADER_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
    public static void init() {}
}
