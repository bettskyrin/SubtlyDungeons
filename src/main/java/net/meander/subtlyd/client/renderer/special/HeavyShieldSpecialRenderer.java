package net.meander.subtlyd.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.model.HeavyShieldModel;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @see net.minecraft.client.renderer.special.ShieldSpecialRenderer
 */
@Environment(EnvType.CLIENT)
public class HeavyShieldSpecialRenderer implements SpecialModelRenderer<DataComponentMap> {
    public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, new Vector3f(1.0F, -1.0F, -1.0F), null);
    public static final SpriteId HEAVY_SHIELD_BASE = new SpriteId(Sheets.SHIELD_SHEET, UtilSD.identifier("entity/shield/heavy_shield_base"));
    public static final SpriteId HEAVY_SHIELD_BASE_NO_PATTERN = new SpriteId(Sheets.SHIELD_SHEET, UtilSD.identifier("entity/shield/heavy_shield_base_nopattern"));
    public static final SpriteId HEAVY_SHIELD_OVERLAY = new SpriteId(Sheets.SHIELD_SHEET, UtilSD.identifier("entity/shield/heavy_shield_overlay"));

    private final SpriteGetter sprites;
    private final HeavyShieldModel model;

    public HeavyShieldSpecialRenderer(final SpriteGetter sprites, final HeavyShieldModel model) {
        this.sprites = sprites;
        this.model = model;
    }

    @Override
    public @Nullable DataComponentMap extractArgument(final ItemStack stack) {
        return stack.immutableComponents();
    }

    @Override
    public void submit(final @Nullable DataComponentMap components, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final int overlayCoords, final boolean hasFoil, final int outlineColor) {
        BannerPatternLayers patterns = components != null ? components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) : BannerPatternLayers.EMPTY;
        DyeColor baseColor = components != null ? components.get(DataComponents.BASE_COLOR) : null;
        boolean hasPatterns = !patterns.layers().isEmpty() || baseColor != null;

        SpriteId base = hasPatterns ? HEAVY_SHIELD_BASE : HEAVY_SHIELD_BASE_NO_PATTERN;

        if (hasFoil && !hasPatterns) {
            submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, RenderTypes.entitySolidGlint(base.atlasLocation()), lightCoords, overlayCoords, -1, sprites.get(base), outlineColor);
        } else {
            submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, base, sprites, outlineColor);
        }

        if (hasPatterns) {
            int orderOffset = patterns.layers().size() + 2;

            BannerRenderer.submitPatterns(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, model, Unit.INSTANCE, false, Objects.requireNonNullElse(baseColor, DyeColor.WHITE), patterns);
            poseStack.pushPose();

            if (hasFoil) {
                submitNodeCollector.order(orderOffset).submitModel(model, Unit.INSTANCE, poseStack, RenderTypes.patternedShieldGlint(), lightCoords, overlayCoords, -1, sprites.get(HEAVY_SHIELD_BASE), outlineColor);
                submitNodeCollector.order(orderOffset + 1).submitModel(model, Unit.INSTANCE, poseStack, RenderTypes.itemCutoutGlint(HEAVY_SHIELD_OVERLAY.atlasLocation()), lightCoords, overlayCoords, -1, sprites.get(HEAVY_SHIELD_OVERLAY), outlineColor);
            } else {
                submitNodeCollector.order(orderOffset).submitModel(model, Unit.INSTANCE, poseStack, RenderTypes.entityCutout(HEAVY_SHIELD_OVERLAY.atlasLocation()), lightCoords, overlayCoords, -1, sprites.get(HEAVY_SHIELD_OVERLAY), outlineColor);
            }

            poseStack.popPose();
        }
    }

    @Override
    public void getExtents(final Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        model.root().getExtentsForGui(poseStack, output);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
        public static final HeavyShieldSpecialRenderer.Unbaked INSTANCE = new HeavyShieldSpecialRenderer.Unbaked();
        public static final MapCodec<HeavyShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override
        public MapCodec<HeavyShieldSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public HeavyShieldSpecialRenderer bake(final BakingContext context) {
            return new HeavyShieldSpecialRenderer(context.sprites(), new HeavyShieldModel(context.entityModelSet().bakeLayer(ModelLayersSD.HEAVY_SHIELD)));
        }
    }
}