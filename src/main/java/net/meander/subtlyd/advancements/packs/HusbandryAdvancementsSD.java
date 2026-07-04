package net.meander.subtlyd.advancements.packs;

import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider.createPlaceholder;

/**
 * @see net.minecraft.data.advancements.packs.VanillaHusbandryAdvancements
 */
public class HusbandryAdvancementsSD {
    public static void register(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("husbandry/root")))
                .display(
                        Items.CAMPFIRE,
                        Component.translatable("advancements.subtlyd.light_campfire.title"),
                        Component.translatable("advancements.subtlyd.light_campfire.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("try_light_campfire_with_stick", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), Blocks.CAMPFIRE)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), Items.STICK)))
                .save(consumer, Util.identifier("husbandry/light_campfire").toString());
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Util.identifier("husbandry/light_campfire")))
                .display(
                        ItemsSD.LIGHT_STEW,
                        Component.translatable("advancements.subtlyd.make_stew.title"),
                        Component.translatable("advancements.subtlyd.make_stew.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("add_stew_ingredient", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), Blocks.CAULDRON, BlocksSD.STEW_CAULDRON)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), ItemTagsSD.STEW_INGREDIENT)))
                .save(consumer, Util.identifier("husbandry/make_stew").toString());
    }
}
