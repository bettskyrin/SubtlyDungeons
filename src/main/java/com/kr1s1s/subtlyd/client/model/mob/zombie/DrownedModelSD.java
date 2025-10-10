package com.kr1s1s.subtlyd.client.model.mob.zombie;

import com.kr1s1s.subtlyd.client.renderer.state.ZombieRenderStateSD;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class DrownedModelSD extends ZombieModel<ZombieRenderStateSD> {
    public DrownedModelSD(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(ZombieRenderStateSD zombieRenderState) {
        super.setupAnim(zombieRenderState);
        if (zombieRenderState.leftArmPose == ArmPose.THROW_TRIDENT) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
            this.leftArm.yRot = 0.0F;
        }

        if (zombieRenderState.rightArmPose == ArmPose.THROW_TRIDENT) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
            this.rightArm.yRot = 0.0F;
        }

        float f = zombieRenderState.swimAmount;
        if (f > 0.0F) {
            this.rightArm.xRot = Mth.rotLerpRad(f, this.rightArm.xRot, (float) (-Math.PI * 4.0 / 5.0)) + f * 0.35F * Mth.sin(0.1F * zombieRenderState.ageInTicks);
            this.leftArm.xRot = Mth.rotLerpRad(f, this.leftArm.xRot, (float) (-Math.PI * 4.0 / 5.0)) - f * 0.35F * Mth.sin(0.1F * zombieRenderState.ageInTicks);
            this.rightArm.zRot = Mth.rotLerpRad(f, this.rightArm.zRot, -0.15F);
            this.leftArm.zRot = Mth.rotLerpRad(f, this.leftArm.zRot, 0.15F);
            this.leftLeg.xRot = this.leftLeg.xRot - f * 0.55F * Mth.sin(0.1F * zombieRenderState.ageInTicks);
            this.rightLeg.xRot = this.rightLeg.xRot + f * 0.55F * Mth.sin(0.1F * zombieRenderState.ageInTicks);
            this.head.xRot = 0.0F;
        }
    }
}
