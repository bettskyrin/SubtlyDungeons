package com.kr1s1s.subtlyd.world.entity.monster;

import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ZombieSD extends Zombie {
    private static final Identifier LEADER_ZOMBIE_BONUS_ID = Identifier.withDefaultNamespace("leader_zombie_bonus");

    public ZombieSD(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public static void alterAttributes(Zombie zombie, float f) {
        if (zombie.getRandom().nextFloat() < (f * Zombie.ZOMBIE_LEADER_CHANCE)) {
            zombie.getEntityData().set(SynchedEntityDataSD.DATA_LEADER_ID, true);
            Objects.requireNonNull(zombie.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE))
                    .addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, zombie.getRandom().nextDouble() * 0.25 + 0.5, AttributeModifier.Operation.ADD_VALUE));
            Objects.requireNonNull(zombie.getAttribute(Attributes.MAX_HEALTH))
                    .addOrReplacePermanentModifier(
                            new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, (int) (zombie.getRandom().nextDouble() * 3.0 + 1.0), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    );
            zombie.setHealth((int) (20 + zombie.getMaxHealth() * f));
            zombie.setCanBreakDoors(true);
        }
    }

    public static boolean isLeader(Zombie zombie) {
        return zombie.getEntityData().get(SynchedEntityDataSD.DATA_LEADER_ID);
    }
}
