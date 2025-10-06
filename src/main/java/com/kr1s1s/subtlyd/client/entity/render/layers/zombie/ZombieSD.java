package com.kr1s1s.subtlyd.client.entity.render.layers.zombie;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class ZombieSD extends Zombie {
    public static final ResourceLocation LEADER_ZOMBIE_BONUS_ID = ResourceLocation.withDefaultNamespace("leader_zombie_bonus");
    public static final float ZOMBIE_LEADER_CHANCE = 0.05F;

    public ZombieSD(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public static void alterAttributes(Zombie zombie, float f) {
        if ((zombie.getRandom().nextFloat() < f * ZOMBIE_LEADER_CHANCE)) {
            zombie.getEntityData().set(SubtlyDungeons.DATA_LEADER_ID, true);
            zombie.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                    .addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, zombie.getRandom().nextDouble() * 0.25 + 0.5, AttributeModifier.Operation.ADD_VALUE));
            zombie.getAttribute(Attributes.MAX_HEALTH)
                    .addOrReplacePermanentModifier(
                            new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, (int) (zombie.getRandom().nextDouble() * 3.0 + 1.0), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    );
            zombie.setHealth((int) (20 + zombie.getMaxHealth() * f)); // Increase Zombie Leader health
            zombie.setCanBreakDoors(true);
        }
    }

    public static boolean isLeader(Zombie zombie) {
        return zombie.getEntityData().get(SubtlyDungeons.DATA_LEADER_ID);
    }
}
