package net.meander.subtlyd.references;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.ColorCollection;

public class EntityTypeIdsSD {
    public static final ColorCollection<ResourceKey<EntityType<?>>> TENT = ColorCollection.prefixWithColor(ColorCollection.create("tent_entity")).map(EntityTypeIdsSD::create);

    private static ResourceKey<EntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Util.identifier(name));
    }
}
