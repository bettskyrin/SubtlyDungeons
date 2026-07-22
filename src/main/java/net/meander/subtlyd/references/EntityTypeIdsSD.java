package net.meander.subtlyd.references;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

/**
 * @see net.minecraft.world.entity.EntityTypeIds
 */
public class EntityTypeIdsSD {
    public static final ResourceKey<EntityType<?>> TENT = create("tent");
    public static final ResourceKey<EntityType<?>> BLAST_FUNGUS = create("blast_fungus");

    private static ResourceKey<EntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, UtilSD.identifier(name));
    }
}
