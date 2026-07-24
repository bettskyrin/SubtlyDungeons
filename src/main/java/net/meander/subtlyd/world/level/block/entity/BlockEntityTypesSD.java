package net.meander.subtlyd.world.level.block.entity;

import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;

/**
 * @see BlockEntityTypes
 */
public class BlockEntityTypesSD {
    public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON = BlockEntityTypes.register(BlockEntityTypeIdsSD.POTION_CAULDRON, PotionCauldronBlockEntity::new, BlocksSD.POTION_CAULDRON);
    public static final BlockEntityType<StewCauldronBlockEntity> STEW_CAULDRON = BlockEntityTypes.register(BlockEntityTypeIdsSD.STEW_CAULDRON, StewCauldronBlockEntity::new, BlocksSD.STEW_CAULDRON);

    public static void registration() {}
}
