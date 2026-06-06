package net.meander.subtlyd.world.item;

import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TentItem extends Item {
    private final EntityType<TentEntity> entityType;

    public TentItem(EntityType<TentEntity> entityType, Properties properties) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPlaceContext blockPlaceContext = new BlockPlaceContext(useOnContext);
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Direction direction = useOnContext.getClickedFace();
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        AABB aABB = entityType.getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());

        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            if (level.noCollision(null, aABB) && level.getEntities(null, aABB).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    PostSpawnProcessor<TentEntity> consumer = EntityType.createDefaultStackConfig(serverLevel, itemStack, player);
                    TentEntity tentEntity = entityType.create(serverLevel, consumer, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);

                    if (tentEntity == null) {
                        return InteractionResult.FAIL;
                    }
                    if (player != null){
                        tentEntity.setYRot(player.getYRot() + 180F);
                    }
                    serverLevel.addFreshEntityWithPassengers(tentEntity);
                    level.playSound(null, tentEntity.getX(), tentEntity.getY(), tentEntity.getZ(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    tentEntity.gameEvent(GameEvent.ENTITY_PLACE, player);
                }
                itemStack.consume(1, player);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}
