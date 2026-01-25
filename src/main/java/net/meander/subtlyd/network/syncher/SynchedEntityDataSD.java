package net.meander.subtlyd.network.syncher;

import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

public class SynchedEntityDataSD {
    public static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.FLOAT);

    public static void createEntityData() {}
}
