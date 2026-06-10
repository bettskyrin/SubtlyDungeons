package net.meander.subtlyd.mixin.common.world.level.levelgen.structure.structures;

import net.meander.subtlyd.world.level.levelgen.structure.structures.OceanMonumentStructureSD;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(OceanMonumentStructure.class)
public class OceanMonumentStructureMixin {
    /**
     * Moves ocean monuments to the ocean floor.
     */
    @Inject(method = "generatePieces", at = @At("HEAD"))
    private static void relocateMonument(StructurePiecesBuilder builder, Structure.GenerationContext context, CallbackInfo ci) {
        final int classicFloorY = 39;
        int minFloorY = Integer.MAX_VALUE;
        int startX = context.chunkPos().getMinBlockX();
        int startZ = context.chunkPos().getMinBlockZ();

        for (int x = 0; x <= 58; x += 14) {
            for (int z = 0; z <= 58; z += 14) {
                int floorY = context.chunkGenerator().getBaseHeight(
                        startX + x,
                        startZ + z,
                        Heightmap.Types.OCEAN_FLOOR_WG,
                        context.heightAccessor(),
                        context.randomState()
                );
                minFloorY = Math.min(minFloorY, floorY);
            }
        }
        int shift = minFloorY - classicFloorY;

        OceanMonumentStructureSD.NEW_DEPTH.set(Math.min(shift, 0));
    }

    @Inject(method = "generatePieces", at = @At("RETURN"))
    private static void cleanUpDepth(StructurePiecesBuilder builder, Structure.GenerationContext context, CallbackInfo ci) {
        OceanMonumentStructureSD.NEW_DEPTH.remove();
    }
}