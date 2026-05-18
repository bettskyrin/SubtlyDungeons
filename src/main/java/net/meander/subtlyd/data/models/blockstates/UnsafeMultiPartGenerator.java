package net.meander.subtlyd.data.models.blockstates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.block.dispatch.multipart.Condition;
import net.minecraft.client.renderer.block.dispatch.multipart.Selector;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @see MultiPartGenerator
 */
public class UnsafeMultiPartGenerator implements BlockModelDefinitionGenerator {
    private final Block block;
    private final List<UnsafeMultiPartGenerator.Entry> parts = new ArrayList<>();

    private UnsafeMultiPartGenerator(final Block block) {
        this.block = block;
    }

    @Override
    public Block block() {
        return this.block;
    }

    public static UnsafeMultiPartGenerator multiPart(final Block block) {
        return new UnsafeMultiPartGenerator(block);
    }

    public UnsafeMultiPartGenerator with(final MultiVariant variants) {
        this.parts.add(new UnsafeMultiPartGenerator.Entry(Optional.empty(), variants));
        return this;
    }

    public UnsafeMultiPartGenerator with(final Condition condition, final MultiVariant variants) {
        this.parts.add(new UnsafeMultiPartGenerator.Entry(Optional.of(condition), variants));
        return this;
    }

    public UnsafeMultiPartGenerator with(final ConditionBuilder condition, final MultiVariant variants) {
        return this.with(condition.build(), variants);
    }

    @Override
    public BlockStateModelDispatcher create() {
        return new BlockStateModelDispatcher(
                Optional.empty(), Optional.of(new BlockStateModelDispatcher.MultiPartDefinition(this.parts.stream().map(UnsafeMultiPartGenerator.Entry::toUnbaked).toList()))
        );
    }

    @Environment(EnvType.CLIENT)
    private record Entry(Optional<Condition> condition, MultiVariant variants) {
        public Selector toUnbaked() {
            return new Selector(this.condition, this.variants.toUnbaked());
        }
    }
}