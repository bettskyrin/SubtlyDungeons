package net.meander.subtlyd.core.cauldron;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.meander.subtlyd.server.level.ServerLevelSD;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.meander.subtlyd.world.level.block.PotionCauldronBlock;
import net.meander.subtlyd.world.level.block.StewCauldronBlock;
import net.meander.subtlyd.world.level.block.entity.PotionCauldronBlockEntity;
import net.meander.subtlyd.world.level.block.entity.StewCauldronBlockEntity;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @see CauldronInteractions
 */
public class CauldronInteractionsSD {
    public static CauldronInteraction.Dispatcher POTION = new CauldronInteraction.Dispatcher();
    public static CauldronInteraction.Dispatcher INCOMPLETE_STEW = new CauldronInteraction.Dispatcher();
    public static CauldronInteraction.Dispatcher STEW = new CauldronInteraction.Dispatcher();

    public static void bootstrap() {
        CauldronInteractions.addDefaultInteractions(POTION);
        CauldronInteractions.addDefaultInteractions(INCOMPLETE_STEW);
        CauldronInteractions.EMPTY.put(Items.SPLASH_POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);
        CauldronInteractions.EMPTY.put(Items.LINGERING_POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);
        CauldronInteractions.EMPTY.put(Items.POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);

        POTION.put(Items.POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);
        POTION.put(Items.SPLASH_POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);
        POTION.put(Items.LINGERING_POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);
        POTION.put(Items.GLASS_BOTTLE, CauldronInteractionsSD::fillBottle);
        POTION.put(Items.ARROW, CauldronInteractionsSD::createTippedArrow);

        INCOMPLETE_STEW.put(Items.BOWL, CauldronInteractionsSD::serveStew);
        STEW.put(Items.BOWL, CauldronInteractionsSD::serveStew);
        ServerLevelSD.registerEvent(putStewIngredients());
    }

    private static ServerLifecycleEvents.ServerStarting putStewIngredients() {
        return (_) -> {
            for (Item ingredient : ItemTagsSD.getItems(ItemTagsSD.STEW_INGREDIENT)) {
                CauldronInteractionsSD.INCOMPLETE_STEW.put(ingredient, CauldronInteractionsSD::fillStewCauldronWithStewIngredient);
                CauldronInteractions.EMPTY.put(ingredient, CauldronInteractionsSD::fillEmptyCauldronWithStewIngredient);
            }
        };
    }

    private static CraftingInput findStewRecipe(StewCauldronBlockEntity blockEntity) {
        int index = 1;
        NonNullList<ItemStack> gridItems = NonNullList.withSize(9, ItemStack.EMPTY);

        gridItems.set(0, new ItemStack(Items.BOWL));

        for (ItemStack ingredient : blockEntity.getIngredients()) {
            if (!ingredient.isEmpty() && index < 9) {
                gridItems.set(index++, ingredient);
            }
        }

        return CraftingInput.of(3, 3, gridItems);
    }

