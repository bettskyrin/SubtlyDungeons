package net.meander.subtlyd.world.block.entity;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class BlockEntityTypesSD {
    public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON = register(BlockEntityTypeIdsSD.POTION_CAULDRON, PotionCauldronBlockEntity::new, BlocksSD.POTION_CAULDRON);

    public static void bootstrap() {}

    private static <T extends BlockEntity> BlockEntityType<T> register(final ResourceKey<BlockEntityType<?>> key, final BlockEntityType.BlockEntitySupplier<? extends T> factory, final Block... validBlocks) {
        Identifier id = key.identifier();

        if (validBlocks.length == 0) {
            Util.LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", id);
        }
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, new BlockEntityType<>(factory, Set.of(validBlocks)));
    }
}
