package net.meander.subtlyd.client.data.model;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;

public class ModelProviderSD extends FabricModelProvider {
    public ModelProviderSD(FabricPackOutput output) {
        super(output);
    }

    /**
     * Builds a slab model from a vanilla pillar block texture.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param newBlock The custom block that the texture will be mapped to.
     */
    private static void generatePillarSlabFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerator) {
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(vanillaBlock));
        Identifier top = TextureMapping.getBlockTexture(vanillaBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(vanillaBlock, "_side").sprite();

        TextureMapping slabTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, new Material(top))
                .put(TextureSlot.TOP, new Material(top))
                .put(TextureSlot.SIDE, new Material(side));

        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerator.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerator.modelOutput));

        blockModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    /**
     * Builds a stair model from a vanilla pillar block texture.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param newBlock The custom stair block that the texture will be mapped to.
     */
    private static void generatePillarStairsFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerator) {
        Identifier top = TextureMapping.getBlockTexture(vanillaBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(vanillaBlock, "_side").sprite();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.BOTTOM, new Material(top))
                .put(TextureSlot.TOP, new Material(top))
                .put(TextureSlot.SIDE, new Material(side));

        MultiVariant inner = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.create(newBlock, mapping, blockModelGenerator.modelOutput));
        MultiVariant straight = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_STRAIGHT.create(newBlock, mapping, blockModelGenerator.modelOutput));
        MultiVariant outer = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.create(newBlock, mapping, blockModelGenerator.modelOutput));

        blockModelGenerator.blockStateOutput.accept(BlockModelGenerators.createStairs(newBlock, inner, straight, outer));
    }

    /**
     * Separate from the rest of the block family generation to allow for snowlogging.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param wallBlock The custom wall that the texture will be mapped to.
     */
    private static void generateCustomWallFromVanilla(Block vanillaBlock, Block wallBlock, BlockModelGenerators blockModelGenerator) {
        if (BuiltInRegistries.BLOCK.getKey(wallBlock).toString().contains("subtlyd:")) {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.WALL, TextureMapping.getBlockTexture(vanillaBlock))
                    .put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(vanillaBlock))
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(vanillaBlock));

            ModelTemplates.WALL_POST.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_LOW_SIDE.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_TALL_SIDE.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_INVENTORY.create(wallBlock, mapping, blockModelGenerator.modelOutput);
        }
    }

    public static void generateCustomWalls(BlockModelGenerators blockModelGenerator) {
        generateCustomWallFromVanilla(BlocksSD.POLISHED_DRIPSTONE, BlocksSD.POLISHED_DRIPSTONE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.STONE_TILES, BlocksSD.STONE_TILE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.SNOW_BRICKS, BlocksSD.SNOW_BRICK_WALL, blockModelGenerator);
    }

    /**
     * Creates an overhang block.
     * @param overhangBlock The block to map to.
     */
    private static void generateOverhangBlock(Block overhangBlock, BlockModelGenerators blockModelGenerator) {
        Identifier north = TextureMapping.getBlockTexture(overhangBlock, "_north").sprite();
        Identifier east = TextureMapping.getBlockTexture(overhangBlock, "_east").sprite();
        Identifier south = TextureMapping.getBlockTexture(overhangBlock, "_south").sprite();
        Identifier west = TextureMapping.getBlockTexture(overhangBlock, "_west").sprite();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.NORTH, new Material(north))
                .put(TextureSlot.EAST, new Material(east))
                .put(TextureSlot.SOUTH, new Material(south))
                .put(TextureSlot.WEST, new Material(west))
                .put(TextureSlot.PARTICLE, new Material(north));
        Identifier model = ModelTemplatesSD.OVERHANG_BLOCK.create(overhangBlock, mapping, blockModelGenerator.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(model));

        blockModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(overhangBlock, multiVariant));
    }

    /**
     * Creates a textured cauldron block
     */
    @SuppressWarnings("DataFlowIssue")
    private static void generateFilledCauldron(Block cauldronBlock, BlockModelGenerators blockModelGenerator) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.CONTENT, new Material(Identifier.tryParse("minecraft:block/water_still")))
                .put(TextureSlot.TOP, new Material(Identifier.tryParse("minecraft:block/cauldron_top")))
                .put(TextureSlot.INSIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_inner")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.tryParse("minecraft:block/cauldron_bottom")))
                .put(TextureSlot.SIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")))
                .put(TextureSlot.PARTICLE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")));
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(cauldronBlock);

        Identifier level1Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level1");
        Identifier level2Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level2");
        Identifier level3Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_full");

        Identifier level1 = ModelTemplates.CAULDRON_LEVEL1.create(level1Id, mapping, blockModelGenerator.modelOutput);
        Identifier level2 = ModelTemplates.CAULDRON_LEVEL2.create(level2Id, mapping, blockModelGenerator.modelOutput);
        Identifier level3 = ModelTemplates.CAULDRON_FULL.create(level3Id, mapping, blockModelGenerator.modelOutput);

        blockModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(cauldronBlock)
                .with(PropertyDispatch.initial(PotionCauldronBlock.POTION_LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(level1))
                        .select(2, BlockModelGenerators.plainVariant(level1))
                        .select(3, BlockModelGenerators.plainVariant(level2))
                        .select(4, BlockModelGenerators.plainVariant(level2))
                        .select(5, BlockModelGenerators.plainVariant(level3))
                        .select(6, BlockModelGenerators.plainVariant(level3))
                )
        );
    }

    private void generateCustomFlowerBedBlock(final Block flowerbed, BlockModelGenerators blockModelGenerator) {
        BlockModelGenerators.plainVariant(TexturedModel.FLOWERBED_1.create(flowerbed, blockModelGenerator.modelOutput));
        BlockModelGenerators.plainVariant(TexturedModel.FLOWERBED_2.create(flowerbed, blockModelGenerator.modelOutput));
        BlockModelGenerators.plainVariant(TexturedModel.FLOWERBED_3.create(flowerbed, blockModelGenerator.modelOutput));
        BlockModelGenerators.plainVariant(TexturedModel.FLOWERBED_4.create(flowerbed, blockModelGenerator.modelOutput));
    }

    private void generateSnowloggables(BlockModelGenerators blockModelGenerator) {
        SnowloggedBlockModelProvider.generateSnowloggableSimpleVegetation(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableAgingVegetation(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableTallVegetation(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableSegmentableVegetation(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableFences(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableCrossCollisionBlocks(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableWalls(blockModelGenerator);
        SnowloggedBlockModelProvider.generateSnowloggableFenceGates(blockModelGenerator);
    }

    private void generatePotionArchetypes(ItemModelGenerators itemModelGenerator) {
        Identifier conicalBottle = Util.identifier("item/potion/conical_overlay");
        Identifier sphericalBottle = Util.identifier("item/potion/spherical_overlay");
        Identifier vialBottle = Util.identifier("item/potion/vial_overlay");

        ModelTemplates.FLAT_ITEM.create(conicalBottle, TextureMapping.layer0(new Material(conicalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(sphericalBottle, TextureMapping.layer0(new Material(sphericalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(vialBottle, TextureMapping.layer0(new Material(vialBottle)), itemModelGenerator.modelOutput);
    }

    private void generateInventoryItemFromBlock(Block block, ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block, "_inventory")));
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS)
                .stairs(BlocksSD.SNOW_BRICK_STAIRS)
                .slab(BlocksSD.SNOW_BRICK_SLAB)
                .getFamily());
        blockModelGenerator.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(new BlockFamily.Builder(BlocksSD.POLISHED_DRIPSTONE)
                .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .getFamily());
        blockModelGenerator.family(BlocksSD.STONE_TILES).generateFor(new BlockFamily.Builder(BlocksSD.STONE_TILES)
                .stairs(BlocksSD.STONE_TILE_STAIRS)
                .slab(BlocksSD.STONE_TILE_SLAB)
                .getFamily());
        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerator.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        blockModelGenerator.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        blockModelGenerator.createTrivialCube(BlocksSD.IRON_GRATE);
        blockModelGenerator.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        generateOverhangBlock(BlocksSD.WARPED_OVERHANG, blockModelGenerator);
        generatePillarSlabFromVanilla(Blocks.BASALT, BlocksSD.BASALT_SLAB, blockModelGenerator);
        blockModelGenerator.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        generateFilledCauldron(BlocksSD.POTION_CAULDRON, blockModelGenerator);
        generateCustomFlowerBedBlock(BlocksSD.PERSE_WILDFLOWERS, blockModelGenerator);
        generateSnowloggables(blockModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        ItemsSD.TENT.forEach(item -> itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        itemModelGenerator.generateFlatItem(ItemsSD.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COOKED_CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_SOUL_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.POTTAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.REEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.WARPED_OVERHANG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.BLAST_FUNGUS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COVEN_ELIXIR, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.PERSE_WILDFLOWERS, ModelTemplates.FLAT_ITEM);
        generatePotionArchetypes(itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.POLISHED_DRIPSTONE_WALL, itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.STONE_TILE_WALL, itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.SNOW_BRICK_WALL, itemModelGenerator);
    }
}
