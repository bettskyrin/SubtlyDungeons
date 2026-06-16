package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.data.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.MobSD;
import net.meander.subtlyd.world.entity.ai.goal.SeekShadeGoal;
import net.meander.subtlyd.world.entity.ai.goal.SeekShelterGoal;
import net.meander.subtlyd.world.entity.ai.goal.SeekWarmthGoal;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin implements MobSD {
    @Unique private long huntingCooldown = 0;

    @Override
    public long subtlyd$getHuntingCooldownTicks() {
        return huntingCooldown;
    }

    @Override
    public void subtlyd$setHuntingCooldown(long time) {
        huntingCooldown = time;
    }

    /**
     * Adds the goal of finding shelter from the rain and cold. Only warm and temperate animals seek warmth.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void addEnvironmentGoals(EntityType<?> type, Level level, CallbackInfo ci) {
        if (((Object) this) instanceof PathfinderMob mob) {
            if (mob.is(EntityTypeTagsSD.CAN_SEEK_WARMTH)) {
                mob.goalSelector.addGoal(4, new SeekWarmthGoal(mob, 1.0D, 16));
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_SHELTER)) {
                mob.goalSelector.addGoal(5, new SeekShelterGoal(mob, 1.0D));
            }

            if (mob.is(EntityTypeTagsSD.CAN_SEEK_SHADE)) {
                mob.goalSelector.addGoal(4, new SeekShadeGoal(mob, 1.0D));
            }
        }
    }

    /**
     * Allows pets to sprint with their owner.
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

    @Inject(method = "ate", at = @At("TAIL"))
    private void healFromGrazing(CallbackInfo ci) {
        if (((Object) this) instanceof Animal animal) {
            if (animal.getHealth() < animal.getMaxHealth()) {
                animal.heal(2.0F);
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveHuntingCooldown(ValueOutput output, CallbackInfo ci) {
        if (huntingCooldown > 0) {
            output.putLong("huntingCooldown", huntingCooldown);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadHuntingCooldown(ValueInput input, CallbackInfo ci) {
        input.getLong("huntingCooldown").ifPresent(cooldown -> huntingCooldown = cooldown);
    }


    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void setWitherSkeletonTeam(LivingEntity target, CallbackInfo ci) {
        if (((LivingEntity) (Object) this).is(EntityTypeTags.WITHER_FRIENDS)) {
            if (target instanceof WitherBoss) {
                ci.cancel();
            }
        }
    }
}