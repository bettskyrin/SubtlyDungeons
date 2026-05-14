package net.meander.subtlyd.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.meander.subtlyd.util.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
     * Modifies only the Uncraftable Potion's color
     */
    private void modifyUncraftablePotion(CachedOutput writer, List<CompletableFuture<?>> futures) {
        final int MAGENTA = 16253176;
        List<String> potionTypes = List.of("potion", "splash_potion", "lingering_potion");
        List<String> blankPotions = List.of("minecraft:water", "minecraft:mundane", "minecraft:awkward", "minecraft:thick");

        for (String potionTypeId : potionTypes) {
            JsonObject itemDefObj = new JsonObject();
            JsonObject currentModelObj = new JsonObject();

            JsonArray cases = new JsonArray();

            currentModelObj.addProperty("type", "minecraft:select");
            currentModelObj.addProperty("property", "minecraft:component");
            currentModelObj.addProperty("component", "minecraft:potion_contents");

            for (String potion : blankPotions) {
                JsonObject potionCase = getBlankPotionCase(potionTypeId, potion);
                cases.add(potionCase);
            }
            currentModelObj.add("cases", cases);

            JsonObject newModelObj = modifyColor(potionTypeId, MAGENTA);

            currentModelObj.add("fallback", newModelObj);
            itemDefObj.add("model", currentModelObj);

            Path vanillaPath = this.itemsPath.json(Identifier.withDefaultNamespace(potionTypeId));
            futures.add(DataProvider.saveStable(writer, itemDefObj, vanillaPath));
        }
    }

    /**
     * Generates the potion bottle archetype JSON files.
     */
    private void setArchetype(CachedOutput writer, List<CompletableFuture<?>> futures) {
        List<String> potionArchetypes = List.of("conical_bottle", "spherical_bottle", "vial_bottle");
        List<String> potionTypePrefixes = List.of("", "splash_", "lingering_");

        for (String archetype : potionArchetypes) {
            for (String prefix : potionTypePrefixes) {
                String fileName = prefix + archetype;
                JsonObject itemDef = getItemDefJson(fileName); // items/...
                JsonObject currentModel = getItemModelJson(fileName, archetype);// models/item/potion/...

                Path itemFilePath = this.itemsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, itemDef, itemFilePath));

                Path modelFilePath = this.modelsPath.json(Util.identifier(fileName));
                futures.add(DataProvider.saveStable(writer, currentModel, modelFilePath));
            }
        }
    }

    /**
     * Modifies a potion's color
     * @param potionId The potion as a string
     * @param colorValue The integer value of the color to set it to
     * @return The modified potion model
     */
    private static @NonNull JsonObject modifyColor(String potionId, int colorValue) {
        JsonObject newModel = new JsonObject();
        newModel.addProperty("type", "minecraft:model");
        newModel.addProperty("model", "minecraft:item/" + potionId);

        JsonArray newTints = new JsonArray();
        JsonObject newTentObj = new JsonObject();

        newTentObj.addProperty("type", "minecraft:potion");
        newTentObj.addProperty("default", colorValue);
        newTints.add(newTentObj);
        newModel.add("tints", newTints);
        return newModel;
    }

    /**
     * Gets the case where a potion has no "potion" value.
     * @param potionTypeId The potion type
     * @param potion The potion
     * @return A blank potion, colored blue
     */
    private static @NonNull JsonObject getBlankPotionCase(String potionTypeId, String potion) {
        final int BLUE = 3694022;
        JsonObject whenNode = new JsonObject();
        JsonObject caseNode = new JsonObject();
        JsonObject modelCase = modifyColor(potionTypeId, BLUE);

        whenNode.addProperty("potion", potion);
        caseNode.add("when", whenNode);
        caseNode.add("model", modelCase);

        return caseNode;
    }

    /**
     * Creates an item model.
     * @param fileName The file name for the model
     * @param archetype The potion archetype.
     * @return The item model as a JsonObject
     */
    private static @NonNull JsonObject getItemModelJson(String fileName, String archetype) {
        JsonObject model = new JsonObject();
        JsonObject textures = new JsonObject();

        model.addProperty("parent", "minecraft:item/generated");

        textures.addProperty("layer0", "subtlyd:item/potion/" + archetype.replace("_bottle", "_overlay"));
        textures.addProperty("layer1", "subtlyd:item/potion/" + fileName);

        model.add("textures", textures);

        return model;
    }

    /**
     * Creates an item definition.
     * @param fileName The file name for the model/item definition
     * @return The item definition as a JsonObject
     */
    private static @NonNull JsonObject getItemDefJson(String fileName) {
        JsonObject itemDefObj = new JsonObject();
        JsonObject modelObj = new JsonObject();
        JsonArray tints = new JsonArray();
        JsonObject tintObj = new JsonObject();

        modelObj.addProperty("type", "minecraft:model");
        modelObj.addProperty("model", "subtlyd:item/potion/" + fileName);

        tintObj.addProperty("type", "minecraft:potion");
        tintObj.addProperty("default", 16253176);
        tints.add(tintObj);

        modelObj.add("tints", tints);
        itemDefObj.add("model", modelObj);

        return itemDefObj;
    }

    @Override
    public String getName() {
        return "Potion Bottle Models & Definitions";
    }
}