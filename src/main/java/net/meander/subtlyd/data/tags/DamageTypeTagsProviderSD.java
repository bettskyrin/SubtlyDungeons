package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.tags.DamageTypeTagsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.DamageTypeTagsProvider
 */
public class DamageTypeTagsProviderSD extends FabricTagsProvider<DamageType> {
    public DamageTypeTagsProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, Registries.DAMAGE_TYPE, completableFuture);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(DamageTypeTagsSD.CAN_BREAK_TENT)
                .add(DamageTypes.PLAYER_EXPLOSION)
                .add(DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH);
        tag(DamageTypeTagsSD.ALWAYS_KILLS_TENT)
                .add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE);
        tag(DamageTypeTagsSD.BURNS_TENTS)
                .add(DamageTypes.ON_FIRE);
        tag(DamageTypeTagsSD.IGNITES_TENTS)
                .add(DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE);
        tag(DamageTypeTagsSD.IS_OCCULT)
                .add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS, DamageTypes.WITHER);
    }
}
