package net.meander.subtlyd.mixin.common.world.level.levelgen.structure.structures;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.block.entity.PotionCauldronBlockEntity;
import net.meander.subtlyd.world.level.storage.loot.LootTablesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
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
            ServerLevel serverLevel = level.getLevel();
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(LootTablesSD.SWAMP_HUT_CAULDRON);
            LootParams lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(cauldronPos))
                    .create(LootContextParamSets.CHEST);
            ObjectArrayList<ItemStack> generatedLoot = lootTable.getRandomItems(lootParams, random.nextLong());
            Holder<Potion> potion = Potions.WATER;
            Item potionType = Items.POTION;

            if (!generatedLoot.isEmpty()) {
                ItemStack itemStack = generatedLoot.getFirst();
                PotionContents potionContents = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

                if (potionContents.potion().isPresent()) {
                    potion = potionContents.potion().get();
                }
                potionType = itemStack.getItem();
            }

            level.setBlock(cauldronPos, BlocksSD.POTION_CAULDRON.defaultBlockState().setValue(PotionCauldronBlock.LEVEL, levels), 2);

            if (level.getBlockEntity(cauldronPos) instanceof PotionCauldronBlockEntity cauldron) {
                cauldron.setPotion(potion);
                cauldron.setPotionType(potionType);
            }
        }
    }
}