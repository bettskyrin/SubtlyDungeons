package net.meander.subtlyd.world.block;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.core.CauldronInteractionsSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.world.block.entity.PotionCauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public class PotionCauldronBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final MapCodec<PotionCauldronBlock> CODEC = simpleCodec(properties ->
            new PotionCauldronBlock(properties, CauldronInteractionsSD.POTION)
    );
    public static final IntegerProperty POTION_LEVEL = IntegerProperty.create("level", 1, 6);

    public PotionCauldronBlock(Properties properties, CauldronInteraction.Dispatcher interactions) {
        super(properties, interactions);
        this.registerDefaultState(this.stateDefinition.any().setValue(POTION_LEVEL, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POTION_LEVEL);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(POTION_LEVEL) == 6;
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PotionCauldronBlockEntity(pos, state);
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(pos);

        if (blockEntity != null && blockEntity.getPotion() != null) {
            OptionalInt particleColor = PotionContents.getColorOptional(blockEntity.getPotion().value().getEffects());

            if (particleColor.isPresent()) {
                ParticleTypesSD.generatePotionParticles(level, pos, particleColor.getAsInt(), false);
            }
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(Items.CAULDRON);
    }
}
