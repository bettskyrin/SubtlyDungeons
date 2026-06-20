package net.meander.subtlyd.core.cauldron;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.block.StewCauldronBlock;
import net.meander.subtlyd.world.block.entity.PotionCauldronBlockEntity;
import net.meander.subtlyd.world.block.entity.StewCauldronBlockEntity;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;
import java.util.OptionalInt;

public class CauldronInteractionsSD {
    public static CauldronInteraction.Dispatcher POTION = new CauldronInteraction.Dispatcher();
    public static CauldronInteraction.Dispatcher INCOMPLETE_STEW = new CauldronInteraction.Dispatcher();
    public static CauldronInteraction.Dispatcher STEW = new CauldronInteraction.Dispatcher();

    public static void bootstrap() {
        CauldronInteractions.EMPTY.put(Items.SPLASH_POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);
        CauldronInteractions.EMPTY.put(Items.LINGERING_POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);
        CauldronInteractions.EMPTY.put(Items.POTION, CauldronInteractionsSD::fillEmptyCauldronWithPotion);

        POTION.put(Items.POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);
        POTION.put(Items.SPLASH_POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);
        POTION.put(Items.LINGERING_POTION, CauldronInteractionsSD::fillPotionCauldronWithPotion);

        POTION.put(Items.WATER_BUCKET, CauldronInteractionsSD::fillWithBucketContents);
        POTION.put(Items.LAVA_BUCKET, CauldronInteractionsSD::fillWithBucketContents);

        POTION.put(Items.GLASS_BOTTLE, CauldronInteractionsSD::fillBottle);
        POTION.put(Items.ARROW, CauldronInteractionsSD::createTippedArrow);

        INCOMPLETE_STEW.put(Items.BOWL, CauldronInteractionsSD::serveStew);
        STEW.put(Items.BOWL, CauldronInteractionsSD::serveStew);
    }

    private static CraftingInput findStewRecipe(StewCauldronBlockEntity blockEntity) {
        NonNullList<ItemStack> gridItems = NonNullList.withSize(9, ItemStack.EMPTY);

        gridItems.set(0, new ItemStack(Items.BOWL));

        int index = 1;
        for (ItemStack ingredient : blockEntity.getIngredients()) {
            if (!ingredient.isEmpty() && index < 9) {
                gridItems.set(index++, ingredient);
            }
        }

        return CraftingInput.of(3, 3, gridItems);
    }

