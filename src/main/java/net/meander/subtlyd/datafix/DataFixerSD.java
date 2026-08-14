package net.meander.subtlyd.datafix;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataFixers;

public class DataFixerSD {
    private static final DataFixer FIXER = createFixer();

    private static DataFixer createFixer() {
        int dataVersion = SharedConstants.getCurrentVersion().dataVersion().version();
        Schema schema = DataFixers.getDataFixer().getSchema(dataVersion);
        Schema schemaSDV0 = new V0SD(0, schema);
        DataFixerBuilder builder = new DataFixerBuilder(UtilSD.DATA_VERSION);

        builder.addSchema(schemaSDV0);

        Schema schemaSDV1 = builder.addSchema(1, V1SD::new);

        builder.addFixer(new TentEntityColorComponentFix(schemaSDV1, true));
        return builder.build().fixer();
    }

    public static DataFixer getFixer() {
        return FIXER;
    }
}