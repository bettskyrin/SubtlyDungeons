package net.meander.subtlyd.mixin.common.world.level.levelgen.structure.structures;

import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.block.entity.PotionCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwampHutPiece.class)
public class SwampHutPieceMixin {
    @Inject(method = "postProcess", at = @At("TAIL"))
    private void fillWitchHutCauldron(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos, CallbackInfo ci) {
        SwampHutPiece piece = (SwampHutPiece) (Object) this;
        BlockPos cauldronPos = new BlockPos(piece.getWorldX(4, 6), piece.getWorldY(2), piece.getWorldZ(4, 6));

        if (chunkBB.isInside(cauldronPos) && level.getBlockState(cauldronPos).is(Blocks.CAULDRON)) {
            int levels = (random.nextInt(3) + 1) * 2;
            int roll = random.nextInt(100);
            Holder<Potion> selectedPotion = getPotion(roll);

            level.setBlock(cauldronPos, BlocksSD.POTION_CAULDRON.defaultBlockState().setValue(PotionCauldronBlock.POTION_LEVEL, levels), 2);

            if (level.getBlockEntity(cauldronPos) instanceof PotionCauldronBlockEntity blockEntity) {
                blockEntity.setPotion(selectedPotion);
                blockEntity.setPotionType("minecraft:potion");
            }
        }
    }

    private static Holder<Potion> getPotion(int roll) {
        Holder<Potion> selectedPotion;

        if (roll < 25) {
            selectedPotion = Potions.HEALING;
        } else if (roll < 50) {
            selectedPotion = Potions.POISON;
        } else if (roll < 65) {
            selectedPotion = Potions.SWIFTNESS;
        } else if (roll < 75) {
            selectedPotion = Potions.SLOWNESS;
        } else if (roll < 85) {
            selectedPotion = Potions.WEAKNESS;
        } else if (roll < 95) {
            selectedPotion = Potions.WATER_BREATHING;
        } else {
            selectedPotion = Potions.FIRE_RESISTANCE;
        }
        return selectedPotion;
    }
}