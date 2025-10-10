package com.kr1s1s.subtlyd.client.renderer;

import com.kr1s1s.subtlyd.client.model.TentModel;
import com.kr1s1s.subtlyd.client.model.geom.ModelLayersSD;
import com.kr1s1s.subtlyd.world.entity.EntityTypeSD;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;

public class EntityRenderersSD extends EntityRenderers {
    public static void runRegistration() {
        register(EntityType.ZOMBIE, ZombieRendererSD::new);
        register(EntityType.HUSK, HuskRendererSD::new);
        register(EntityType.DROWNED, DrownedRendererSD::new);

        register(EntityTypeSD.WHITE_TENT, context -> new TentRenderer(context, ModelLayersSD.WHITE_TENT));
        register(EntityTypeSD.ORANGE_TENT, context -> new TentRenderer(context, ModelLayersSD.ORANGE_TENT));
        register(EntityTypeSD.MAGENTA_TENT, context -> new TentRenderer(context, ModelLayersSD.MAGENTA_TENT));
        register(EntityTypeSD.LIGHT_BLUE_TENT, context -> new TentRenderer(context, ModelLayersSD.LIGHT_BLUE_TENT));
        register(EntityTypeSD.YELLOW_TENT, context -> new TentRenderer(context, ModelLayersSD.YELLOW_TENT));
        register(EntityTypeSD.LIME_TENT, context -> new TentRenderer(context, ModelLayersSD.LIME_TENT));
        register(EntityTypeSD.PINK_TENT, context -> new TentRenderer(context, ModelLayersSD.PINK_TENT));
        register(EntityTypeSD.GRAY_TENT, context -> new TentRenderer(context, ModelLayersSD.GRAY_TENT));
        register(EntityTypeSD.LIGHT_GRAY_TENT, context -> new TentRenderer(context, ModelLayersSD.LIGHT_GRAY_TENT));
        register(EntityTypeSD.CYAN_TENT, context -> new TentRenderer(context, ModelLayersSD.CYAN_TENT));
        register(EntityTypeSD.PURPLE_TENT, context -> new TentRenderer(context, ModelLayersSD.PURPLE_TENT));
        register(EntityTypeSD.BLUE_TENT, context -> new TentRenderer(context, ModelLayersSD.BLUE_TENT));
        register(EntityTypeSD.BROWN_TENT, context -> new TentRenderer(context, ModelLayersSD.BROWN_TENT));
        register(EntityTypeSD.GREEN_TENT, context -> new TentRenderer(context, ModelLayersSD.GREEN_TENT));
        register(EntityTypeSD.RED_TENT, context -> new TentRenderer(context, ModelLayersSD.RED_TENT));
        register(EntityTypeSD.BLACK_TENT, context -> new TentRenderer(context, ModelLayersSD.BLACK_TENT));

        for (ModelLayerLocation modelLayerLocation : ModelLayersSD.TENTS) {
            EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, TentModel::createBodyLayer);
        }
    }
}
