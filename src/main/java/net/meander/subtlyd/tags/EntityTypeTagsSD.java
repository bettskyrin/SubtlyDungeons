package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

/**
 * @see net.minecraft.tags.EntityTypeTags
 */
public class EntityTypeTagsSD {
    public static final TagKey<EntityType<?>> CAN_BE_SCARED = create("can_be_scared");
    public static final TagKey<EntityType<?>> SEEKS_SHELTER = create("seeks_shelter");
    public static final TagKey<EntityType<?>> SEEKS_WARMTH = create("seek_warmth");
    public static final TagKey<EntityType<?>> SEEKS_SHADE = create("seek_shade");
    public static final TagKey<EntityType<?>> PREDATOR = create("predator");
    public static final TagKey<EntityType<?>> NOCTURNAL = create("nocturnal");
    public static final TagKey<EntityType<?>> FEAST_OR_FAMINE_HUNTER = create("feast_or_famine_hunter");
    public static final TagKey<EntityType<?>> SCANSORIAL = create("scansorial");

    private static TagKey<EntityType<?>> create(String string) {
        return TagKey.create(Registries.ENTITY_TYPE, UtilSD.identifier(string));
    }
}
