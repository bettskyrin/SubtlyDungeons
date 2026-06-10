package net.meander.subtlyd.mixin.common.world.level.levelgen.structure.structures;

import net.meander.subtlyd.world.level.levelgen.structure.structures.OceanMonumentStructureSD;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentPieces;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(OceanMonumentPieces.MonumentBuilding.class)
public class MonumentBuildingMixin {
    @Shadow @Final private List<OceanMonumentPieces.OceanMonumentPiece> childPieces;

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void subtlyd$sinkAllPieces(RandomSource random, int west, int north, Direction direction, CallbackInfo ci) {
        int shiftDown = OceanMonumentStructureSD.NEW_DEPTH.get();

        if (shiftDown < 0) {
            StructurePiece piece = (StructurePiece) (Object) this;

            piece.getBoundingBox().move(0, shiftDown, 0);

            for (StructurePiece child : this.childPieces) {
                child.getBoundingBox().move(0, shiftDown, 0);
            }
        }
    }
}