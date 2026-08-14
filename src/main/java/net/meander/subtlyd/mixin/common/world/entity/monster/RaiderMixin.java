package net.meander.subtlyd.mixin.common.world.entity.monster;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("DataFlowIssue")
@Mixin(Raider.class)
public class RaiderMixin {
    /**
     * Gives pillager captains a 3-minute resistance boost based on raid difficulty level.
     */
    private void setBoost(final DifficultyInstance difficulty) {
        final Raider raider = (Raider) (Object) this;

        if (raider.isCaptain() && getRaidDifficulty(difficulty) >= 4) {
            raider.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 3600, getCaptainBonus(difficulty)));
        }
    }

    /**
     * Determines the pillager captain resistance boost level.
     * @return The level of resistance to grant
     */
    private int getCaptainBonus(final DifficultyInstance difficulty) {
        if (getRaidDifficulty(difficulty) >= 7) {
            return 1;
        }

        return 0;
    }

    /**
     * Calculates the raid difficulty level with the formula: Level difficulty + Raid Omen Level + Raid wave
     * @return The raid difficulty level
     */
    private int getRaidDifficulty(final DifficultyInstance difficulty) {
        final Raider raider = (Raider) (Object) this;

        if (raider.hasActiveRaid()) {
            return difficulty.getDifficulty().getId() + raider.getCurrentRaid().getRaidOmenLevel() + raider.getWave();
        }

        return 0;
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void finalizeSpawn(final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, final @Nullable SpawnGroupData groupData, CallbackInfoReturnable<Object> cir) {
        spawnArsonistRaiders(level, difficulty);
    }

    /**
     * Determines if the raid difficulty is high enough to allow pillagers with bow enchanted crossbows to spawn in a raid.
     * The raid difficulty must reach level 10 before this can happen.
     * i.e. Normal Difficulty with a raid omen of at least V, on wave 3 or higher OR Hard Difficulty with a raid omen of at least 4, on wave 3 or higher
     */
    private void spawnArsonistRaiders(final ServerLevelAccessor level, final DifficultyInstance difficulty) {
        final int DIFFICULTY_THRESHOLD = 10;
        final int WAVE_THRESHOLD = 3;
        final Raider raider = (Raider) (Object) this;
        ItemStack mainHandItem = raider.getItemBySlot(EquipmentSlot.MAINHAND);

        if (getRaidDifficulty(difficulty) >= DIFFICULTY_THRESHOLD) {
            final float SPAWN_ARSONIST_CHANCE = 0.0625F * (getRaidDifficulty(difficulty) - WAVE_THRESHOLD) * raider.getCurrentRaid().getEnchantOdds();

            if (mainHandItem.isEnchanted() && mainHandItem.is(Items.CROSSBOW)) {
                try {
                    Optional<Enchantment> flameEnchantment = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOptional(Enchantments.FLAME);

                    if (raider.getRandom().nextFloat() < SPAWN_ARSONIST_CHANCE && flameEnchantment.isPresent()) {
                        EnchantmentHelper.setEnchantments(mainHandItem, ItemEnchantments.EMPTY);
                        mainHandItem.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(flameEnchantment.get()), 1);
                    }
                } catch (Exception e) {
                    UtilSD.LOGGER.error("Failed to set enchantment: {}", e.getMessage());
                }
            }

            setBoost(difficulty);
        }
    }

    @Inject(method = "pickUpItem", at = @At("RETURN"))
    private void pickUpItem(ServerLevel level, ItemEntity entity, CallbackInfo ci) {
        setBoost(level.getCurrentDifficultyAt(entity.blockPosition()));
    }
}
