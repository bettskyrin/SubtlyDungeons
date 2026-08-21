package net.meander.subtlyd.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * @see net.minecraft.advancements.triggers.ConstructBeaconTrigger
 */
public class ConstructConduitTrigger extends SimpleCriterionTrigger<ConstructConduitTrigger.TriggerInstance> {
	@Override
	public Codec<ConstructConduitTrigger.TriggerInstance> codec() {
		return ConstructConduitTrigger.TriggerInstance.CODEC;
	}

	public void trigger(final ServerPlayer player, final int levels) {
		this.trigger(player, t -> t.matches(levels));
	}

	public record TriggerInstance(Optional<Holder<LootItemCondition>> player, MinMaxBounds.Ints level) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<ConstructConduitTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
				i -> i.group(
								LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ConstructConduitTrigger.TriggerInstance::player),
								MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(ConstructConduitTrigger.TriggerInstance::level)
						)
						.apply(i, ConstructConduitTrigger.TriggerInstance::new)
		);

		public static Criterion<ConstructConduitTrigger.TriggerInstance> constructedConduit() {
			return CriteriaTriggersSD.CONSTRUCT_CONDUIT.createCriterion(new ConstructConduitTrigger.TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY));
		}

		public static Criterion<ConstructConduitTrigger.TriggerInstance> constructedConduit(final MinMaxBounds.Ints level) {
			return CriteriaTriggersSD.CONSTRUCT_CONDUIT.createCriterion(new ConstructConduitTrigger.TriggerInstance(Optional.empty(), level));
		}

		public boolean matches(final int levels) {
			return level.matches(levels);
		}
	}
}
