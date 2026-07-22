package net.meander.subtlyd.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.zombie.Zombie;

public class ZombieSD {
    public static final EntityDataAccessor<Boolean> DATA_ID_ZOMBIE_LEADER = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
}
