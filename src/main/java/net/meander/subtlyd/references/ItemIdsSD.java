package net.meander.subtlyd.references;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ColorCollection;

/**
 * @see net.minecraft.references.ItemIds
 */
public class ItemIdsSD {
    public static final ResourceKey<Item> APPLE_PIE = create("apple_pie");
    public static final ResourceKey<Item> CALAMARI = create("calamari");
    public static final ResourceKey<Item> COOKED_CALAMARI = create("cooked_calamari");
    public static final ResourceKey<Item> POTTAGE = create("pottage");
    public static final ColorCollection<ResourceKey<Item>> TENT = createSimpleColored("tent");
    public static final ResourceKey<Item> BLAST_FUNGUS = create("blast_fungus");
    public static final ResourceKey<Item> COVEN_ELIXIR = create("coven_elixir");
    public static final ResourceKey<Item> LIGHT_STEW = create("light_stew");
    public static final ResourceKey<Item> WOODEN_DAGGER = create("wooden_dagger");
    public static final ResourceKey<Item> STONE_DAGGER = create("stone_dagger");
    public static final ResourceKey<Item> COPPER_DAGGER = create("copper_dagger");
    public static final ResourceKey<Item> IRON_DAGGER = create("iron_dagger");
    public static final ResourceKey<Item> GOLDEN_DAGGER = create("golden_dagger");
    public static final ResourceKey<Item> DIAMOND_DAGGER = create("diamond_dagger");
    public static final ResourceKey<Item> NETHERITE_DAGGER = create("netherite_dagger");
    public static final ResourceKey<Item> QUIVER = create("quiver");
    public static final ColorCollection<ResourceKey<Item>> DYED_QUIVER = createSimpleColored("dyed_quiver");
    public static final ResourceKey<Item> HEAVY_SHIELD = create("heavy_shield");

    private static ResourceKey<Item> create(final String name) {
        return ResourceKey.create(Registries.ITEM, UtilSD.identifier(name));
    }

    private static ColorCollection<ResourceKey<Item>> createSimpleColored(final String baseName) {
        return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(ItemIdsSD::create);
    }
}
