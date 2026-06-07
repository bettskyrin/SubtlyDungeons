package net.meander.subtlyd.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.util.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PotionProviderSD implements DataProvider {
    private final PackOutput.PathProvider itemsPath;
    private final PackOutput.PathProvider modelsPath;

    public PotionProviderSD(PackOutput output) {
        this.itemsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item/potion");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        setArchetype(writer, futures);
        modifyUncraftablePotion(writer, futures);
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    /**
     * Modifies the uncraftable potion's texture
     */
    private void modifyUncraftablePotion(CachedOutput writer, List<CompletableFuture<?>> futures) {
        final int MAGENTA = 16253176;
        final int BLUE = 3694022;
        List<String> potionTypes = List.of("potion", "splash_potion", "lingering_potion");
        List<String> blankPotions = List.of("minecraft:water", "minecraft:mundane", "minecraft:awkward", "minecraft:thick");

        for (String potionTypeId : potionTypes) {
            List<PotionCase> cases = new ArrayList<>();
            for (String potion : blankPotions) {
                cases.add(new PotionCase(
                        Map.of("potion", potion),
                        new SimpleModel("minecraft:model", "minecraft:item/" + potionTypeId, List.of(new Tint("minecraft:potion", BLUE)))
                ));
            }

            SelectModel selectModel = new SelectModel(
                    "minecraft:select",
                    "minecraft:component",
                    "minecraft:potion_contents",
                    cases,
                    new SimpleModel("minecraft:model", "minecraft:item/" + potionTypeId, List.of(new Tint("minecraft:potion", MAGENTA)))
            );

            ItemDef<SelectModel> itemDef = new ItemDef<>(selectModel);
            Path vanillaPath = this.itemsPath.json(Identifier.withDefaultNamespace(potionTypeId));

            JsonElement encodedJson = ItemDef.codec(SelectModel.CODEC).encodeStart(JsonOps.INSTANCE, itemDef).getOrThrow();
            futures.add(DataProvider.saveStable(writer, encodedJson, vanillaPath));
        }
    }

    private void setArchetype(CachedOutput writer, List<CompletableFuture<?>> futures) {
        List<String> potionArchetypes = List.of("conical_bottle", "spherical_bottle", "vial_bottle");
        List<String> potionTypePrefixes = List.of("", "splash_", "lingering_");

        for (String archetype : potionArchetypes) {
            for (String prefix : potionTypePrefixes) {
                String fileName = prefix + archetype;

                TextureModel model = new TextureModel(
                        "minecraft:item/generated",
                        Map.of(
                                "layer0", "subtlyd:item/potion/" + archetype.replace("_bottle", "_overlay"),
                                "layer1", "subtlyd:item/potion/" + fileName
                        )
                );

                ItemDef<SimpleModel> itemDef = new ItemDef<>(new SimpleModel(
                        "minecraft:model",
                        "subtlyd:item/potion/" + fileName,
                        List.of(new Tint("minecraft:potion", 16253176))
                ));

                Path modelFilePath = this.modelsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, TextureModel.CODEC.encodeStart(JsonOps.INSTANCE, model).getOrThrow(), modelFilePath));

                Path itemFilePath = this.itemsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, ItemDef.codec(SimpleModel.CODEC).encodeStart(JsonOps.INSTANCE, itemDef).getOrThrow(), itemFilePath));
            }
        }
    }

    @Override
    public String getName() {
        return "Potion Bottle Models & Definitions";
    }

    public record TextureModel(String parent, Map<String, String> textures) {
        public static final Codec<TextureModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("parent").forGetter(TextureModel::parent),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("textures").forGetter(TextureModel::textures)
        ).apply(inst, TextureModel::new));
    }

    public record Tint(String type, int defaultValue) {
        public static final Codec<Tint> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(Tint::type),
                Codec.INT.fieldOf("default").forGetter(Tint::defaultValue)
        ).apply(inst, Tint::new));
    }

    public record SimpleModel(String type, String model, List<Tint> tints) {
        public static final Codec<SimpleModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(SimpleModel::type),
                Codec.STRING.fieldOf("model").forGetter(SimpleModel::model),
                Tint.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(SimpleModel::tints)
        ).apply(inst, SimpleModel::new));
    }

    public record PotionCase(Map<String, String> when, SimpleModel model) {
        public static final Codec<PotionCase> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("when").forGetter(PotionCase::when),
                SimpleModel.CODEC.fieldOf("model").forGetter(PotionCase::model)
        ).apply(inst, PotionCase::new));
    }

    public record SelectModel(String type, String property, String component, List<PotionCase> cases, SimpleModel fallback) {
        public static final Codec<SelectModel> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("type").forGetter(SelectModel::type),
                Codec.STRING.fieldOf("property").forGetter(SelectModel::property),
                Codec.STRING.fieldOf("component").forGetter(SelectModel::component),
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