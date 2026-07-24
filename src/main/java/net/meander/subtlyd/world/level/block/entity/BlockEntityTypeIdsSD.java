package net.meander.subtlyd.world.level.block.entity;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * @see net.minecraft.world.level.block.entity.BlockEntityTypeIds
 */
public class BlockEntityTypeIdsSD {
    public static final ResourceKey<BlockEntityType<?>> POTION_CAULDRON = create("potion_cauldron");
    public static final ResourceKey<BlockEntityType<?>> STEW_CAULDRON = create("stew_cauldron");

    private static ResourceKey<BlockEntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, UtilSD.identifier(name));
    }
}
