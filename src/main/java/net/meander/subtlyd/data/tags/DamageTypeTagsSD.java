package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagsSD extends FabricTagsProvider<DamageType> {
    public static final TagKey<DamageType> CAN_BREAK_TENT = bind("can_break_tents");
    public static final TagKey<DamageType> ALWAYS_KILLS_TENT = bind("always_kills_tent");
    public static final TagKey<DamageType> BURNS_TENTS = bind("burns_tents");
    public static final TagKey<DamageType> IGNITES_TENTS = bind("ignites_tents");
    public static final TagKey<DamageType> IS_OCCULT = bind("is_occult");

    public DamageTypeTagsSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, Registries.DAMAGE_TYPE, completableFuture);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(CAN_BREAK_TENT)
                .add(DamageTypes.PLAYER_EXPLOSION)
                .add(DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH);
        tag(ALWAYS_KILLS_TENT)
                .add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE);
        tag(BURNS_TENTS)
                .add(DamageTypes.ON_FIRE);
        tag(IGNITES_TENTS)
                .add(DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE);
        tag(IS_OCCULT)
                .add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS, DamageTypes.WITHER);
    }

    private static TagKey<DamageType> bind(String string) {
        return TagKey.create(Registries.DAMAGE_TYPE, Util.identifier(string));
    }
}
