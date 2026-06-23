package net.meander.subtlyd.client.data.model;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.block.StewCauldronBlock;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
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
import net.minecraft.server.packs.resources.ResourceManager;
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

    @SuppressWarnings("DataFlowIssue")
    private static Identifier[] generateCauldronContentsModel(Identifier baseId, String nameSuffix, Identifier liquidTexture, BlockModelGenerators blockModelGenerator) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.CONTENT, new Material(liquidTexture))
                .put(TextureSlot.TOP, new Material(Identifier.tryParse("minecraft:block/cauldron_top")))
                .put(TextureSlot.INSIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_inner")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.tryParse("minecraft:block/cauldron_bottom")))
                .put(TextureSlot.SIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")))
                .put(TextureSlot.PARTICLE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")));

        Identifier level1Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_level1");
        Identifier level2Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_level2");
        Identifier level3Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_full");

        return new Identifier[]{
                ModelTemplates.CAULDRON_LEVEL1.create(level1Id, mapping, blockModelGenerator.modelOutput),
                ModelTemplates.CAULDRON_LEVEL2.create(level2Id, mapping, blockModelGenerator.modelOutput),
                ModelTemplates.CAULDRON_FULL.create(level3Id, mapping, blockModelGenerator.modelOutput)
        };
    }

    private static void generatePotionCauldron(BlockModelGenerators blockModelGenerator) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.POTION_CAULDRON);
        Identifier[] waterModels = generateCauldronContentsModel(blockId, "", Identifier.tryParse("minecraft:block/water_still"), blockModelGenerator);

        blockModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlocksSD.POTION_CAULDRON)
                .with(PropertyDispatch.initial(PotionCauldronBlock.POTION_LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(waterModels[0]))
                        .select(2, BlockModelGenerators.plainVariant(waterModels[0]))
                        .select(3, BlockModelGenerators.plainVariant(waterModels[1]))
                        .select(4, BlockModelGenerators.plainVariant(waterModels[1]))
                        .select(5, BlockModelGenerators.plainVariant(waterModels[2]))
                        .select(6, BlockModelGenerators.plainVariant(waterModels[2]))
                )
        );
    }

    private static void generateStewCauldron(BlockModelGenerators blockModelGenerator) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.STEW_CAULDRON);

        Identifier lightTexture = Identifier.tryParse(blockId.getNamespace() + ":block/light_stew_still");
        Identifier[] lightModels = generateCauldronContentsModel(blockId, "_light", lightTexture, blockModelGenerator);

        Identifier finishedTexture = Identifier.tryParse(blockId.getNamespace() + ":block/heavy_stew_still");
        Identifier[] finishedModels = generateCauldronContentsModel(blockId, "_heavy", finishedTexture, blockModelGenerator);

        blockModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlocksSD.STEW_CAULDRON)
                .with(PropertyDispatch.initial(StewCauldronBlock.LEVEL, StewCauldronBlock.IS_HEAVY_STEW)
                        .select(1, false, BlockModelGenerators.plainVariant(lightModels[0]))
                        .select(2, false, BlockModelGenerators.plainVariant(lightModels[1]))
                        .select(3, false, BlockModelGenerators.plainVariant(lightModels[2]))
                        .select(1, true, BlockModelGenerators.plainVariant(finishedModels[0]))
                        .select(2, true, BlockModelGenerators.plainVariant(finishedModels[1]))
                        .select(3, true, BlockModelGenerators.plainVariant(finishedModels[2]))));
    }

    private void generatePotionArchetypes(ItemModelGenerators itemModelGenerator) {
        Identifier conicalBottle = Util.identifier("item/potion/conical_overlay");
        Identifier sphericalBottle = Util.identifier("item/potion/spherical_overlay");
        Identifier vialBottle = Util.identifier("item/potion/vial_overlay");

        ModelTemplates.FLAT_ITEM.create(conicalBottle, TextureMapping.layer0(new Material(conicalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(sphericalBottle, TextureMapping.layer0(new Material(sphericalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(vialBottle, TextureMapping.layer0(new Material(vialBottle)), itemModelGenerator.modelOutput);
    }

    public static void generateRuntimeBlockModels(ResourceManager resourceManager) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block.defaultBlockState().hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);

                switch (block) {
                    case WallBlock _ -> SnowloggedBlockModelProvider.generateWallState(block, blockId);
                    case FenceBlock fence when fence == Blocks.BAMBOO_FENCE -> SnowloggedBlockModelProvider.generateAltFenceState(block, blockId);
                    case FenceBlock _ -> SnowloggedBlockModelProvider.generateFenceState(block, blockId);
                    case FenceGateBlock _ -> SnowloggedBlockModelProvider.generateFenceGateState(block, blockId);
                    case CrossCollisionBlock _ -> SnowloggedBlockModelProvider.generateCrossState(blockId, resourceManager);
                    case DoublePlantBlock _ -> SnowloggedBlockModelProvider.generateDoublePlantState(block, blockId, resourceManager);
                    case SegmentableBlock _ -> SnowloggedBlockModelProvider.generateSegmentableState(block, blockId, resourceManager);
                    case CropBlock _, SweetBerryBushBlock _, NetherWartBlock _, StemBlock _ -> SnowloggedBlockModelProvider.generateAgeableState(block, blockId, resourceManager);
                    default -> SnowloggedBlockModelProvider.generatePlantState(block, blockId, resourceManager);
                }
            }
        }
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS)
                .stairs(BlocksSD.SNOW_BRICK_STAIRS)
                .slab(BlocksSD.SNOW_BRICK_SLAB)
                .wall(BlocksSD.SNOW_BRICK_WALL)
                .getFamily());
        blockModelGenerator.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(new BlockFamily.Builder(BlocksSD.POLISHED_DRIPSTONE)
                .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .wall(BlocksSD.POLISHED_DRIPSTONE_WALL)
                .getFamily());
        blockModelGenerator.family(BlocksSD.STONE_TILES).generateFor(new BlockFamily.Builder(BlocksSD.STONE_TILES)
                .stairs(BlocksSD.STONE_TILE_STAIRS)
                .slab(BlocksSD.STONE_TILE_SLAB)
                .wall(BlocksSD.STONE_TILE_WALL)
                .getFamily());
        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerator.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        blockModelGenerator.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        blockModelGenerator.createTrivialCube(BlocksSD.IRON_GRATE);
        blockModelGenerator.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        blockModelGenerator.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerator.createFlowerBed(BlocksSD.PERSE_WILDFLOWERS);
        generateOverhangBlock(BlocksSD.WARPED_OVERHANG, blockModelGenerator);
        generatePillarSlabFromVanilla(Blocks.BASALT, BlocksSD.BASALT_SLAB, blockModelGenerator);
        generatePotionCauldron(blockModelGenerator);
        generateStewCauldron(blockModelGenerator);
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
        itemModelGenerator.generateFlatItem(ItemsSD.LIGHT_STEW, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.WOODEN_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.STONE_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COPPER_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.IRON_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.GOLDEN_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.DIAMOND_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.NETHERITE_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        generatePotionArchetypes(itemModelGenerator);
    }
}
