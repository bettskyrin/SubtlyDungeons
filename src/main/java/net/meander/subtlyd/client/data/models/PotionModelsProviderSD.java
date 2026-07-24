package net.meander.subtlyd.client.data.models;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.client.data.models.ModelProvider
 */
public class PotionModelsProviderSD implements DataProvider {
    private final PackOutput.PathProvider itemsPath;
    private final PackOutput.PathProvider modelsPath;
    private final CompletableFuture<HolderLookup.Provider> completableFuture;
    public static final String ARCHETYPE_CONICAL = "conical_bottle";
    public static final String ARCHETYPE_SPHERICAL = "spherical_bottle";
    public static final String ARCHETYPE_VIAL = "vial_bottle";

    public PotionModelsProviderSD(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        itemsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        modelsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item/potion");
        completableFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return completableFuture.thenCompose(_ -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            List<Item> potionTypes = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);

            setArchetype(writer, futures, potionTypes);
            modifyUncraftablePotion(writer, futures, potionTypes);

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    private void modifyUncraftablePotion(CachedOutput writer, List<CompletableFuture<?>> futures, List<Item> potionTypes) {
        final int magenta = 0xF800F8;
        final int blue = 0x385DC6;

        List<Potion> blankPotions = List.of(Potions.WATER.value(), Potions.MUNDANE.value(), Potions.AWKWARD.value(), Potions.THICK.value());
        Identifier selectType = Identifier.withDefaultNamespace("select");
        Identifier component = Identifier.withDefaultNamespace("component");
        Identifier potionContents = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(DataComponents.POTION_CONTENTS);
        Identifier modelType = Identifier.withDefaultNamespace("model");
        Identifier potionTint = Identifier.withDefaultNamespace("potion");

        for (Item potionItem : potionTypes) {
            Identifier potionId = BuiltInRegistries.ITEM.getKey(potionItem);
            List<PotionCase> cases = new ArrayList<>();
            Identifier baseModelId = Identifier.withDefaultNamespace("item/" + potionId.getPath());

            for (Potion blankPotion : blankPotions) {
                Identifier blankPotionId = BuiltInRegistries.POTION.getKey(blankPotion);

                if (blankPotionId != null) {
                    cases.add(new PotionCase(Map.of("potion", blankPotionId), new SimpleModel(modelType, baseModelId, List.of(new Tint(potionTint, blue)))));
                }
            }

            SelectModel selectModel = new SelectModel(selectType, component, potionContents, cases, new SimpleModel(modelType, baseModelId, List.of(new Tint(potionTint, magenta))));
            ItemDef<SelectModel> itemDef = new ItemDef<>(selectModel);
            Path vanillaPath = itemsPath.json(potionId);
            JsonElement encodedJson = ItemDef.codec(SelectModel.CODEC).encodeStart(JsonOps.INSTANCE, itemDef).getOrThrow(IllegalStateException::new);

            futures.add(DataProvider.saveStable(writer, encodedJson, vanillaPath));
        }
    }

    private void setArchetype(CachedOutput writer, List<CompletableFuture<?>> futures, List<Item> potionTypes) {
        List<String> potionArchetypes = List.of(ARCHETYPE_CONICAL, ARCHETYPE_SPHERICAL, ARCHETYPE_VIAL);

        Identifier generatedParent = Identifier.withDefaultNamespace("item/generated");
        Identifier modelType = Identifier.withDefaultNamespace("model");
        Identifier potionTint = Identifier.withDefaultNamespace("potion");

        for (String archetype : potionArchetypes) {
            for (Item potionItem : potionTypes) {
                Identifier potionId = BuiltInRegistries.ITEM.getKey(potionItem);
                String prefix = potionId.getPath().replace("potion", "");
                String fileName = prefix + archetype;

                TextureModel model = new TextureModel(
                        generatedParent,
                        Map.of(
                                TextureSlot.LAYER0.getId(), UtilSD.identifier("item/potion/" + archetype.replace("_bottle", "_overlay")),
                                TextureSlot.LAYER1.getId(), UtilSD.identifier("item/potion/" + fileName)
                        )
                );

                ItemDef<SimpleModel> itemDef = new ItemDef<>(new SimpleModel(modelType, UtilSD.identifier("item/potion/" + fileName), List.of(new Tint(potionTint, 16253176))));

                Path modelFilePath = modelsPath.json(UtilSD.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, TextureModel.CODEC.encodeStart(JsonOps.INSTANCE, model).getOrThrow(IllegalStateException::new), modelFilePath));

                Path itemFilePath = itemsPath.json(UtilSD.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, ItemDef.codec(SimpleModel.CODEC).encodeStart(JsonOps.INSTANCE, itemDef).getOrThrow(IllegalStateException::new), itemFilePath));
            }
        }
    }

    @Override
    public String getName() {
        return "Potion Models Provider";
    }

    public record TextureModel(Identifier parent, Map<String, Identifier> textures) {
        public static final Codec<TextureModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Identifier.CODEC.fieldOf("parent").forGetter(TextureModel::parent),
                Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("textures").forGetter(TextureModel::textures)
        ).apply(inst, TextureModel::new));
    }

    public record Tint(Identifier type, int defaultValue) {
        public static final Codec<Tint> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Identifier.CODEC.fieldOf("type").forGetter(Tint::type),
                Codec.INT.fieldOf("default").forGetter(Tint::defaultValue)
        ).apply(inst, Tint::new));
    }

    public record SimpleModel(Identifier type, Identifier model, List<Tint> tints) {
        public static final Codec<SimpleModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Identifier.CODEC.fieldOf("type").forGetter(SimpleModel::type),
                Identifier.CODEC.fieldOf("model").forGetter(SimpleModel::model),
                Tint.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(SimpleModel::tints)
        ).apply(inst, SimpleModel::new));
    }

    public record PotionCase(Map<String, Identifier> when, SimpleModel model) {
        public static final Codec<PotionCase> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, Identifier.CODEC).fieldOf("when").forGetter(PotionCase::when),
                SimpleModel.CODEC.fieldOf("model").forGetter(PotionCase::model)
        ).apply(inst, PotionCase::new));
    }

    public record SelectModel(Identifier type, Identifier property, Identifier component, List<PotionCase> cases, SimpleModel fallback) {
        public static final Codec<SelectModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Identifier.CODEC.fieldOf("type").forGetter(SelectModel::type),
                Identifier.CODEC.fieldOf("property").forGetter(SelectModel::property),
                Identifier.CODEC.fieldOf("component").forGetter(SelectModel::component),
                PotionCase.CODEC.listOf().fieldOf("cases").forGetter(SelectModel::cases),
                SimpleModel.CODEC.fieldOf("fallback").forGetter(SelectModel::fallback)
        ).apply(inst, SelectModel::new));
    }

    public record ItemDef<T>(T model) {
        public static <T> Codec<ItemDef<T>> codec(Codec<T> modelCodec) {
            return RecordCodecBuilder.create(inst -> inst.group(
                    modelCodec.fieldOf("model").forGetter(ItemDef::model)
            ).apply(inst, ItemDef::new));
        }
    }
}