package net.meander.subtlyd.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public record IsWetCondition() implements LootItemCondition {
    public static final MapCodec<IsWetCondition> CODEC = MapCodec.unit(new IsWetCondition());

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity target = lootContext.getOptionalParameter(LootContextParams.THIS_ENTITY);

        if (target != null) {
            return target.isInWaterOrRain();
        }

        return false;
    }
}
