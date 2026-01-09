package com.kr1s1s.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import com.kr1s1s.subtlyd.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;

import java.util.Set;

public class ModelLayersSD extends ModelLayers {
    public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ModelLayerLocation WHITE_TENT = register("tent/white_tent");
    public static final ModelLayerLocation ORANGE_TENT = register("tent/orange_tent");
    public static final ModelLayerLocation MAGENTA_TENT = register("tent/magenta_tent");
    public static final ModelLayerLocation LIGHT_BLUE_TENT = register("tent/light_blue_tent");
    public static final ModelLayerLocation YELLOW_TENT = register("tent/yellow_tent");
    public static final ModelLayerLocation LIME_TENT = register("tent/lime_tent");
    public static final ModelLayerLocation PINK_TENT = register("tent/pink_tent");
    public static final ModelLayerLocation GRAY_TENT = register("tent/gray_tent");
    public static final ModelLayerLocation LIGHT_GRAY_TENT = register("tent/light_gray_tent");
    public static final ModelLayerLocation CYAN_TENT = register("tent/cyan_tent");
    public static final ModelLayerLocation PURPLE_TENT = register("tent/purple_tent");
    public static final ModelLayerLocation BLUE_TENT = register("tent/blue_tent");
    public static final ModelLayerLocation BROWN_TENT = register("tent/brown_tent");
    public static final ModelLayerLocation GREEN_TENT = register("tent/green_tent");
    public static final ModelLayerLocation RED_TENT = register("tent/red_tent");
    public static final ModelLayerLocation BLACK_TENT = register("tent/black_tent");

    private static ModelLayerLocation register(String string) {
        ModelLayerLocation modelLayerLocation = createLocation(string);
        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
        } else {
            return modelLayerLocation;
        }
    }

    private static ModelLayerLocation createLocation(String string) {
        return new ModelLayerLocation(Util.identifier(string), "main");
    }
}
