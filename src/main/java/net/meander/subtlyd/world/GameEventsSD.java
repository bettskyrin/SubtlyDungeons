package net.meander.subtlyd.world;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.util.EventResult;
import net.meander.subtlyd.commands.CommandsSD;
import net.meander.subtlyd.core.CauldronInteractionsSD;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.data.tags.ItemTagsSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.meander.subtlyd.world.level.block.UnlitCampfireFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GameEventsSD {
    private static final List<Item> UNCOMMON_ITEMS = List.of(
            Items.NETHERITE_AXE,
            Items.NETHERITE_HOE,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SPEAR,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_HORSE_ARMOR,
            Items.NETHERITE_NAUTILUS_ARMOR,
            Items.OMINOUS_TRIAL_KEY,
            Items.LINGERING_POTION,
            Items.TIPPED_ARROW
    );
    private static final List<Item> RARE_ITEMS = List.of(
            Items.WITHER_ROSE
    );

    public static void registration() {
        registerBlockEvents();
        registerSleepEvents();
        registerFuelValues();
        registerCommands();
        modifyItemComponents();
        CauldronInteractionsSD.bootstrap();
    }

    private static void registerBlockEvents() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        PlayerBlockBreakEvents.AFTER.register((level, _, pos, state, _) -> {
            if (level.getServer() instanceof MinecraftServer server && server.getGameRules().get(GameRules.BLOCK_DROPS)) {
                if (state.is(BlockTags.CROPS)) {
                    CropBlock crop = (CropBlock) state.getBlock();

                    if (crop.isMaxAge(state)) {
                        ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(pos), UniformInt.of(0, 2).sample(level.getRandom()));
                    }
                }
            }
        });
    }

    /**
     * Enables tent sleeping.
     */
    private static void registerSleepEvents() {
        EntitySleepEvents.ALLOW_BED.register((livingEntity, _, _, _) -> {
            TentEntity tent = TentEntity.getTent(livingEntity, false);

            if (tent != null) {
                return EventResult.ALLOW;
            }
            return EventResult.PASS;
        });
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(LivingEntity::isSleeping);
    }

    /**
     * Registers new fuel materials.
     */
    private static void registerFuelValues() {
        FuelValueEvents.BUILD.register(((builder, _) -> {
            builder.add(ItemsSD.CHARCOAL_BLOCK, 200 * 8 * 10);
            builder.add(ItemTagsSD.TENTS, 200 * 3);
        }));
    }

    /**
     * Registers custom commands.
     */
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) -> CommandsSD.register(dispatcher)));
    }

    /**
     * Modifies item components.
     */
    public static void modifyItemComponents() {
        DefaultItemComponentEvents.MODIFY.register(listener -> {
            listener.modify(UNCOMMON_ITEMS, (builder, _) -> builder.set(DataComponents.RARITY, Rarity.UNCOMMON));
            listener.modify(RARE_ITEMS, (builder, _) -> builder.set(DataComponents.RARITY, Rarity.RARE));

            listener.modify(ItemTagsSD.getItems(ItemTagsSD.LIQUID_CONSUMABLES), (builder, item) -> {
                Consumable oldConsumable = item.components().get(DataComponents.CONSUMABLE);

                if (oldConsumable != null) {
                    builder.set(DataComponents.CONSUMABLE, new Consumable(
                            1.0F,
                            oldConsumable.animation(),
                            oldConsumable.sound(),
                            oldConsumable.hasConsumeParticles(),
                            oldConsumable.onConsumeEffects()
                    ));
                }
            });
            listener.modify(Items.POTION, (builder) -> builder.set(DataComponents.MAX_STACK_SIZE, 16));

            listener.modify(ItemTagsSD.getItems(ItemTagsSD.HAS_MAGIC_LIMIT), (builder, item) -> {
                ItemStack itemStack = item.getDefaultInstance();
                int magicLevel = 0;

                if (itemStack.isEnchanted() || itemStack.is(Items.ENCHANTED_BOOK)) {
                    magicLevel = Mth.ceil((double) (25 - Math.max(ItemStackSD.getEnchantability(itemStack), ItemStackSD.getEnchantabilityFromMap(item))) / 3);

                    if (itemStack.is(Items.ENCHANTED_BOOK)) {
                        magicLevel =  EnchantmentHelperSD.getEnchantmentCost(itemStack);
                    }
                }
                builder.set(DataComponentsSD.MAGIC_LEVEL, magicLevel);
            });
        });
    }
}
