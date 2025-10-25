package com.kr1s1s.subtlyd.network.syncher;

import com.kr1s1s.subtlyd.client.entity.mosnter.ZombieSD;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

public class SynchedEntityDataSD {
    public static final EntityDataAccessor<Boolean> DATA_LEADER_ID = SynchedEntityData.defineId(ZombieSD.class, EntityDataSerializers.BOOLEAN); // TODO Check
}
