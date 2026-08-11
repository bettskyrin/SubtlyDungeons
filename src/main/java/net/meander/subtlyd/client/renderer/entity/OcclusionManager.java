package net.meander.subtlyd.client.renderer.entity;

import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
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
import java.util.function.Predicate;

public class OcclusionManager {
    public static final double STEP_SIZE = 0.75;
    private static final OcclusionManager INSTANCE = new OcclusionManager();
    private final ExecutorService executor;
    private final Map<Integer, OcclusionState> visibilityCache = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> isCalculating = new ConcurrentHashMap<>();
    private float currentFogEndSqr = Float.MAX_VALUE;

    public OcclusionManager() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int threads = Math.max(1, availableProcessors - 2);

        executor = Executors.newFixedThreadPool(threads, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("Occlusion-Culling-SD");
            return thread;
        });
    }

    public static OcclusionManager getInstance() {
        return INSTANCE;
    }

    public boolean isEntityOccluded(Entity entity, Vec3 cameraPos, BlockGetter level) {
        int entityId = entity.getId();
        Vec3 entityPos = entity.position();
        OcclusionState state = visibilityCache.get(entityId);

        if (state != null) {
            boolean cameraMoved = cameraPos.distanceToSqr(state.prevCameraPos()) > 0.01;
            boolean entityMoved = entityPos.distanceToSqr(state.prevEntityPos()) > 0.01;

            if (cameraMoved || entityMoved || state.isDirty()) {
                queueVisibilityCheck(entity, cameraPos, level);
            }

            return !state.isOccluded();
        }

        queueVisibilityCheck(entity, cameraPos, level);
        return false;
    }

    public void setCurrentFogEndSqr(float fogEnd) {
        currentFogEndSqr = Mth.square(fogEnd);
    }

    public float getCurrentFogEndSqr() {
        return currentFogEndSqr;
    }

    public void invalidateSection(SectionPos sectionPos) {
        visibilityCache.entrySet().removeIf(entry -> {
            OcclusionState state = entry.getValue();
            SectionPos entitySection = SectionPos.of(state.prevEntityPos());

            return Math.abs(entitySection.x() - sectionPos.x()) <= 1
                    && Math.abs(entitySection.y() - sectionPos.y()) <= 1
                    && Math.abs(entitySection.z() - sectionPos.z()) <= 1;
        });
    }

    public void clearCache() {
        visibilityCache.clear();
        isCalculating.clear();
    }

    private void queueVisibilityCheck(Entity entity, Vec3 cameraPos, BlockGetter level) {
        int entityId = entity.getId();

        if (!isCalculating.getOrDefault(entityId, false)) {
            AABB boundingBox = entity.getBoundingBox();
            Vec3 entityPos = entity.position();

            isCalculating.put(entityId, true);

            executor.execute(() -> {
                try {
                    boolean isNotOccluded = checkLineOfSight(cameraPos, boundingBox, level);
                    visibilityCache.put(entityId, new OcclusionState(isNotOccluded, cameraPos, entityPos, false));
                } catch (Exception exception) {
                    OcclusionState prevState = visibilityCache.get(entityId);
                    boolean fallback = prevState == null || prevState.isOccluded();
                    visibilityCache.put(entityId, new OcclusionState(fallback, cameraPos, entityPos, false));
                } finally {
                    isCalculating.put(entityId, false);
                }
            });
        }
    }

    public static void traverseBoxFaces(AABB expandedBox, Vec3 cameraPos, double stepSize, Predicate<Vec3> pointAction) {
        int xTotalSteps = Math.max(1, Mth.ceil((expandedBox.maxX - expandedBox.minX) / stepSize));
        int yTotalSteps = Math.max(1, Mth.ceil((expandedBox.maxY - expandedBox.minY) / stepSize));
        int zTotalSteps = Math.max(1, Mth.ceil((expandedBox.maxZ - expandedBox.minZ) / stepSize));

        double xStep = (expandedBox.maxX - expandedBox.minX) / xTotalSteps;
        double yStep = (expandedBox.maxY - expandedBox.minY) / yTotalSteps;
        double zStep = (expandedBox.maxZ - expandedBox.minZ) / zTotalSteps;

        boolean testMinX = cameraPos.x() < expandedBox.getCenter().x();
        boolean testMinY = cameraPos.y() < expandedBox.getCenter().y();
        boolean testMinZ = cameraPos.z() < expandedBox.getCenter().z();

        for (int xIndex = 0; xIndex <= xTotalSteps; xIndex++) {
            double pointX = expandedBox.minX + (xIndex * xStep);
            boolean isOnXFace = (testMinX && xIndex == 0) || (!testMinX && xIndex == xTotalSteps);

            for (int yIndex = 0; yIndex <= yTotalSteps; yIndex++) {
                double pointY = expandedBox.minY + (yIndex * yStep);
                boolean isOnYFace = (testMinY && yIndex == 0) || (!testMinY && yIndex == yTotalSteps);

                for (int zIndex = 0; zIndex <= zTotalSteps; zIndex++) {
                    double pointZ = expandedBox.minZ + (zIndex * zStep);
                    boolean isOnZFace = (testMinZ && zIndex == 0) || (!testMinZ && zIndex == zTotalSteps);

                    if (isOnXFace || isOnYFace || isOnZFace) {
                        Vec3 target = new Vec3(pointX, pointY, pointZ);

                        if (pointAction.test(target)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public AABB getOcclusionBox(AABB entityBox) {
        return entityBox.inflate(Math.max(1, 0.3 * Mth.square(entityBox.getSize())));
    }

    private boolean checkLineOfSight(Vec3 cameraPos, AABB box, BlockGetter level) {
        final boolean[] isVisible = {false};
        AABB expandedBox = getOcclusionBox(box);

        traverseBoxFaces(expandedBox, cameraPos, STEP_SIZE, target -> {
            ClipContext context = new ClipContext(
                    cameraPos,
                    target,
                    ClipContext.Block.VISUAL,
                    ClipContext.Fluid.NONE,
                    CollisionContext.empty()
            );

            if (level.clip(context).getType() == HitResult.Type.MISS) {
                isVisible[0] = true;

                return true;
            }

            return false;
        });

        return isVisible[0];
    }

    private record OcclusionState(boolean isOccluded, Vec3 prevCameraPos, Vec3 prevEntityPos, boolean isDirty) {
    }
}