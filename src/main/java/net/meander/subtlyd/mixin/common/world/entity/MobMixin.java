package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.MobSD;
import net.meander.subtlyd.world.entity.ai.goal.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Mob.class)
public abstract class MobMixin implements MobSD {
    @Shadow public abstract GoalSelector getGoalSelector();
    private long huntingCooldown = 0;

    @Override
    public long getHuntingCooldownTicks() {
        return huntingCooldown;
    }

    @Override
    public void setHuntingCooldownTicks(long time) {
        huntingCooldown = time;
    }

    private int getGoalPriority(PathfinderMob mob, Class<? extends Goal> goalClass) {
        Optional<Integer> priority = Optional.empty();

        for (WrappedGoal wrappedGoal : mob.getGoalSelector().getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();

            if (goal.getClass() == goalClass) {
                priority = Optional.of(Mth.clamp(priority.orElse(64) - 1, 0, wrappedGoal.getPriority()));
            }
        }

        return priority.orElse(4);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addEnvironmentGoals(EntityType<?> type, Level level, CallbackInfo ci) {
        if (((Object) this) instanceof PathfinderMob mob) {
            int panicPrio = getGoalPriority(mob, PanicGoal.class);
            int strollPrio = getGoalPriority(mob, RandomStrollGoal.class);

            if (mob.is(EntityTypeTagsSD.PANICS_WITH_FLOCK)) {
                getGoalSelector().addGoal(Math.max(panicPrio, 0), new PanicWithFlockGoal(mob, mob.getPanicSpeed()));
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_WARMTH)) {
                getGoalSelector().addGoal(Math.max(strollPrio - 1, 0), new MoveToWarmthGoal(mob, 1.0));
                getGoalSelector().addGoal(strollPrio, new ShelteredRandomStrollGoal(mob, 1.0));
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_SHELTER)) {
                getGoalSelector().addGoal(Math.max(strollPrio - 1, 0) , new MoveToShelterGoal(mob, 1.0));
                getGoalSelector().addGoal(strollPrio, new ShelteredRandomStrollGoal(mob, 1.0));
            }

            if (mob.is(EntityTypeTagsSD.SEEKS_SHADE)) {
                getGoalSelector().addGoal(Math.max(strollPrio - 1, 0), new MoveToShadeGoal(mob, 1.0));
                getGoalSelector().addGoal(strollPrio, new ShelteredRandomStrollGoal(mob, 1.0));
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void allowPetSprinting(CallbackInfo ci) {
        if (((Object) this) instanceof TamableAnimal tamable) {
            if (tamable.level() instanceof ServerLevel && tamable.isTame() && !tamable.isInSittingPose()) {
                LivingEntity owner = tamable.getOwner();

                if (owner != null && owner.isSprinting() && tamable.getNavigation().isInProgress()) {
                    tamable.setSprinting(true);
                } else if (tamable.isSprinting()) {
                    tamable.setSprinting(false);
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

    @Inject(method = "createNavigation", at = @At("HEAD"), cancellable = true)
    private void setScansorialEntityNavigation(Level level, CallbackInfoReturnable<PathNavigation> cir) {
        if (Minecraft.getInstance().options.fancyEntities().get()) {
            Mob mob = (Mob) (Object) this;

            if (mob instanceof Silverfish || mob instanceof Endermite) {
                cir.setReturnValue(new WallClimberNavigation(mob, level));
            }
        }
    }
}