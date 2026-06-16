package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.references.EntityTypeIdsSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.ColorCollection;

public class EntityTypesSD {
    public static final ColorCollection<EntityType<TentEntity>> TENT = ColorCollection.zipMap(ColorCollection.VALUES,
            EntityTypeIdsSD.TENT,
            (color, key) -> Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    key,
                    EntityType.Builder.of(EntityTypeSD.tentFactory(() -> ItemsSD.TENT.pick(color)), MobCategory.MISC)
                            .sized(3.5F, 1.8F)
                            .noLootTable()
                            .clientTrackingRange(10)
                            .build(key))
    );
    public static final EntityType<BlastFungusEntity> BLAST_FUNGUS = EntityTypeSD.register(ItemsSD.BLAST_FUNGUS,
            EntityType.Builder.<BlastFungusEntity>of(BlastFungusEntity::new, MobCategory.MISC)
                    .noLootTable().sized(0.25F, 0.25F)
                    .clientTrackingRange(4).
                    updateInterval(10)
    );
}
