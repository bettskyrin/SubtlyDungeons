package net.meander.subtlyd.util.data.loot_table;

import net.meander.subtlyd.util.data.loot_table.chests.VillageLootSD;
import net.meander.subtlyd.util.data.loot_table.entities.EntityLootSD;
import net.meander.subtlyd.util.data.loot_table.gameplay.FishingLootSD;
import net.meander.subtlyd.util.data.loot_table.gameplay.VillageHeroLootSD;

public class LootSD {
    public static void registration() {
        VillageLootSD.register();
        VillageHeroLootSD.register();
        EntityLootSD.register();
        FishingLootSD.register();
    }
}
