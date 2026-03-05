package net.meander.subtlyd.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.level.biome.BiomeSD;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SeekShadeGoal extends Goal { // FIXME
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double shadeX;
    protected double shadeY;
    protected double shadeZ;

    public SeekShadeGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Sets the target shade position.
     * @return True if a shade position has been set.
     */
    protected boolean setShadePos() {
        Vec3 foundShadePos = findShadePos();

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
     * @return A Vec3 shelter position or null, if valid shade is not found.
     */
    private Vec3 findShadePos() {
        RandomSource random = mob.getRandom();
        int YRADIUS = 4;

        for (int searchRadius = 4; searchRadius <= 16; searchRadius += 4) {
            for (int j = 0; j < searchRadius * 6; j++) {
                BlockPos randPos = mob.blockPosition().offset(random.nextInt((searchRadius * 2) + 1) - searchRadius, random.nextInt((YRADIUS * 2) + 1) - YRADIUS, random.nextInt((searchRadius * 2) + 1) - searchRadius);

                if (BiomeSD.getTemperatureAsVariantType(mob.level(), randPos) != TemperatureVariants.WARM && mob.level().getBlockState(randPos).isPathfindable(PathComputationType.LAND) && !mob.level().canSeeSky(randPos)) {
                    return Vec3.atBottomCenterOf(randPos);
                }
            }
        }
        return null;
    }


    @Override
    public boolean canUse() {
        Identifier variant = EntityTypeSD.getTemperatureVariantType(mob);

        if (variant == TemperatureVariants.WARM || !mob.level().canSeeSky(mob.blockPosition())) {
            return false;
        }
        return setShadePos();
    }

    @Override
    public boolean canContinueToUse() {
        return BiomeSD.getTemperatureAsVariantType(mob.level(), mob.blockPosition()) == TemperatureVariants.WARM;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(shadeX,  shadeY, shadeZ, speedModifier);
    }

    @Override
    public void tick() {
        if (mob.getNavigation().isDone() && BiomeSD.getTemperatureAsVariantType(mob.level(), mob.blockPosition()) == TemperatureVariants.WARM && setShadePos()) {
            start();
        }
    }
}