    public static InteractionResult fillEmptyCauldronWithStewIngredient(BlockState blockState, Level level, BlockPos blockPos, Player player, ItemStack itemStack) {
        if (!level.isClientSide()) {
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

                    level.playSound(null, blockPos, SoundEventsSD.STEW_STEWS, SoundSource.BLOCKS, 1.0F, 1.5F);
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public static InteractionResult fillStewCauldronWithStewIngredient(BlockState blockState, Level level, BlockPos blockPos, Player player, ItemStack itemStack) {
        if (level.getBlockEntity(blockPos) instanceof StewCauldronBlockEntity blockEntity) {
            if (!blockState.getValue(StewCauldronBlock.IS_HEAVY_STEW)) {
                if (!level.isClientSide()) {
                    if (blockEntity.addIngredient(itemStack)) {
                        CraftingInput input = findStewRecipe(blockEntity);
                        Optional<RecipeHolder<CraftingRecipe>> recipe = level.recipeAccess().getSynchronizedRecipes().getFirstMatch(RecipeType.CRAFTING, input, level);

                        if (!player.hasInfiniteMaterials()) {
                            itemStack.shrink(1);
                        }

                        if (recipe.isPresent()) {
                            level.setBlockAndUpdate(blockPos, blockState.setValue(StewCauldronBlock.IS_HEAVY_STEW, true));
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
            if (!level.isClientSide()) {
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
        Identifier potionTypeResource = Identifier.tryParse(itemStack.getItem().toString());
        PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);

        if (potionTypeResource != null && potionContents != null) {
            Optional<Holder<Potion>> potionHolder = potionContents.potion();

            if (potionHolder.isPresent()) {
                Holder<Potion> potion = potionHolder.get();
                String potionType = potionTypeResource.toString();

                if (potion != Potions.WATER && potion != Potions.AWKWARD && potion != Potions.MUNDANE && potion != Potions.THICK) {
                    level.setBlockAndUpdate(blockPos, BlocksSD.POTION_CAULDRON.defaultBlockState().setValue(PotionCauldronBlock.POTION_LEVEL, 2));
                    PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

                    if (blockEntity != null) {
                        blockEntity.setPotion(potion);
                        blockEntity.setPotionType(potionType);

                        if (!level.isClientSide()) {
                            OptionalInt particleColor = PotionContents.getColorOptional(potion.value().getEffects());

                            if (particleColor.isPresent()) {
                                ((ServerLevel) level).sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                            }
                            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                            level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                            return InteractionResult.SUCCESS_SERVER;
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else if (potion == Potions.WATER) {
                    if (!level.isClientSide()) {
                        level.setBlockAndUpdate(blockPos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                        return InteractionResult.SUCCESS_SERVER;
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult fillPotionCauldronWithPotion(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        Identifier potionTypeResource = Identifier.tryParse(itemStack.getItem().toString());
        PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);

        if (potionTypeResource != null && potionContents != null) {
            Optional<Holder<Potion>> potionHolder = potionContents.potion();

            if (potionHolder.isPresent()) {
                Holder<Potion> potionInHand = potionHolder.get();
                String potionTypeInHand = potionTypeResource.toString();
                int currentLevel = blockState.getValue(PotionCauldronBlock.POTION_LEVEL);

                if (potionInHand != Potions.WATER && potionInHand != Potions.AWKWARD && potionInHand != Potions.MUNDANE && potionInHand != Potions.THICK) {
                    if (currentLevel < 6) {
                        PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

                        if (blockEntity != null) {
                            Holder<Potion> potionInCauldron = blockEntity.getPotion();
                            String potionTypeInCauldron = blockEntity.getPotionType();

                            if (potionInCauldron == potionInHand) {
                                if (!potionTypeInCauldron.equals(potionTypeInHand)) {
                                    blockEntity.setPotionType(potionTypeInHand);
                                }

                                if (!level.isClientSide()) {
                                    if (currentLevel <= 4) {
                                        OptionalInt particleColor = PotionContents.getColorOptional(potionInCauldron.value().getEffects());

                                        level.setBlockAndUpdate(blockPos, blockState.setValue(PotionCauldronBlock.POTION_LEVEL, currentLevel + 2));

                                        if (particleColor.isPresent()) {
                                            ((ServerLevel) level).sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
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

    private static InteractionResult fillWithBucketContents(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        return nonPotionInteractions(level, blockPos, player, interactionHand, itemStack);
    }

    private static InteractionResult fillBottle(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        int currentLevel = blockState.getValue(PotionCauldronBlock.POTION_LEVEL);

        if (currentLevel > 0) {
            PotionCauldronBlockEntity blockEntity = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

            if (blockEntity != null) {
                Holder<Potion> potion = blockEntity.getPotion();
                Identifier potionTypeIdentifier = Identifier.tryParse(blockEntity.getPotionType());

                if (potion != null && potionTypeIdentifier != null) {
                    if (!level.isClientSide()) {
                        OptionalInt particleColor = PotionContents.getColorOptional(potion.value().getEffects());
                        Optional<Holder.Reference<Item>> potionTypeHolder = BuiltInRegistries.ITEM.get(potionTypeIdentifier);

                        if (particleColor.isPresent()) {
                            ((ServerLevel) level).sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                        }

                        if (potionTypeHolder.isPresent()) {
                            Item potionType = potionTypeHolder.get().value();

                            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, PotionContents.createItemStack(potionType, potion)));

                            if (currentLevel > 2) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(PotionCauldronBlock.POTION_LEVEL, currentLevel - 2));
                            } else {
                                level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                            }

                            level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                            level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                        }
                        return InteractionResult.SUCCESS_SERVER;
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult createTippedArrow(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand ignored, ItemStack stack) {
        int currentLevel = blockState.getValue(PotionCauldronBlock.POTION_LEVEL);

        if (currentLevel > 0) {
            if (!level.isClientSide()) {
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
                            ((ServerLevel) level).sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, particleColor.getAsInt()), blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                        }

                        level.setBlockAndUpdate(blockPos, remainingCauldronLevels == 0 ? Blocks.CAULDRON.defaultBlockState() : blockState.setValue(PotionCauldronBlock.POTION_LEVEL, remainingCauldronLevels));

                        if (!player.isCreative()) {
                            stack.shrink(tippedArrowCount);
                        }

                        if (!inventory.add(tippedArrows)) {
                            player.drop(tippedArrows, false);
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

    private static InteractionResult nonPotionInteractions(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        if (!level.isClientSide()) {
            ItemStack itemResult;

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.POOF, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
            }
            level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());

            if (itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.LAVA_BUCKET)) {
                itemResult = new ItemStack(Items.BUCKET);
            } else {
                itemResult = new ItemStack(Items.GLASS_BOTTLE);
            }

            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, itemResult));
            level.playSound(null, blockPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }
}