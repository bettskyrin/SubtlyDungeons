package net.meander.subtlyd.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.renderer.entity.TentRenderer;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.level.block.ColorCollection;

/**
 * @see EntityRenderers
 */
public class EntityRenderersSD extends EntityRenderers {
    public static void registration() {
        ColorCollection.zipApply(EntityTypesSD.TENT, ModelLayersSD.TENT, (type,  tent) -> register(type, (context) -> new TentRenderer(context, tent)));
        register(EntityTypesSD.BLAST_FUNGUS, ThrownItemRenderer::new);
        modelLayers();
    }

    private static void modelLayers() {
        for (ModelLayerLocation modelLayerLocation : ModelLayersSD.ALL_MODELS) {
            ModelLayerRegistry.registerModelLayer(modelLayerLocation, ModelLayersSD.LOCATION_PROVIDER_MAP.get(modelLayerLocation));
        }
    }
}
