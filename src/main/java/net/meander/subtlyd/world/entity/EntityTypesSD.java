package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.references.EntityTypeIdsSD;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;

/**
 * @see net.minecraft.world.entity.EntityTypes
 */
public class EntityTypesSD {
    public static final EntityType<Tent> TENT = EntityTypes.register(EntityTypeIdsSD.TENT, EntityType.Builder.of(Tent::new, MobCategory.MISC).noLootTable().sized(3.5F, 1.8F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).dontTrackDeltas());
    public static final EntityType<BlastFungus> BLAST_FUNGUS = EntityTypes.register(EntityTypeIdsSD.BLAST_FUNGUS, EntityType.Builder.<BlastFungus>of(BlastFungus::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
}
