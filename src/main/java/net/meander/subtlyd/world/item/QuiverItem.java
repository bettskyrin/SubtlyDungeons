package net.meander.subtlyd.world.item;

import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuiverItem extends BundleItem {
    private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
    public static boolean renderingQuiverTooltip = false;

    public QuiverItem(Properties properties) {
        super(properties);
    }

    private static int insertArrow(ItemStack quiver, ItemStack arrowStack) {
        BundleContents contents = quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        List<ItemStack> items = new ArrayList<>();
        contents.items().forEach(template -> items.add(template.create()));

        final int MAX_QUIVER_ARROWS = 256;
        int currentTotal = items.stream().mapToInt(ItemStack::getCount).sum();
        int spaceLeft = MAX_QUIVER_ARROWS - currentTotal;

        if (spaceLeft > 0) {
            int amountToInsert = Math.min(arrowStack.getCount(), spaceLeft);
            int remainingToInsert = amountToInsert;
            ItemStack stackToInsert = arrowStack.copyWithCount(amountToInsert);
            List<ItemStackTemplate> newTemplates = new ArrayList<>();

            for (ItemStack quiverArrows : items) {
                if (ItemStack.isSameItemSameComponents(quiverArrows, stackToInsert)) {
                    int stackSpace = quiverArrows.getMaxStackSize() - quiverArrows.getCount();
                    int transferAmount = Math.min(remainingToInsert, stackSpace);

                    if (transferAmount > 0) {
                        quiverArrows.grow(transferAmount);
                        remainingToInsert -= transferAmount;
                    }

                    if (remainingToInsert <= 0) {
                        break;
                    }
                }
            }

            while (remainingToInsert > 0) {
                int toCreate = Math.min(remainingToInsert, arrowStack.getMaxStackSize());

                items.addFirst(arrowStack.copyWithCount(toCreate));
                remainingToInsert -= toCreate;
            }

            arrowStack.shrink(amountToInsert);

            for (ItemStack item : items) {
                if (!item.isEmpty()) {
                    newTemplates.add(ItemStackTemplate.fromNonEmptyStack(item));
                }
            }

            quiver.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(newTemplates));
            return amountToInsert;
        }
        return 0;
    }

    private static ItemStack removeOneArrowType(ItemStack quiver) {
        BundleContents contents = quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        if (!contents.isEmpty()) {
            List<ItemStack> items = new ArrayList<>();
            contents.items().forEach(template -> items.add(template.create()));

            ItemStack removedStack = items.removeFirst();

            List<ItemStackTemplate> newTemplates = new ArrayList<>();
            for (ItemStack item : items) {
                newTemplates.add(ItemStackTemplate.fromNonEmptyStack(item));
            }

            if (newTemplates.isEmpty()) {
                quiver.remove(DataComponents.BUNDLE_CONTENTS);
            } else {
                quiver.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(newTemplates));
            }

            return removedStack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getActiveArrowAndCycle(ItemStack quiver, ItemStack weapon, LivingEntity holder, boolean forceInfinite) {
        if (quiver.getItem() instanceof QuiverItem) {
            BundleContents arrows = quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS,  BundleContents.EMPTY);

            if (!arrows.isEmpty()) {
                List<ItemStackTemplate> templates = new ArrayList<>(arrows.items());
                ItemStackTemplate activeTemplate = templates.removeFirst();
                ItemStack activeStack = activeTemplate.create();
                ItemStack firedProjectile = activeStack.copyWithCount(1);

                int ammoToUse;

                if (!forceInfinite && !holder.hasInfiniteMaterials()) {
                    if (holder.level() instanceof ServerLevel serverLevel) {
                        ammoToUse = EnchantmentHelper.processAmmoUse(serverLevel, weapon, activeStack, 1);
                    } else {
                        ammoToUse = 0;
                    }
                } else {
                    ammoToUse = 0;
                }

                if (ammoToUse == 0) {
                    firedProjectile.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
                }

                activeStack.shrink(ammoToUse);

                if (!activeStack.isEmpty()) {
                    templates.add(ItemStackTemplate.fromNonEmptyStack(activeStack));
                }

                quiver.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templates));
                return firedProjectile;
            }
        }
        return ItemStack.EMPTY;
    }

    public static Fraction getWeightSafe(final BundleContents contents) {
        return switch (contents.weight()) {
            case DataResult.Success<Fraction> success -> success.value();
            case DataResult.Error<?> _ -> Fraction.getFraction(4, 1);
        };
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack quiver, Slot slot, ClickAction action, Player player) {
        ItemStack slotItem = slot.getItem();

        if (action == ClickAction.PRIMARY && !slotItem.isEmpty()) {
            if (slotItem.is(ItemTags.ARROWS)) {
                int inserted = insertArrow(quiver, slotItem);
                if (inserted > 0) {
                    player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                }
                return true;
            }
        } else if (action == ClickAction.SECONDARY && slotItem.isEmpty()) {
            ItemStack removedArrow = removeOneArrowType(quiver);

            if (!removedArrow.isEmpty()) {
                ItemStack remainder = slot.safeInsert(removedArrow);

                if (remainder.getCount() > 0) {
                    insertArrow(quiver, remainder);
                } else {
                    player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Triggers when the player holds an item and right-clicks it ONTO the Quiver.
     */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack quiver, ItemStack cursor, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (slot.allowModification(player)) {
            if (action == ClickAction.PRIMARY && !cursor.isEmpty()) {
                if (cursor.is(ItemTags.ARROWS)) {
                    int inserted = insertArrow(quiver, cursor);
                    if (inserted > 0) {
                        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                    }
                    return true;
                }
            } else if (action == ClickAction.SECONDARY && cursor.isEmpty()) {
                ItemStack removedArrow = removeOneArrowType(quiver);
                if (!removedArrow.isEmpty()) {
                    player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                    access.set(removedArrow);
                }
                return true;
            } else if (action == ClickAction.SECONDARY && !cursor.isEmpty()) {
                if (cursor.is(ItemTags.ARROWS)) {
                    ItemStack singleArrow = cursor.copyWithCount(1);
                    int inserted = insertArrow(quiver, singleArrow);
                    if (inserted > 0) {
                        cursor.shrink(1);
                        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack quiver = player.getItemInHand(hand);
        Equippable equippable = quiver.get(DataComponents.EQUIPPABLE);

        if (equippable != null && equippable.swappable()) {
            InteractionResult swapResult = equippable.swapWithEquipmentSlot(quiver, player);

            if (swapResult instanceof InteractionResult.Success) {
                return swapResult;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getBarWidth(ItemStack quiver) {
        BundleContents contents = quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        return Math.min(1 + Mth.mulAndTruncate(getWeightSafe(contents), 3), 13);
    }

    @Override
    public int getBarColor(ItemStack quiver) {
        BundleContents contents = quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        return getWeightSafe(contents).compareTo(Fraction.getFraction(4, 1)) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }

    public static float getFullnessDisplay(final ItemStack itemStack) {
        BundleContents contents = itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        return getWeightSafe(contents).floatValue();
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack quiver) {
        renderingQuiverTooltip = true;

        return Optional.of(new BundleTooltip(quiver.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));
    }
}
