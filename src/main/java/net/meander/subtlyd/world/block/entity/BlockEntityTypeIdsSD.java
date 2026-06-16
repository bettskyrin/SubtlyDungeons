package net.meander.subtlyd.world.block.entity;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityTypeIdsSD {
    public static final ResourceKey<BlockEntityType<?>> POTION_CAULDRON = create("potion_cauldron");

    private static ResourceKey<BlockEntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Util.identifier(name));
    }
}
