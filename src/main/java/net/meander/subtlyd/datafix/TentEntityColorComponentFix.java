package net.meander.subtlyd.datafix;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.util.datafix.fixes.References;

import java.util.Optional;

public class TentEntityColorComponentFix extends DataFix {
    public TentEntityColorComponentFix(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> inputType = getInputSchema().getType(References.ENTITY);
        Type<?> outputType = getOutputSchema().getType(References.ENTITY);

        return writeFixAndRead("TentEntityColorComponentFix", inputType, outputType, dynamic -> {
            Optional<String> idOpt = dynamic.get("id").asString().result();

            if (idOpt.isPresent()) {
                String oldId = idOpt.get();
                String prefix = UtilSD.NAMESPACE + ":";
                String suffix = "_tent_entity";

                if (oldId.startsWith(prefix) && oldId.endsWith(suffix)) {
                    String color = oldId.substring(prefix.length(), oldId.indexOf(suffix));
                    String newId = prefix + "tent";

                    return dynamic.set("id", dynamic.createString(newId)).set("color", dynamic.createString(color));
                }
            }
            return dynamic;
        });
    }
}