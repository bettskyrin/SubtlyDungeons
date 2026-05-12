package net.meander.subtlyd.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.meander.subtlyd.client.model.TentModel;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.entity.PotionCauldronBlockEntity;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.ColorCollection;

public class EntityRenderersSD extends EntityRenderers {
    public static void registration() {
        ColorCollection.zipApply(EntityTypesSD.TENT, ModelLayersSD.TENT,
                (type,  tent) -> EntityRenderers.register(type, (context) -> new TentRenderer(context, tent)));

        for (ModelLayerLocation modelLayerLocation : ModelLayersSD.ALL_MODELS) {
            ModelLayerRegistry.registerModelLayer(modelLayerLocation, TentModel::createBodyLayer);
        }
        register(EntityTypesSD.BLAST_FUNGUS, ThrownItemRenderer::new);

        BlockColorRegistry.register((_, view, pos, tintValues) -> {
            int cauldronColor = 0x385dc6; // Water

            if (view.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity) {
                if (blockEntity.getPotion() != null) {
                    cauldronColor = PotionContents.getColorOptional(blockEntity.getPotion().value().getEffects()).orElse(cauldronColor);
                }
            }
            tintValues.add(cauldronColor);
        }, BlocksSD.POTION_CAULDRON);
    }
}
