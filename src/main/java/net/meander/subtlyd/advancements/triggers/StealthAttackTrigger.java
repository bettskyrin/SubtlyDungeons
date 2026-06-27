package net.meander.subtlyd.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class StealthAttackTrigger extends SimpleCriterionTrigger<StealthAttackTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Called from your damage calculation method to evaluate if the player meets the advancement conditions.
     */
    public void trigger(ServerPlayer player, Entity victim) {
        LootContext victimContext = EntityPredicate.createContext(player, victim);

        trigger(player, instance -> instance.matches(victimContext));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> victim) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("victim").forGetter(TriggerInstance::victim)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> stealthAttack(EntityPredicate.Builder playerPredicate) {
            return CriteriaTriggersSD.STEALTH_ATTACK.createCriterion(
                    new TriggerInstance(Optional.of(EntityPredicate.wrap(playerPredicate)), Optional.empty())
            );
        }

        public boolean matches(LootContext victimContext) {
            return this.victim.isEmpty() || this.victim.get().matches(victimContext);
        }
    }
}