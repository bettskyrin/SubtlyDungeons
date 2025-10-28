package com.kr1s1s.subtlyd.data.tags;

import com.kr1s1s.subtlyd.SubtlyDungeons;
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

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(CAN_BREAK_TENT).add(DamageTypes.PLAYER_EXPLOSION).add(DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH);
        this.tag(ALWAYS_KILLS_TENT).add(DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE);
    }

    private static TagKey<DamageType> create(String string) {
        return TagKey.create(Registries.DAMAGE_TYPE, SubtlyDungeons.resourceLocation(string));
    }
}
