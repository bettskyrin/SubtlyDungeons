package net.meander.subtlyd.client.data.models;

import net.meander.subtlyd.client.renderer.special.HeavyShieldSpecialRenderer;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

/**
 * @see net.minecraft.client.data.models.ModelProvider
 */
public class ItemModelGeneratorsSD  {
    private void generatePotionArchetypes(ItemModelGenerators itemModelGenerators) {
        Identifier conicalBottle = UtilSD.identifier("item/potion/conical_overlay");
        Identifier sphericalBottle = UtilSD.identifier("item/potion/spherical_overlay");
        Identifier vialBottle = UtilSD.identifier("item/potion/vial_overlay");

        ModelTemplates.FLAT_ITEM.create(conicalBottle, TextureMapping.layer0(new Material(conicalBottle)), itemModelGenerators.modelOutput);
        ModelTemplates.FLAT_ITEM.create(sphericalBottle, TextureMapping.layer0(new Material(sphericalBottle)), itemModelGenerators.modelOutput);
        ModelTemplates.FLAT_ITEM.create(vialBottle, TextureMapping.layer0(new Material(vialBottle)), itemModelGenerators.modelOutput);
    }

    private void generateShield(ItemModelGenerators itemModelGenerators, final Item item) {
        ItemModel.Unbaked normal = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item), new HeavyShieldSpecialRenderer.Unbaked());
        ItemModel.Unbaked blocking = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(item, "_blocking"), new HeavyShieldSpecialRenderer.Unbaked());

        itemModelGenerators.itemModelOutput.accept(item, ItemModelUtils.conditional(HeavyShieldSpecialRenderer.DEFAULT_TRANSFORMATION, ItemModelUtils.isUsingItem(), blocking, normal));
    }

    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        ItemsSD.TENT.forEach(item -> itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        itemModelGenerators.generateFlatItem(ItemsSD.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.COOKED_CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.UNLIT_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.UNLIT_SOUL_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.POTTAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.REEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.WARPED_OVERHANG, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.BLAST_FUNGUS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.COVEN_ELIXIR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.LIGHT_STEW, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.WOODEN_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.STONE_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.COPPER_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.IRON_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.GOLDEN_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.DIAMOND_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.NETHERITE_DAGGER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerators.generateFlatItem(ItemsSD.QUIVER, ModelTemplates.FLAT_ITEM);
        ItemsSD.DYED_QUIVER.forEach(item -> itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        generatePotionArchetypes(itemModelGenerators);
        generateShield(itemModelGenerators, ItemsSD.HEAVY_SHIELD);
    }
}
