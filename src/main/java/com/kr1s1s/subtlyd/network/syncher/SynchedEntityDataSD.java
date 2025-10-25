package com.kr1s1s.subtlyd.network.syncher;

import com.kr1s1s.subtlyd.world.entity.TentEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;

public class SynchedEntityDataSD {
    public static final EntityDataAccessor<Boolean> DATA_LEADER_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.FLOAT);
    public static void init() {}
}
