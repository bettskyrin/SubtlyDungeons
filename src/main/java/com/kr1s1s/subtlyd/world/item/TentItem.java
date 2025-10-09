package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.entity.EntitySD;
import com.kr1s1s.subtlyd.world.entity.TentEntitySD;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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

import java.util.function.Consumer;

public class TentItem extends Item {
    public TentItem(EntityType<TentEntitySD> entityType, Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPlaceContext blockPlaceContext = new BlockPlaceContext(useOnContext);
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        AABB aABB = EntitySD.TENT.getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());

        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            if (level.noCollision(null, aABB) && level.getEntities(null, aABB).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    Consumer<TentEntitySD> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, useOnContext.getPlayer());
                    TentEntitySD tentEntity = EntitySD.TENT.create(serverLevel, consumer, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);

                    if (tentEntity == null) {
                        return InteractionResult.FAIL;
                    }
                    float f = useOnContext.getHorizontalDirection().get2DDataValue() + Mth.floor((Mth.wrapDegrees(useOnContext.getRotation()) / 45.0F) * 45.0F - 90.0F);
                    tentEntity.lookAt(EntityAnchorArgument.Anchor.EYES, useOnContext.getPlayer().getEyePosition());
                    tentEntity.setYRot(tentEntity.getYRot() - 135.0F);
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
