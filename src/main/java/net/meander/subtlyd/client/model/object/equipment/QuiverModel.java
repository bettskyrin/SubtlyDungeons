package net.meander.subtlyd.client.model.object.equipment;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class QuiverModel extends EntityModel<HumanoidRenderState> {
    public final ModelPart bag;
    public final ModelPart arrow1;
    public final ModelPart arrow2;
    public final ModelPart arrow3;

    public QuiverModel(ModelPart root) {
        super(root);

        bag = root.getChild("bag");
        arrow1 = bag.getChild("arrow1");
        arrow2 = bag.getChild("arrow2");
        arrow3 = bag.getChild("arrow3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bag = partdefinition.addOrReplaceChild("bag", CubeListBuilder.create()
            .texOffs(16, 0).addBox(-0.3984F, -2.4751F, -2.0438F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.25F))
            .texOffs(0, 0).addBox(-0.3984F, -2.4751F, -2.0438F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-3.6016F, 0.4751F, 5.0438F));

        bag.addOrReplaceChild("arrow1", CubeListBuilder.create()
            .texOffs(10, 17).addBox(-1.5625F, -9.267F, -0.05F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 18).addBox(-0.5625F, -6.267F, -0.45F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(2.1641F, 0.7919F, 0.4063F, 0.0F, -0.192F, 0.0F));

        bag.addOrReplaceChild("arrow2", CubeListBuilder.create()
            .texOffs(10, 17).addBox(-1.5625F, -9.267F, -0.05F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 18).addBox(-0.5625F, -6.267F, -0.45F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), 
            PartPose.offsetAndRotation(0.6641F, 1.7919F, 0.4063F, 0.0F, -0.7854F, 0.0F));

        bag.addOrReplaceChild("arrow3", CubeListBuilder.create()
            .texOffs(10, 17).addBox(-1.5625F, -9.267F, -0.05F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 18).addBox(-0.5625F, -6.267F, -0.45F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), 
            PartPose.offset(1.1641F, -0.2081F, -0.5938F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(HumanoidRenderState state) {}
}