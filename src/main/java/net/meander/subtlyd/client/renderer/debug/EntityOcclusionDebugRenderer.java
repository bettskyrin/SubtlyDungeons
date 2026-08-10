package net.meander.subtlyd.client.renderer.debug;

import net.meander.subtlyd.client.renderer.entity.OcclusionManager;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityOcclusionDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private final OcclusionManager occlusionManager;

    public EntityOcclusionDebugRenderer(final Minecraft minecraft, final OcclusionManager occlusionManager) {
        this.minecraft = minecraft;
        this.occlusionManager = occlusionManager;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        ClientLevel level = minecraft.level;

        if (level != null) {
            boolean isFirstPerson = minecraft.options.getCameraType() == CameraType.FIRST_PERSON;
            int visibleColor = 0xFF198019;
            int occludedColor = 0xFF960000;
            Entity cameraEntity = minecraft.getCameraEntity();
            Vec3 cameraPos = new Vec3(camX, camY, camZ);

            for (Entity entity : level.entitiesForRendering()) {
                if (entity != cameraEntity || !isFirstPerson) {
                    AABB boundingBox = entity.getBoundingBox();

                    if (frustum.isVisible(boundingBox)) {
                        boolean isOccluded = occlusionManager.isEntityOccluded(entity, cameraPos, level);
                        int color = isOccluded ? occludedColor : visibleColor;
                        float deltaTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(!level.tickRateManager().isEntityFrozen(entity));

                        Vec3 interpolatedPos = entity.getPosition(deltaTick);
                        Vec3 renderOffset = interpolatedPos.subtract(entity.position());
                        AABB renderBox = boundingBox.move(renderOffset);
                        AABB perspectiveBox = occlusionManager.getOcclusionBox(renderBox);

                        GizmoStyle stroke = GizmoStyle.stroke(color);

                        Gizmos.cuboid(perspectiveBox, stroke);
                        drawGridLines(perspectiveBox, cameraPos, color);
                    }
                }
            }
        }
    }

    private void drawGridLines(AABB perspectiveBox, Vec3 cameraPos, int color) {
        OcclusionManager.traverseBoxFaces(perspectiveBox, cameraPos, OcclusionManager.STEP_SIZE, target -> {
            Gizmos.line(cameraPos, target, color);
            return false;
        });
    }
}
