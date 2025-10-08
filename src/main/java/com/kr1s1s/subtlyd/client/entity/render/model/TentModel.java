package com.kr1s1s.subtlyd.client.entity.render.model;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.entity.render.layers.TentRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class TentModel extends EntityModel<TentRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "white_tent"), "main"
    );

    private final ModelPart root;
    private final ModelPart TentModel;

    public TentModel(ModelPart root) {
        super(root);
        this.root = root;
        this.TentModel = root.getChild("TentModel");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition TentModel = partdefinition.addOrReplaceChild("TentModel", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 3.5F));

        PartDefinition Canvas = TentModel.addOrReplaceChild("Canvas", CubeListBuilder.create(), PartPose.offset(20.0F, -1.0F, 23.0F));

        Canvas.addOrReplaceChild("left_canvas_r1",
                CubeListBuilder.create().texOffs(12, 53)
                        .addBox(-48.0F, -37.0F, -1.0F, 56.0F, 40.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        Canvas.addOrReplaceChild("right_canvas_r1",
                CubeListBuilder.create().texOffs(14, 10).mirror()
                        .addBox(-48.0F, -37.0F, -1.0F, 56.0F, 40.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 1.0F, -52.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition Poles = TentModel.addOrReplaceChild("Poles", CubeListBuilder.create(), PartPose.offset(20.0F, 0.0F, 0.0F));

        Poles.addOrReplaceChild("back_pole_r1",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-1.5F, -15.0F, -1.5F, 3.0F, 30.0F, 3.0F),
                PartPose.offsetAndRotation(-47.5F, -14.0F, -3.25F, 0.0F, 1.5708F, 0.0F));

        Poles.addOrReplaceChild("front_pole_r1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, -31.0F, -3.0F, 3.0F, 30.0F, 3.0F),
                PartPose.offsetAndRotation(6.0F, 2.0F, -3.75F, 0.0F, -1.5708F, 0.0F));

        PartDefinition Stakes = TentModel.addOrReplaceChild("Stakes", CubeListBuilder.create(), PartPose.offset(20.0F, 0.0F, 25.0F));

        Stakes.addOrReplaceChild("stake_r1",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(-49.0F, 2.0F, -56.0F, 0.0F, -1.5708F, 0.0F));

        Stakes.addOrReplaceChild("stake_r2",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(-49.0F, 2.0F, -2.0F, 0.0F, -1.5708F, 0.0F));

        Stakes.addOrReplaceChild("stake_r3",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(10.0F, 2.0F, -56.0F, 0.0F, -1.5708F, 0.0F));

        Stakes.addOrReplaceChild("stake_r4",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0F, -5.0F, -1.0F, 3.0F, 5.0F, 3.0F),
                PartPose.offsetAndRotation(10.0F, 2.0F, -2.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}