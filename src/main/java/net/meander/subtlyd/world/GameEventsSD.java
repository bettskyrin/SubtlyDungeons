package net.meander.subtlyd.world;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.util.EventResult;
import net.meander.subtlyd.commands.CommandsSD;
import net.meander.subtlyd.core.cauldron.CauldronInteractionsSD;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.meander.subtlyd.world.level.block.UnlitCampfireFunction;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
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
        registerCompostables();
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

    private static void registerCompostables() {
        CompostableRegistry.INSTANCE.add(ItemsSD.APPLE_PIE, 1.0F);
    }

    private static void registerBlockEvents() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());

        UseBlockCallback.EVENT.register(((player, level, hand, hitResult) -> { // Cauldron Stews
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(ItemTagsSD.STEW_INGREDIENT)) {
                if (state.is(Blocks.CAULDRON)) {
                    InteractionResult result = CauldronInteractionsSD.fillEmptyCauldronWithStewIngredient(state, level, pos, player, stack);
                    if (result.consumesAction()) {
                        return result;
                    }
                } else if (state.is(BlocksSD.STEW_CAULDRON)) {
                    InteractionResult result = CauldronInteractionsSD.fillStewCauldronWithStewIngredient(state, level, pos, player, stack);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
            }

            return InteractionResult.PASS;
        }));

        PlayerBlockBreakEvents.AFTER.register((level, _, pos, state, _) -> { // Crop XP
            if (level.getServer() instanceof MinecraftServer server) {
                if (server.getGameRules().get(GameRules.BLOCK_DROPS)) {
                    if (state.is(BlockTags.CROPS)) {
                        CropBlock crop = (CropBlock) state.getBlock();

                        if (crop.isMaxAge(state)) {
                            ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(pos), UniformInt.of(0, 2).sample(level.getRandom()));
                        }
                    }
                }

                if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                    int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                    if (layers > 0) {
                        BlockState snowState = (layers == 8) ? Blocks.SNOW_BLOCK.defaultBlockState() : Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, layers);
                        level.setBlock(pos, snowState, 3);
                    }
                }
            }
        });

        BlockEvents.USE_ITEM_ON.register(((_, blockState, level, blockPos, player, interactionHand, _) -> {
            if (blockState.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                SimpleSnowloggedBlock snowloggedBlock = (SimpleSnowloggedBlock) blockState.getBlock();
                InteractionResult result = snowloggedBlock.trySnowlog(blockState, level, blockPos, player, interactionHand);

                if (result.consumesAction()) {
                    return result;
                }
            }
            return null;
        }));

        AttackBlockCallback.EVENT.register((player, level, hand, pos, _) -> {
            BlockState state = level.getBlockState(pos);

            if (state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS)) {
                int layers = state.getValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);

                if (layers > 0) {
                    if (!level.isClientSide()) {
                        ItemStack tool = player.getItemInHand(hand);

                        if (tool.is(ItemTags.SHOVELS) && !player.hasInfiniteMaterials()) {
                            Block.popResource(level, pos, new ItemStack(Items.SNOWBALL, layers));
                            tool.hurtAndBreak(1, player, player.getEquipmentSlotForItem(tool));
                        }

                        level.setBlock(pos, state.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, 0), 3);
                        level.playSound(null, pos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
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
}
