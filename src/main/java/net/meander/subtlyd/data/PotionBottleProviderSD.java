package net.meander.subtlyd.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.meander.subtlyd.util.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PotionBottleProviderSD implements DataProvider {
    private final PackOutput.PathProvider itemsPath;
    private final PackOutput.PathProvider modelsPath;

    public PotionBottleProviderSD(PackOutput output) {
        this.itemsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelsPath = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item/potion");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        List<String> shapes = List.of("conical_bottle", "spherical_bottle", "vial_bottle");
        List<String> prefixes = List.of("", "splash_", "lingering_");

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (String shape : shapes) {
            for (String prefix : prefixes) {
                String fileName = prefix + shape;

                JsonObject itemDef = getItemJson(fileName); // items/...

                Path itemFilePath = this.itemsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, itemDef, itemFilePath));

                JsonObject visualModel = getModelJson(fileName); // models/item/potion/...

                Path modelFilePath = this.modelsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, visualModel, modelFilePath));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private static @NonNull JsonObject getModelJson(String fileName) {
        JsonObject visualModel = new JsonObject();
        visualModel.addProperty("parent", "minecraft:item/generated");

        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", "subtlyd:item/potion/" + fileName.replace("_bottle", "_overlay"));
        textures.addProperty("layer1", "subtlyd:item/potion/" + fileName);

        visualModel.add("textures", textures);
        return visualModel;
    }

    private static @NonNull JsonObject getItemJson(String fileName) {
        JsonObject itemDef = new JsonObject();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("type", "minecraft:model");
        modelObj.addProperty("model", "subtlyd:item/potion/" + fileName);

        JsonArray tints = new JsonArray();
        JsonObject tintObj = new JsonObject();
        tintObj.addProperty("type", "minecraft:potion");
        tintObj.addProperty("default", 16253176);
        tints.add(tintObj);

        modelObj.add("tints", tints);
        itemDef.add("model", modelObj);
        return itemDef;
    }

    @Override
    public String getName() {
        return "Potion Bottle Models & Definitions";
    }
}