package net.meander.subtlyd.data.tags;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagsSD extends KeyTagProvider<DamageType> {
    public DamageTypeTagsSD(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, Registries.DAMAGE_TYPE, completableFuture);
    }

    public static final TagKey<DamageType> CAN_BREAK_TENT = create("can_break_tents");
    public static final TagKey<DamageType> ALWAYS_KILLS_TENT = create("always_kills_tent");
    public static final TagKey<DamageType> BURNS_TENTS = create("burns_tents");
    public static final TagKey<DamageType> IGNITES_TENTS = create("ignites_tents");
    public static final TagKey<DamageType> IS_OCCULT = create("is_occult");

    @SuppressWarnings("unchecked") @Override protected void addTags(HolderLookup.Provider registries) {
        this.tag(CAN_BREAK_TENT).add(DamageTypes.PLAYER_EXPLOSION).add(DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH);
        this.tag(ALWAYS_KILLS_TENT).add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE);
        this.tag(BURNS_TENTS).add(DamageTypes.ON_FIRE);
        this.tag(IGNITES_TENTS).add(DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE);
        this.tag(IS_OCCULT).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS, DamageTypes.WITHER);
    }

    private static TagKey<DamageType> create(String string) {
        return TagKey.create(Registries.DAMAGE_TYPE, Util.identifier(string));
    }
}
