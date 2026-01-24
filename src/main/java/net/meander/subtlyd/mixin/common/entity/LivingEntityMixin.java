package net.meander.subtlyd.mixin.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private final LivingEntity livingEntity = (LivingEntity) (Object) this;

    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    /**
     * @param mob The pathfinding mob to test.
     * @return The speed multiplier for that animal type when it panics.
     */
    private double getPanicSpeed(PathfinderMob mob) {
        for (WrappedGoal wrappedGoal : mob.goalSelector.getAvailableGoals()) {
            if (wrappedGoal.getGoal() instanceof PanicGoal panicGoal) {
                return panicGoal.speedModifier;
            }
        }
        return 1.25D;
    }

    /**
     * @param animal The animal to test.
     * @return Whether an animal should be considered a herd-type animal.
     */
    private boolean isHerdAnimal(Object animal) {
        return !((animal instanceof NeutralMob)
                || (animal instanceof Enemy)
                || (animal instanceof ZombieHorse)
                || (animal instanceof CamelHusk))
                && animal instanceof Animal;
    }

    /**
     * Runs when an entity takes damage.
     */
    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void panicFromDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!livingEntity.level().isClientSide() && cir.getReturnValue() && isHerdAnimal(livingEntity)) {
            if (source.getEntity() instanceof LivingEntity attacker) {
                shareHerdPanic(livingEntity, attacker);
            }
        }
    }

    /**
     * Broadcasts that an animal was attacked, and that any nearby animals should flee.
     * @param victim The animal being attacked.
     * @param attacker The attacking entity.
     */
    private void shareHerdPanic(LivingEntity victim, LivingEntity attacker) {
        double RADIUS = 16.0F;
        AABB searchAabb = victim.getBoundingBox().inflate(RADIUS);
        List<? extends LivingEntity> herd = victim.level().getEntitiesOfClass(Animal.class, searchAabb);

        for (LivingEntity animal : herd) {
            if (isHerdAnimal(animal) && animal != victim && !animal.isAlliedTo(attacker)) {
                animal.hurtDuration = 50;
                animal.hurtTime = animal.hurtDuration;

                animal.getBrain().setActiveActivityIfPossible(Activity.PANIC);

                if (animal instanceof PathfinderMob mob) {
                    if (animal instanceof Armadillo armadillo) {
                        armadillo.rollUp();
                        armadillo.getBrain().setMemory(MemoryModuleType.DANGER_DETECTED_RECENTLY, true);
                    } else {
                        Vec3 pos = new Vec3(animal.getX(), animal.getY(), animal.getZ());

                        while (pos != null && (pos.x == mob.getX() && pos.z == mob.getZ())) {
                            pos = DefaultRandomPos.getPosAway(mob, 16, 4, attacker.position());
                        }
                        if (pos != null) {
                            mob.getNavigation().moveTo(pos.x, pos.y, pos.z, getPanicSpeed(mob));
                        }
                    }
                }
            }
        }
    }
}