package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.entity.TamableAnimalSD;
import net.meander.subtlyd.world.level.biome.BiomeSD;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SeekShadeGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double shadeX;
    protected double shadeY;
    protected double shadeZ;

    public SeekShadeGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Sets the target "cool spot" position.
     * @return True if a cool position has been set.
     */
    protected boolean setCoolPos() {
        Vec3 foundShadePos = findCoolPos();

        if (foundShadePos != null) {
            shadeX = foundShadePos.x;
            shadeY = foundShadePos.y;
            shadeZ = foundShadePos.z;

            return true;
        }
        return false;
    }

    /**
     * Searches for valid shade.
     * @return A Vec3 cool position or null, if valid shade is not found.
     */
    private Vec3 findCoolPos() {
        RandomSource random = mob.getRandom();
        int YRADIUS = 4;

        for (int searchRadius = 4; searchRadius <= 16; searchRadius += 4) {
            for (int j = 0; j < searchRadius * 6; j++) {
                BlockPos randPos = mob.blockPosition().offset(random.nextInt((searchRadius * 2) + 1) - searchRadius, random.nextInt((YRADIUS * 2) + 1) - YRADIUS, random.nextInt((searchRadius * 2) + 1) - searchRadius);

                if (mob.level().getBlockState(randPos).isPathfindable(PathComputationType.LAND) && !mob.level().canSeeSky(randPos)) {
                    return Vec3.atBottomCenterOf(randPos);
                }
            }
        }
        return null;
    }


    @Override
    public boolean canUse() {
        Identifier variant = EntityTypeSD.getTemperatureVariantType(mob);
        Identifier biomeTemp = BiomeSD.getBiomeAsTemperatureVariant(mob.level(), mob.blockPosition());

        if (biomeTemp != TemperatureVariants.WARM || variant == TemperatureVariants.WARM || !mob.level().canSeeSky(mob.blockPosition()) ||
                mob.level().dimension() == Level.NETHER || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        }
        return setCoolPos();
    }

    @Override
    public boolean canContinueToUse() {
        Identifier biomeTemp = BiomeSD.getBiomeAsTemperatureVariant(mob.level(), mob.blockPosition());
        if (biomeTemp != TemperatureVariants.WARM || !mob.level().canSeeSky(mob.blockPosition()) || mob.level().dimension() == Level.NETHER || TamableAnimalSD.shouldFollowOwner(mob) || mob.getTarget() != null) {
            return false;
        }
        return !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(shadeX,  shadeY, shadeZ, speedModifier);
    }

    @Override
    public void tick() {
        if (mob.getNavigation().isDone() && BiomeSD.getBiomeAsTemperatureVariant(mob.level(), mob.blockPosition()) == TemperatureVariants.WARM && setCoolPos()) {
            start();
        }
    }
}
