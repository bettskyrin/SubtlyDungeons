package net.meander.subtlyd.mixin.client.renderer.extract;

import net.meander.subtlyd.client.EntityCullingMethod;
import net.meander.subtlyd.client.renderer.entity.OcclusionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelExtractor.class)
public class LevelExtractorMixin {
    @Shadow private ClientLevel level;
    private final Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "isEntityVisible", at = @At("RETURN"), cancellable = true)
    private void cullOccludedEntity(final Entity entity, final Frustum frustum, final double camX, final double camY, final double camZ, CallbackInfoReturnable<Boolean> cir) {
        Options options = minecraft.options;

        if (options.entityCulling().get() == EntityCullingMethod.OCCLUSION) {
            if (cir.getReturnValue() && level != null) {
                Vec3 cameraPos = new Vec3(camX, camY, camZ);
                double distanceSqr = cameraPos.distanceToSqr(entity.position());

                if (distanceSqr > OcclusionManager.getInstance().getCurrentFogEndSqr()) {
                    cir.setReturnValue(false);
                } else if (OcclusionManager.getInstance().isEntityOccluded(entity, cameraPos, level)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "setSectionDirty(IIIZ)V", at = @At("HEAD"))
    private void onSectionDirty(final int sectionX, final int sectionY, final int sectionZ, final boolean playerChanged, CallbackInfo ci) {
        OcclusionManager.getInstance().invalidateSection(SectionPos.of(sectionX, sectionY, sectionZ));
    }
}