    public static InteractionResult fillEmptyCauldronWithStewIngredient(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (level instanceof ServerLevel) {
            level.setBlockAndUpdate(blockPos, BlocksSD.STEW_CAULDRON.defaultBlockState().setValue(StewCauldronBlock.LEVEL, 3));

            if (level.getBlockEntity(blockPos) instanceof StewCauldronBlockEntity blockEntity) {
                if (blockEntity.addIngredient(itemStack)) {
                    CraftingInput input = findStewRecipe(blockEntity);
                    Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getSynchronizedRecipes().getFirstMatch(RecipeType.CRAFTING, input, level);

                    if (!player.hasInfiniteMaterials()) {
                        itemStack.shrink(1);
                    }

                    if (recipe.isPresent()) {
                        level.setBlockAndUpdate(blockPos, blockState.setValue(StewCauldronBlock.IS_HEAVY_STEW, true));
                    }

                    if (player instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
                    }

                    level.playSound(null, blockPos, SoundEventsSD.STEW_STEWS, SoundSource.BLOCKS, 1.0F, 1.5F);
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public static InteractionResult fillStewCauldronWithStewIngredient(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (level.getBlockEntity(blockPos) instanceof StewCauldronBlockEntity blockEntity) {
            if (!blockState.getValue(StewCauldronBlock.IS_HEAVY_STEW)) {
                if (level instanceof ServerLevel) {
                    if (blockEntity.addIngredient(itemStack)) {
                        CraftingInput input = findStewRecipe(blockEntity);
                        Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getSynchronizedRecipes().getFirstMatch(RecipeType.CRAFTING, input, level);

                        if (!player.hasInfiniteMaterials()) {
                            itemStack.shrink(1);
                        }

                        if (recipe.isPresent()) {
                            level.setBlockAndUpdate(blockPos, blockState.setValue(StewCauldronBlock.IS_HEAVY_STEW, true));
                        }

                        if (player instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
                        }

                        level.playSound(null, blockPos, SoundEventsSD.STEW_STEWS, SoundSource.BLOCKS, 1.0F, 1.5F);
                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult serveStew(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (level.getBlockEntity(blockPos) instanceof StewCauldronBlockEntity blockEntity) {
            if (level instanceof ServerLevel) {
                int currentLevel = blockState.getValue(StewCauldronBlock.LEVEL);
                CraftingInput input = findStewRecipe(blockEntity);
                Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getSynchronizedRecipes().getFirstMatch(RecipeType.CRAFTING, input, level);
                ItemStack resultStew = recipe.map(craftingRecipeRecipeHolder -> craftingRecipeRecipeHolder.value().assemble(input)).orElseGet(() -> new ItemStack(ItemsSD.LIGHT_STEW));

                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, resultStew));
                level.playSound(null, blockPos, SoundEventsSD.STEW_SERVED, SoundSource.BLOCKS, 1.0F, 1.5F);

                if (currentLevel > 1) {
                    level.setBlockAndUpdate(blockPos, blockState.setValue(StewCauldronBlock.LEVEL, currentLevel - 1));
                } else {
                    level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                }
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult fillEmptyCauldronWithPotion(BlockState ignored, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);

        if (potionContents != null) {
            Optional<Holder<Potion>> potionHolder = potionContents.potion();

            if (potionHolder.isPresent()) {
                Holder<Potion> potion = potionHolder.get();
                Item potionType = itemStack.getItem();

                if (potion != Potions.WATER && potion != Potions.AWKWARD && potion != Potions.MUNDANE && potion != Potions.THICK) {
                    level.setBlockAndUpdate(blockPos, BlocksSD.POTION_CAULDRON.defaultBlockState().setValue(PotionCauldronBlock.LEVEL, 2));

                    PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

                    if (blockEntity != null) {
                        blockEntity.setPotion(potion);
                        blockEntity.setPotionType(potionType);

                        if (level instanceof ServerLevel serverLevel) {
                            OptionalInt particleColor = PotionContents.getColorOptional(potion.value().getEffects());

                            if (particleColor.isPresent()) {
                                serverLevel.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                            }

                            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                            level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                            return InteractionResult.SUCCESS_SERVER;
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else if (potion == Potions.WATER) {
                    if (level instanceof ServerLevel) {
                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        level.setBlockAndUpdate(blockPos, Blocks.WATER_CAULDRON.defaultBlockState());
                        level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                        return InteractionResult.SUCCESS_SERVER;
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult fillPotionCauldronWithPotion(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);

        if (potionContents != null) {
            Optional<Holder<Potion>> potionHolder = potionContents.potion();

            if (potionHolder.isPresent()) {
                Holder<Potion> potionInHand = potionHolder.get();
                Item potionTypeInHand = itemStack.getItem();
                int currentLevel = blockState.getValue(PotionCauldronBlock.LEVEL);

                if (potionInHand != Potions.WATER && potionInHand != Potions.AWKWARD && potionInHand != Potions.MUNDANE && potionInHand != Potions.THICK) {
                    if (currentLevel < 6) {
                        PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

                        if (blockEntity != null) {
                            Holder<Potion> potionInCauldron = blockEntity.getPotion();
                            Item potionTypeInCauldron = blockEntity.getPotionType();

                            if (potionInCauldron == potionInHand) {
                                if (potionTypeInCauldron != potionTypeInHand) {
                                    blockEntity.setPotionType(potionTypeInHand);
                                }

                                if (level instanceof ServerLevel serverLevel) {
                                    if (currentLevel <= 4) {
                                        OptionalInt particleColor = PotionContents.getColorOptional(potionInCauldron.value().getEffects());

                                        level.setBlockAndUpdate(blockPos, blockState.setValue(PotionCauldronBlock.LEVEL, currentLevel + 2));

                                        if (particleColor.isPresent()) {
                                            serverLevel.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                                        }

                                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                                        level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                                        level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                                        
                                        return InteractionResult.SUCCESS_SERVER;
                                    }
                                }
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult fillBottle(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (blockState.hasProperty(PotionCauldronBlock.LEVEL)) {
            int currentLevel = blockState.getValue(PotionCauldronBlock.LEVEL);

            if (currentLevel > 0) {
                if (level.getBlockEntity(blockPos) instanceof PotionCauldronBlockEntity blockEntity) {
                    Holder<Potion> potion = blockEntity.getPotion();
                    Item potionType = blockEntity.getPotionType();

                    if (potion != null) {
                        if (level instanceof ServerLevel serverLevel) {
                            OptionalInt particleColor = PotionContents.getColorOptional(potion.value().getEffects());

                            if (particleColor.isPresent()) {
                                serverLevel.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                            }

                            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, PotionContents.createItemStack(potionType, potion)));

                            if (currentLevel > 2) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(PotionCauldronBlock.LEVEL, currentLevel - 2));
                            } else {
                                level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                            }

                            level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                            level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);

                            return InteractionResult.SUCCESS_SERVER;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult createTippedArrow(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand ignored, ItemStack stack) {
        int currentLevel = blockState.getValue(PotionCauldronBlock.LEVEL);

        if (currentLevel > 0) {
            if (level instanceof ServerLevel serverLevel) {
                int tippedArrowCount = Math.min(stack.getCount(), currentLevel * 4);
                int usedCauldronLevels = Mth.ceil((double) tippedArrowCount / 4.0);
                int remainingCauldronLevels = currentLevel - usedCauldronLevels;
                PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

                if (blockEntity != null) {
                    Holder<Potion> potion = blockEntity.getPotion();

                    if (potion != null) {
                        OptionalInt particleColor = PotionContents.getColorOptional(potion.value().getEffects());
                        ItemStack tippedArrows = new ItemStack(Items.TIPPED_ARROW);
                        Inventory inventory = player.getInventory();

                        tippedArrows.setCount(tippedArrowCount);
                        tippedArrows.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));

                        if (particleColor.isPresent()) {
                            serverLevel.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                        }

                        level.setBlockAndUpdate(blockPos, remainingCauldronLevels == 0 ? Blocks.CAULDRON.defaultBlockState() : blockState.setValue(PotionCauldronBlock.LEVEL, remainingCauldronLevels));

                        if (!player.isCreative()) {
                            stack.shrink(tippedArrowCount);
                        }

                        if (!inventory.add(tippedArrows)) {
                            player.drop(tippedArrows, false, Prediction.SERVER_ONLY);
                        }

                        level.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                    }
                }
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}