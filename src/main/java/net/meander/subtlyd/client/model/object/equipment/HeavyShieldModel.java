package net.meander.subtlyd.client.model.object.equipment;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

/**
 * @see net.minecraft.client.model.object.equipment.ShieldModel
 */
@Environment(EnvType.CLIENT)
public class HeavyShieldModel extends Model<Unit> {
	private final ModelPart plate;
	private final ModelPart handle;

	public HeavyShieldModel(final ModelPart root) {
		super(root, RenderTypes::entitySolid);
		plate = root.getChild("plate");
		handle = root.getChild("handle");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F), PartPose.ZERO);
		root.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 64);
	}

	public ModelPart plate() {
		return plate;
	}

	public ModelPart handle() {
		return handle;
	}
}
