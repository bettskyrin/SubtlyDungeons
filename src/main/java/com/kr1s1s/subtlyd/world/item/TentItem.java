package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.world.entity.TentEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TentItem extends Item {
    private final EntityType<TentEntity> entityType;
    public TentItem(EntityType<TentEntity> entityType, Properties properties) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPlaceContext blockPlaceContext = new BlockPlaceContext(useOnContext);
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        AABB aABB = this.entityType.getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());

        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            if (level.noCollision(null, aABB) && level.getEntities(null, aABB).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    Consumer<TentEntity> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, useOnContext.getPlayer());
                    TentEntity tentEntity = this.entityType.create(serverLevel, consumer, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);

                    if (tentEntity == null) {
                        return InteractionResult.FAIL;
                    }
                    if (useOnContext.getPlayer() != null){
                        tentEntity.setYRot(useOnContext.getPlayer().getYRot() + 180F);
                    }
                    serverLevel.addFreshEntityWithPassengers(tentEntity);
                    level.playSound(null, tentEntity.getX(), tentEntity.getY(), tentEntity.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    tentEntity.gameEvent(GameEvent.ENTITY_PLACE, useOnContext.getPlayer());
                }
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
