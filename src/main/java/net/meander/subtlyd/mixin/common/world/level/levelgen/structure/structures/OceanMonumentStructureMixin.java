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
        final int CLASSIC_OCEAN_FLOOR_Y = 39;
        int centerX = context.chunkPos().getMiddleBlockX();
        int centerZ = context.chunkPos().getMiddleBlockZ();
        int floorY = context.chunkGenerator().getBaseHeight(
                centerX,
                centerZ,
                Heightmap.Types.OCEAN_FLOOR_WG,
                context.heightAccessor(),
                context.randomState()
        );

        int shift = floorY - CLASSIC_OCEAN_FLOOR_Y;

        OceanMonumentStructureSD.NEW_DEPTH.set(shift);
    }

    @Inject(method = "generatePieces", at = @At("RETURN"))
    private static void cleanUpDepth(StructurePiecesBuilder builder, Structure.GenerationContext context, CallbackInfo ci) {
        OceanMonumentStructureSD.NEW_DEPTH.remove();
    }
}