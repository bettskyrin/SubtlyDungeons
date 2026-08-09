package net.meander.subtlyd.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OcclusionManager {
    public static final OcclusionManager INSTANCE = new OcclusionManager();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final Map<Integer, Boolean> visibilityCache = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> isCalculating = new ConcurrentHashMap<>();

    public boolean isEntityVisible(Entity entity, Vec3 cameraPos, BlockGetter level) {
        int entityId = entity.getId();

        if (!visibilityCache.containsKey(entityId)) {
            queueVisibilityCheck(entity, cameraPos, level);
            return true; 
        } else if (entity.tickCount % 10 == 0) {
            queueVisibilityCheck(entity, cameraPos, level);
        }

        return visibilityCache.get(entityId);
    }

    private void queueVisibilityCheck(Entity entity, Vec3 cameraPos, BlockGetter level) {
        int entityId = entity.getId();

        if (!isCalculating.getOrDefault(entityId, false)) {
            AABB boundingBox = entity.getBoundingBox();

            isCalculating.put(entityId, true);

            executor.submit(() -> {
                try {
                    boolean isVisible = checkLineOfSight(cameraPos, boundingBox, level);

                    visibilityCache.put(entityId, isVisible);
                } catch (Exception e) {
                    visibilityCache.put(entityId, true);
                } finally {
                    isCalculating.put(entityId, false);
                }
            });
        }
    }

    private boolean checkLineOfSight(Vec3 cameraPos, AABB box, BlockGetter level) {
        Vec3[] targetPoints = new Vec3[]{
            box.getCenter(),
            new Vec3(box.getCenter().x, box.maxY, box.getCenter().z),
            new Vec3(box.getCenter().x, box.minY, box.getCenter().z)
        };

        for (Vec3 target : targetPoints) {
            ClipContext context = new ClipContext(
                    cameraPos,
                    target,
                    ClipContext.Block.VISUAL,
                    ClipContext.Fluid.NONE,
                    CollisionContext.empty()
            );

            if (level.clip(context).getType() == HitResult.Type.MISS) {
                return true; 
            }
        }

        return false;
    }

    public void clearCache() {
        visibilityCache.clear();
        isCalculating.clear();
    }
}