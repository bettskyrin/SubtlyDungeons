package net.meander.subtlyd.mixin.common.entity;

import net.meander.subtlyd.util.data.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.ai.goal.SeekShadeGoal;
import net.meander.subtlyd.world.entity.ai.goal.SeekShelterGoal;
import net.meander.subtlyd.world.entity.ai.goal.SeekWarmthGoal;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    /**
     * Adds the goal of finding shelther from the rain and cold. Only warm and temperate animals seek warmth.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addEnvironmentGoals(EntityType<?> type, Level level, CallbackInfo ci) {
        if (((Object) this) instanceof PathfinderMob mob) {
            if (mob.is(EntityTypeTagsSD.CAN_SEEK_WARMTH)) {
                mob.goalSelector.addGoal(4, new SeekWarmthGoal(mob, 1.0D, 16));
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_SHELTER)) {
                mob.goalSelector.addGoal(6, new SeekShelterGoal(mob, 1.0D));
            }

            if (mob.is(EntityTypeTagsSD.CAN_SEEK_SHADE)) {
                mob.goalSelector.addGoal(4, new SeekShadeGoal(mob, 1.0D));
            }
        }
    }

    /**
     * Allows pets to spring with their owner.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void allowPetSprinting(CallbackInfo ci) {
        if (((Object) this) instanceof TamableAnimal pet) {
            if (!pet.level().isClientSide() && pet.isTame() && !pet.isInSittingPose()) {
                LivingEntity owner = pet.getOwner();

                if (owner != null && owner.isSprinting() && pet.getNavigation().isInProgress()) {
                    pet.setSprinting(true);
                } else if (pet.isSprinting()) {
                    pet.setSprinting(false);
                }
            }
        }
    }
}