package net.meander.subtlyd.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class StealthAttackTrigger extends SimpleCriterionTrigger<StealthAttackTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(final ServerPlayer player, final Entity victim) {
        LootContext victimContext = EntityPredicate.createContext(player, victim);

        trigger(player, t -> t.matches(victimContext));
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> victim) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                LootItemCondition.CODEC.optionalFieldOf("victim").forGetter(TriggerInstance::victim)
        ).apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> stealthAttack(final EntityPredicate.Builder playerPredicate) {
            return CriteriaTriggersSD.STEALTH_ATTACK.createCriterion(
                    new TriggerInstance(Optional.of(EntityPredicate.wrap(playerPredicate)), Optional.empty())
            );
        }

        public boolean matches(final LootContext victimContext) {
            return victim.isEmpty() || victim.get().value().test(victimContext);
        }
    }
}