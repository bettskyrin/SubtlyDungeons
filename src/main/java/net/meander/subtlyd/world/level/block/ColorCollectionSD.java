package net.meander.subtlyd.world.level.block;

import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ColorCollection;

import java.util.function.BiFunction;

public class ColorCollectionSD {
    public static ColorCollection<Item> registerEntityItem(final String id, final BiFunction<String, DyeColor, Item> itemFactory) {
        return ColorCollection.make((color) -> itemFactory.apply(color.getName() + "_" + id, color));
    }

    public static ColorCollection<EntityType<? extends Entity>> registerTentEntity() {
        return ColorCollection.make(color -> {
            EntityType.Builder<TentEntity> builder = EntityType.Builder.of(EntityTypeSD.tentFactory(() -> ItemsSD.TENT.pick(color)), MobCategory.MISC)
                    .sized(3.5F, 1.8F)
                    .noLootTable()
                    .clientTrackingRange(10);

            return EntityTypeSD.register(TentEntity.getResourceKey(color).identifier(), builder);
        });
    }
}