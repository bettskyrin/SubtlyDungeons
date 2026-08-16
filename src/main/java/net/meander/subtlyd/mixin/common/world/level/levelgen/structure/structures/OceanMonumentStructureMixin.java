package net.meander.subtlyd.mixin.common.world.level.levelgen.structure.structures;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OceanMonumentStructure.class)
public class OceanMonumentStructureMixin {
    private static final int CLASSIC_OCEAN_FLOOR_Y = 39;

    @Inject(method = "generatePieces", at = @At("TAIL"))
    private static void relocateMonument(StructurePiecesBuilder builder, Structure.GenerationContext context, CallbackInfo ci) {
        List<StructurePiece> pieces = builder.pieces;

        if (!pieces.isEmpty()) {
            StructurePiece topPiece = pieces.getFirst();
            int centerX = context.chunkPos().getMiddleBlockX();
            int centerZ = context.chunkPos().getMiddleBlockZ();
            int floorY = context.chunkGenerator().getBaseHeight(
                    centerX,
                    centerZ,
                    Heightmap.Types.OCEAN_FLOOR_WG,
                    context.heightAccessor(),
                    context.randomState()
            );
            int shiftDown = floorY - CLASSIC_OCEAN_FLOOR_Y;

            if (shiftDown != 0) {
                shiftMonumentPiece(topPiece, shiftDown);
            }
        }
    }

    @Inject(method = "regeneratePiecesAfterLoad", at = @At("RETURN"))
    private static void relocateAfterLoad(ChunkPos chunkPos, long seed, PiecesContainer savedPieces, CallbackInfoReturnable<PiecesContainer> cir) {
        if (!savedPieces.isEmpty()) {
            PiecesContainer newContainer = cir.getReturnValue();
            StructurePiece oldTopPiece = savedPieces.pieces().getFirst();
            StructurePiece newTopPiece = newContainer.pieces().getFirst();

            int shiftDown = oldTopPiece.getBoundingBox().minY() - CLASSIC_OCEAN_FLOOR_Y;

            if (shiftDown != 0) {
                shiftMonumentPiece(newTopPiece, shiftDown);
            }
        }
    }

    private static void shiftMonumentPiece(StructurePiece piece, int shiftDown) {
        piece.boundingBox = piece.getBoundingBox().moved(0, shiftDown, 0);

        if (piece instanceof OceanMonumentPieces.MonumentBuilding monument) {
            for (OceanMonumentPieces.OceanMonumentPiece child : monument.childPieces) {
                child.boundingBox = child.getBoundingBox().moved(0, shiftDown, 0);
            }
        }
    }
}