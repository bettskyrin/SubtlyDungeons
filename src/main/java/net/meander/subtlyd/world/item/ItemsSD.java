package net.meander.subtlyd.world.item;

import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.references.ItemIdsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.meander.subtlyd.world.entity.ai.attributes.AttributesSD;
import net.meander.subtlyd.world.food.FoodsSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.meander.subtlyd.world.item.component.ConsumablesSD;
import net.meander.subtlyd.world.level.block.entity.FuelValuesSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.BlockItemId;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * @see Items
 */
public class ItemsSD {
    public static final Item APPLE_PIE = Items.registerItem(ItemIdsSD.APPLE_PIE, Item::new, new Item.Properties()
            .food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = Items.registerItem(ItemIdsSD.CALAMARI, Item::new, new Item.Properties()
            .food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = Items.registerItem(ItemIdsSD.COOKED_CALAMARI, Item::new, new Item.Properties()
            .food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = Items.registerItem(ItemIdsSD.POTTAGE, Item::new, new Item.Properties()
            .food(FoodsSD.POTTAGE)
            .stacksTo(1));
    public static final ColorCollection<Item> TENT = ColorCollection.registerItems(ItemIdsSD.TENT, (id, color) -> Items.registerItem(id, p -> new TentItem(EntityTypesSD.TENT.pick(color), p), new Item.Properties().stacksTo(1)));
    public static final Item UNLIT_CAMPFIRE = registerBlockSD(BlockItemIdsSD.UNLIT_CAMPFIRE, Blocks.CAMPFIRE, (p -> p
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))));
    public static final Item UNLIT_SOUL_CAMPFIRE = registerBlockSD(BlockItemIdsSD.UNLIT_SOUL_CAMPFIRE, Blocks.SOUL_CAMPFIRE, (p -> p
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))));
    public static final Item SNOW_BRICKS = Items.registerBlock(BlockItemIdsSD.SNOW_BRICKS, BlocksSD.SNOW_BRICKS);
    public static final Item SNOW_BRICK_STAIRS = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_STAIRS, BlocksSD.SNOW_BRICK_STAIRS);
    public static final Item SNOW_BRICK_SLAB = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_SLAB, BlocksSD.SNOW_BRICK_SLAB);
    public static final Item SNOW_BRICK_WALL = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_WALL, BlocksSD.SNOW_BRICK_WALL);
    public static final Item CHARCOAL_BLOCK = Items.registerBlock(BlockItemIdsSD.CHARCOAL_BLOCK, BlocksSD.CHARCOAL_BLOCK);
    public static final Item IRON_GRATE = Items.registerBlock(BlockItemIdsSD.IRON_GRATE, BlocksSD.IRON_GRATE);
    public static final Item CHISELED_POLISHED_DRIPSTONE = Items.registerBlock(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE, BlocksSD.CHISELED_POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE, BlocksSD.POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE_SLAB = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB, BlocksSD.POLISHED_DRIPSTONE_SLAB);
    public static final Item POLISHED_DRIPSTONE_STAIRS = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS, BlocksSD.POLISHED_DRIPSTONE_STAIRS);
    public static final Item POLISHED_DRIPSTONE_WALL = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL, BlocksSD.POLISHED_DRIPSTONE_WALL);
    public static final Item STONE_PILLAR = Items.registerBlock(BlockItemIdsSD.STONE_PILLAR, BlocksSD.STONE_PILLAR);
    public static final Item STONE_TILES = Items.registerBlock(BlockItemIdsSD.STONE_TILES, BlocksSD.STONE_TILES);
    public static final Item STONE_TILE_STAIRS = Items.registerBlock(BlockItemIdsSD.STONE_TILE_STAIRS, BlocksSD.STONE_TILE_STAIRS);
    public static final Item STONE_TILE_SLAB = Items.registerBlock(BlockItemIdsSD.STONE_TILE_SLAB, BlocksSD.STONE_TILE_SLAB);
    public static final Item STONE_TILE_WALL = Items.registerBlock(BlockItemIdsSD.STONE_TILE_WALL, BlocksSD.STONE_TILE_WALL);
    public static final Item REEDS = Items.registerBlock(BlockItemIdsSD.REEDS, BlocksSD.REEDS);
    public static final Item WARPED_OVERHANG = Items.registerBlock(BlockItemIdsSD.WARPED_OVERHANG, BlocksSD.WARPED_OVERHANG);
    public static final Item BLAST_FUNGUS = Items.registerItem(ItemIdsSD.BLAST_FUNGUS, BlastFungusItem::new, new Item.Properties()
            .stacksTo(16));
    public static final Item BASALT_SLAB = Items.registerBlock(BlockItemIdsSD.BASALT_SLAB, BlocksSD.BASALT_SLAB);
    public static final Item COVEN_ELIXIR = Items.registerItem(ItemIdsSD.COVEN_ELIXIR, Item::new, new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.CONSUMABLE, ConsumablesSD.COVEN_ELIXIR)
            .usingConvertsTo(Items.GLASS_BOTTLE));
    public static final Item SOUL_JACK_O_LANTERN = Items.registerBlock(BlockItemIdsSD.SOUL_JACK_O_LANTERN, BlocksSD.SOUL_JACK_O_LANTERN);
    public static final Item PERSE_WILDFLOWERS = Items.registerBlock(BlockItemIdsSD.PERSE_WILDFLOWERS, BlocksSD.PERSE_WILDFLOWERS);
    public static final Item LIGHT_STEW = Items.registerItem(ItemIdsSD.LIGHT_STEW, Item::new, new Item.Properties().food(FoodsSD.LIGHT_STEW).stacksTo(1));
    public static final Item WOODEN_DAGGER = Items.registerItem(ItemIdsSD.WOODEN_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.WOOD, 1.0F, -2.3F, 4.0F));
    public static final Item STONE_DAGGER = Items.registerItem(ItemIdsSD.STONE_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.STONE, 1.0F, -2.3F, 4.0F));
    public static final Item COPPER_DAGGER = Items.registerItem(ItemIdsSD.COPPER_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.COPPER, 1.0F, -2.3F, 4.0F));
    public static final Item IRON_DAGGER = Items.registerItem(ItemIdsSD.IRON_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.IRON, 1.0F, -2.3F, 4.0F));
    public static final Item GOLDEN_DAGGER = Items.registerItem(ItemIdsSD.GOLDEN_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.GOLD, 1.0F, -2.3F, 4.0F));
    public static final Item DIAMOND_DAGGER = Items.registerItem(ItemIdsSD.DIAMOND_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.DIAMOND, 1.0F, -2.3F, 4.0F));
    public static final Item NETHERITE_DAGGER = Items.registerItem(ItemIdsSD.NETHERITE_DAGGER, Item::new, new ItemSD.PropertiesSD().dagger(ToolMaterial.NETHERITE, 1.0F, -2.3F, 4.0F).rarity(Rarity.UNCOMMON));
    public static final Item QUIVER = Items.registerItem(ItemIdsSD.QUIVER, QuiverItem::new, new Item.Properties().stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.LEGS).setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER).build()));
    public static final ColorCollection<Item> DYED_QUIVER = ColorCollection.registerItems(ItemIdsSD.DYED_QUIVER, (id, _) -> Items.registerItem(id, QuiverItem::new, new Item.Properties().stacksTo(1).component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.LEGS).setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER).build())));
    public static final Item OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.OAK_WOOD_SLAB, BlocksSD.OAK_WOOD_SLAB);
    public static final Item SPRUCE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.SPRUCE_WOOD_SLAB, BlocksSD.SPRUCE_WOOD_SLAB);
    public static final Item BIRCH_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.BIRCH_WOOD_SLAB, BlocksSD.BIRCH_WOOD_SLAB);
    public static final Item JUNGLE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.JUNGLE_WOOD_SLAB, BlocksSD.JUNGLE_WOOD_SLAB);
    public static final Item ACACIA_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.ACACIA_WOOD_SLAB, BlocksSD.ACACIA_WOOD_SLAB);
    public static final Item DARK_OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.DARK_OAK_WOOD_SLAB, BlocksSD.DARK_OAK_WOOD_SLAB);
    public static final Item MANGROVE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.MANGROVE_WOOD_SLAB, BlocksSD.MANGROVE_WOOD_SLAB);
    public static final Item POPLAR_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.POPLAR_WOOD_SLAB, BlocksSD.POPLAR_WOOD_SLAB);
    public static final Item CHERRY_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.CHERRY_WOOD_SLAB, BlocksSD.CHERRY_WOOD_SLAB);
    public static final Item PALE_OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.PALE_OAK_WOOD_SLAB, BlocksSD.PALE_OAK_WOOD_SLAB);
    public static final Item CRIMSON_HYPHAE_SLAB = Items.registerBlock(BlockItemIdsSD.CRIMSON_HYPHAE_SLAB, BlocksSD.CRIMSON_HYPHAE_SLAB);
    public static final Item WARPED_HYPHAE_SLAB = Items.registerBlock(BlockItemIdsSD.WARPED_HYPHAE_SLAB, BlocksSD.WARPED_HYPHAE_SLAB);
    public static final Item OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.OAK_WOOD_STAIRS, BlocksSD.OAK_WOOD_STAIRS);
    public static final Item SPRUCE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.SPRUCE_WOOD_STAIRS, BlocksSD.SPRUCE_WOOD_STAIRS);
    public static final Item BIRCH_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.BIRCH_WOOD_STAIRS, BlocksSD.BIRCH_WOOD_STAIRS);
    public static final Item JUNGLE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.JUNGLE_WOOD_STAIRS, BlocksSD.JUNGLE_WOOD_STAIRS);
    public static final Item ACACIA_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.ACACIA_WOOD_STAIRS, BlocksSD.ACACIA_WOOD_STAIRS);
    public static final Item DARK_OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.DARK_OAK_WOOD_STAIRS, BlocksSD.DARK_OAK_WOOD_STAIRS);
    public static final Item MANGROVE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.MANGROVE_WOOD_STAIRS, BlocksSD.MANGROVE_WOOD_STAIRS);
    public static final Item POPLAR_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.POPLAR_WOOD_STAIRS, BlocksSD.POPLAR_WOOD_STAIRS);
    public static final Item CHERRY_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.CHERRY_WOOD_STAIRS, BlocksSD.CHERRY_WOOD_STAIRS);
    public static final Item PALE_OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.PALE_OAK_WOOD_STAIRS, BlocksSD.PALE_OAK_WOOD_STAIRS);
    public static final Item CRIMSON_HYPHAE_STAIRS = Items.registerBlock(BlockItemIdsSD.CRIMSON_HYPHAE_STAIRS, BlocksSD.CRIMSON_HYPHAE_STAIRS);
    public static final Item WARPED_HYPHAE_STAIRS = Items.registerBlock(BlockItemIdsSD.WARPED_HYPHAE_STAIRS, BlocksSD.WARPED_HYPHAE_STAIRS);
    public static final Item STRIPPED_OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_OAK_WOOD_SLAB, BlocksSD.STRIPPED_OAK_WOOD_SLAB);
    public static final Item STRIPPED_SPRUCE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_SLAB, BlocksSD.STRIPPED_SPRUCE_WOOD_SLAB);
    public static final Item STRIPPED_BIRCH_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_SLAB, BlocksSD.STRIPPED_BIRCH_WOOD_SLAB);
    public static final Item STRIPPED_JUNGLE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_SLAB, BlocksSD.STRIPPED_JUNGLE_WOOD_SLAB);
    public static final Item STRIPPED_ACACIA_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_SLAB, BlocksSD.STRIPPED_ACACIA_WOOD_SLAB);
    public static final Item STRIPPED_DARK_OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_SLAB, BlocksSD.STRIPPED_DARK_OAK_WOOD_SLAB);
    public static final Item STRIPPED_MANGROVE_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_SLAB, BlocksSD.STRIPPED_MANGROVE_WOOD_SLAB);
    public static final Item STRIPPED_POPLAR_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_SLAB, BlocksSD.STRIPPED_POPLAR_WOOD_SLAB);
    public static final Item STRIPPED_CHERRY_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_SLAB, BlocksSD.STRIPPED_CHERRY_WOOD_SLAB);
    public static final Item STRIPPED_PALE_OAK_WOOD_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_SLAB, BlocksSD.STRIPPED_PALE_OAK_WOOD_SLAB);
    public static final Item STRIPPED_CRIMSON_HYPHAE_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_SLAB, BlocksSD.STRIPPED_CRIMSON_HYPHAE_SLAB);
    public static final Item STRIPPED_WARPED_HYPHAE_SLAB = Items.registerBlock(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_SLAB, BlocksSD.STRIPPED_WARPED_HYPHAE_SLAB);
    public static final Item STRIPPED_OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_OAK_WOOD_STAIRS, BlocksSD.STRIPPED_OAK_WOOD_STAIRS);
    public static final Item STRIPPED_SPRUCE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_STAIRS, BlocksSD.STRIPPED_SPRUCE_WOOD_STAIRS);
    public static final Item STRIPPED_BIRCH_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_STAIRS, BlocksSD.STRIPPED_BIRCH_WOOD_STAIRS);
    public static final Item STRIPPED_JUNGLE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_STAIRS, BlocksSD.STRIPPED_JUNGLE_WOOD_STAIRS);
    public static final Item STRIPPED_ACACIA_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_STAIRS, BlocksSD.STRIPPED_ACACIA_WOOD_STAIRS);
    public static final Item STRIPPED_DARK_OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_STAIRS, BlocksSD.STRIPPED_DARK_OAK_WOOD_STAIRS);
    public static final Item STRIPPED_MANGROVE_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_STAIRS, BlocksSD.STRIPPED_MANGROVE_WOOD_STAIRS);
    public static final Item STRIPPED_POPLAR_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_STAIRS, BlocksSD.STRIPPED_POPLAR_WOOD_STAIRS);
    public static final Item STRIPPED_CHERRY_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_STAIRS, BlocksSD.STRIPPED_CHERRY_WOOD_STAIRS);
    public static final Item STRIPPED_PALE_OAK_WOOD_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_STAIRS, BlocksSD.STRIPPED_PALE_OAK_WOOD_STAIRS);
    public static final Item STRIPPED_CRIMSON_HYPHAE_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_STAIRS, BlocksSD.STRIPPED_CRIMSON_HYPHAE_STAIRS);
    public static final Item STRIPPED_WARPED_HYPHAE_STAIRS = Items.registerBlock(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_STAIRS, BlocksSD.STRIPPED_WARPED_HYPHAE_STAIRS);
    public static final Item HEAVY_SHIELD = Items.registerItem(
            ItemIdsSD.HEAVY_SHIELD,
            ShieldItem::new,
            new Item.Properties()
                    .durability(428)
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                    .repairable(ItemTags.WOODEN_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .delayedComponent(
                            DataComponents.BLOCKS_ATTACKS,
                            context -> new BlocksAttacks(
                                    0.25F,
                                    0.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(AttributesSD.SHIELD_STRENGTH, new AttributeModifier(ItemSD.SHIELD_STRENGTH, 10.0, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.ANY).build())
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK));

    public static void registration() {
        CreativeModeTabsSD.registration();
        ItemSD.modifyComponents();
        FuelValuesSD.registerFuelValues();
        PotionsSD.registration();
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final UnaryOperator<Item.Properties> propertiesFunction) {
        return registerBlockSD(id, block, (b, p) -> new BlockItem(b, propertiesFunction.apply(p)));
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory) {
        return registerBlockSD(id, block, itemFactory, new Item.Properties());
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return Items.registerItem(id.item(), p -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }
}