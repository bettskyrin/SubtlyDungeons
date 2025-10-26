package com.kr1s1s.subtlyd.data.loot_table;

import com.kr1s1s.subtlyd.data.loot_table.chests.VillageLootSD;
import com.kr1s1s.subtlyd.data.loot_table.entities.EntityLootSD;
import com.kr1s1s.subtlyd.data.loot_table.gameplay.FishingLootSD;
import com.kr1s1s.subtlyd.data.loot_table.gameplay.VillageHeroLootSD;

public class LootSD {
    public static void generate() {
        VillageLootSD.generate();
        VillageHeroLootSD.generate();
        EntityLootSD.generate();
        FishingLootSD.generate();
    }
}
