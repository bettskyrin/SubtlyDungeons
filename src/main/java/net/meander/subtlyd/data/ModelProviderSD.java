package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.client.model.ModelTemplatesSD;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModelProviderSD extends FabricModelProvider {
    public ModelProviderSD(FabricPackOutput output) {
        super(output);
    }

    /**
     * Builds a slab model from a vanilla pillar block texture.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param newBlock The custom block that the texture will be mapped to.
     */
    public static void generatePillarSlabFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerators) {
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(vanillaBlock));
        Identifier top = TextureMapping.getBlockTexture(vanillaBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(vanillaBlock, "_side").sprite();

        TextureMapping slabTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, new Material(top))
                .put(TextureSlot.TOP, new Material(top))
                .put(TextureSlot.SIDE, new Material(side));

        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    /**
     * Creates an overhang block.
     * @param block The block to map to.
     */
    public static void generateOverhangBlock(Block block, BlockModelGenerators blockModelGenerators) {
        Identifier north = TextureMapping.getBlockTexture(block, "_north").sprite();
        Identifier east = TextureMapping.getBlockTexture(block, "_east").sprite();
        Identifier south = TextureMapping.getBlockTexture(block, "_south").sprite();
        Identifier west = TextureMapping.getBlockTexture(block, "_west").sprite();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.NORTH, new Material(north))
                .put(TextureSlot.EAST, new Material(east))
                .put(TextureSlot.SOUTH, new Material(south))
                .put(TextureSlot.WEST, new Material(west))
                .put(TextureSlot.PARTICLE, new Material(north));
        Identifier model = ModelTemplatesSD.OVERHANG_BLOCK.create(block, mapping, blockModelGenerators.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(model));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multiVariant));
    }

    /**
     * Creates a textured cauldron block
     */
    @SuppressWarnings("DataFlowIssue")
    public static void generateFilledCauldron(Block block, BlockModelGenerators blockModelGenerators) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.CONTENT, new Material(Identifier.tryParse("minecraft:block/water_still")))
                .put(TextureSlot.TOP, new Material(Identifier.tryParse("minecraft:block/cauldron_top")))
                .put(TextureSlot.INSIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_inner")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.tryParse("minecraft:block/cauldron_bottom")))
                .put(TextureSlot.SIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")))
                .put(TextureSlot.PARTICLE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")));
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);

        Identifier level1Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level1");
        Identifier level2Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level2");
        Identifier level3Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_full");

        Identifier level1 = ModelTemplates.CAULDRON_LEVEL1.create(level1Id, mapping, blockModelGenerators.modelOutput);
        Identifier level2 = ModelTemplates.CAULDRON_LEVEL2.create(level2Id, mapping, blockModelGenerators.modelOutput);
        Identifier level3 = ModelTemplates.CAULDRON_FULL.create(level3Id, mapping, blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
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
        blockModelGenerator.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        generateOverhangBlock(BlocksSD.WARPED_OVERHANG, blockModelGenerator);
        generatePillarSlabFromVanilla(Blocks.BASALT, BlocksSD.BASALT_SLAB, blockModelGenerator);
        blockModelGenerator.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        generateFilledCauldron(BlocksSD.POTION_CAULDRON, blockModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        ItemsSD.TENT.forEach(item -> itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        itemModelGenerator.generateFlatItem(ItemsSD.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COOKED_CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.POTTAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.REEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.WARPED_OVERHANG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.BLAST_FUNGUS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.ELIXIR, ModelTemplates.FLAT_ITEM);
    }
}
