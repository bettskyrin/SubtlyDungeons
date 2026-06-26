package net.meander.subtlyd.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelData;

import java.util.Optional;

public class SleptInTentTrigger extends SimpleCriterionTrigger<SleptInTentTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, instance -> instance.matches(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Integer> minDistanceFromBed) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), Codec.INT.optionalFieldOf("min_distance").forGetter(TriggerInstance::minDistanceFromBed)).apply(instance, TriggerInstance::new)
        );

        public boolean matches(ServerPlayer player) {
            if (minDistanceFromBed.isPresent()) {
                ServerPlayer.RespawnConfig respawnConfig = player.getRespawnConfig();

                if (respawnConfig != null) {
                    LevelData.RespawnData respawn = respawnConfig.respawnData();
                    BlockPos pos = respawn.pos();

                    if (player.level().dimension() != respawn.dimension()) {
                        return true;
                    }

                    return !player.blockPosition().closerThan(pos, minDistanceFromBed.get());
                }
            }
            return true;
        }

        public static Criterion<TriggerInstance> campedFarAway(int minimumDistance) {
            return CriteriaTriggersSD.SLEPT_IN_TENT.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(minimumDistance)));
        }
    }
}
