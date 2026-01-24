package com.meander.subtlyd.util.data.loot_table;

import com.meander.subtlyd.util.data.loot_table.chests.VillageLootSD;
import com.meander.subtlyd.util.data.loot_table.entities.EntityLootSD;
import com.meander.subtlyd.util.data.loot_table.gameplay.FishingLootSD;
import com.meander.subtlyd.util.data.loot_table.gameplay.VillageHeroLootSD;

public class LootSD {
    public static void registration() {
        VillageLootSD.register();
        VillageHeroLootSD.register();
        EntityLootSD.register();
        FishingLootSD.register();
    }
}
