## [4.0 Magical Madness Update] - 6/16/26
### New Features
#### Enchantments
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm
- Added Glyph Affinity enchantment
  - A treasure enchantment which increases the Enchantment Limit by a factor of 1.5
- Added Illager's Bane enchantment
  - Does increased damage to illagers
    - Each level adds 2.5 damage to an attack
  - Is incompatible with Bane of Arthropods, Sharpeness, Density, Breach, and Impaling
  - Is obtainable for enchanted books or equipment via villager trading
- Added Animal Armor enchantments
  - Horse armor may now be enchanted
  - Wolf armor may now be enchanted
  - Nautilus armor may now be enchanted

#### Curses
- Added Curse of Abrading
  - Affected equipment loses durability thrice as fast

#### Charged Tridents
- Tridents enchanted with Channeling can now be charged for 4 seconds to create a Charged Trident
- Charged Tridents can summon a lightning bolt regardless of the current weather
- Charged Tridents have a visual effect similar to that of Charged Creepers
- Tridents may only be charged if the user's dimension is capable of having weather

#### The Nether
- Huge Warped Fungus
  - Now have Warped Overhang
  - There is a 10% chance for Warped Roots to grow on top of the fungus cap
  - This design is inspired by Minecraft Dungeons!

#### Blast Fungus
- Blast Fungi can be crafted shapelessly with a Crimson Fungus and Warped Fungus
  - Once thrown, a blast fungus explodes on impact, doing no damage to surrounding blocks

#### Potions
- Added Potion of Decay
  - Can be brewed by adding a Wither Skeleton Skull to an Awkward Potion
- Added Elixir of the Coven
  - Removes all negative potion effects
  - Can only be obtained by killing a Witch in a Swamp
    - If killed in a Swamp biome, Witches have a 5% chance of dropping 1 bottle
    - If killed in a Witch Hut, Witches have a 50% chance of dropping 1 bottle

#### Potion Effects
- Changed entities with Fire Resistance to no longer be visually set on fire
- High enchantability (level 15 or higher) armors will turn invisible with the wearer
- Changed Jump Boost effect to be increased by half a meter
  - The common Potion of Leaping will now allow the player to leap 2 meters high by default

#### Cauldron Potions
- Added Potion Cauldron block entity
- Behavior mimics Bedrock Edition
  - Cauldrons will now hold potions inside of them
  - Tipped arrows may now be created using cauldrons
  - Have a maximum of 6 levels
    - However, there are still only 3 visual levels

#### Blocks
- Added Basalt Slab
- Added Soul Jack o'Lantern
  - Can be crafted with a carved pumpkin and soul torch
- Added Warped Overhang block
  - Generates beneath Warped Wart blocks
  - Obtainable with silk touch tools or shears
  - Can be used to craft Warped Wart Blocks
- Added Unlit Soul Campfire
  - Is given when the player picks up an unlit Soul Campfire block with Silk Touch

#### Music
- Added 7 new background music tracks:
  - Guldrum by Peter Hont
    - Plays in Nether Wastes
  - Warped Forest by Eugnosis
    - Plays in Warped Forests
  - Soulsand Valley by Rostislav Trifonov
    - Plays in Soul Sand Valleys
  - Ashes by Peter Hont
    - Plays in Basalt Deltas
  - Basalt Deltas by Peter Hont
    - Plays in Basalt Deltas
  - Crimson Forest by Eugnosis
    - Plays in Crimson Forests
  - Excuse by C418
    - Plays in Warped Forests and Nether Wastes
- Changed the unused Warped Forest mood sound "mood 8" to be used
- Changed Dalarna to have a chance to play on the main menu
- Music volume has been refactored
- Removed Soggy Swamp by Johan Johnson (Swamp, Jungle, Sparse Jungle)
  - Developer Note: This music track seems to try to create a specific feeling for the listener. One of the things that we think makes Minecraft music so great, is how neutral it is. A song may make you feel alone, inspired, alien, etc. This song didn't allow for this very much.

#### Advancements
- Added new "Traveler" adventure challenge
  - Awarded for sleeping in a tent at least 1 km away from spawn or in a different dimension

#### Loot
- Added Enchanted Horse Armor loot pool to Ancient Cities, End Cities, Nether Fortresses, and Stronghold Corridors
  - This replaces the unenchanted Horse Armor loot pool
- Changed cows, mooshrooms, horses, llamas, mules, donkeys, and trader llamas to drop between 1 and 4 leather

#### Sounds
- Added sound effects for Frosted Ice (ice created by the Frost Walker enchantment)
- Added new flaming arrow sound effect
- Added new Ghast fireball shooting sound  effect
- Added new Blaze fireball shooting sound effect
- Added new Ender Dragon fireball shooting sound effect
- Added sound effects for Lingering Potion Area Effect Clouds
- Added sound effects for Dragon's Breath Area Effect Clouds
- The Hardcore activate sound has been slightly altered

#### Settings
- Added Video Settings option for toggling the experimental UI changes

#### Splash Text
- Added "Music by Rostislav Trifonov!"
- Added "Music by Eugnosis!"

### Changes
#### Enchanting
- Changed Enchanting tables to accept carpets, potted plants, candles, chains, cauldrons, brewing stands, and chiseled bookshelves within the enchanting area
- Changed Anvil Enchanting
  - The "Too Expensive!" message has been replaced with "Unrepairable!"
    - The idea of the "Too Expensive!" repair limit is to mimic how, in real life, you can't keep repairing an object over and over, forever. While we are not removing this feature, we have decided to rename it to help make this more clear.
  - The limit for adding enchantments is now tied to enchantability
    - The message for an item with too many enchantments is "Magic Capacity Met!"
  - Repairing an item enchanted with mending will have an enchantment level cost of 1.
  - Enchanting and repairing an item enchanted with mending will have a decreased enchantment cost
    - This cost is reduced by 4 levels, to a minimum of 1 level.
  - Repairing and Enchanting in an anvil have been split into two numbers.
    - Repair Cost (also known as the Prior Work Penalty): Functions the same as it did before, but now, enchanting does not increase the Repair Cost
    - Magic Level: Is a separate number that increases each time an item is enchanted. An item can be enchanted until you reach the magic limit, as mentioned above.

#### Enchantments
- Changed Piercing to reduce the effectiveness of armor by 10% with each level
- Changed Impaling to affect all mobs that are in contact with water for parity with Bedrock Edition

#### Potions
- Changed liquid consumables to be consumed faster!
  - The time has been reduced to 20 ticks, to match the Combat Tests
- Changed consumable potions to be stackable up to 16 items
  - This is to match the combat tests
- Changed potions naming to be based on their enhancement
  - Example: A Potion of Swiftness that lasts for 8 minutes is now called a "Long Potion of Swiftness"
  - Example: A Potion of Swiftness that provides Speed II is now called a "Strong Potion of Swiftness"
  - This is based on their internal code names

#### The Wither
- Changed launched Wither Skulls to be on fire
  - This was originally intended by Mojang, but caused crashes
  - The skull flames are blue, to match Soul Fire
  - These skulls now place Soul Fire when they explode
- Changed Wither health to scale with difficulty to match Bedrock Edition
  - Easy 300 HP -> 300 HP
  - Normal 300 HP -> 450 HP
  - Hard 300 HP -> 600 HP
- The Wither now dive bombs and summons Wither Skeletons when it has reached 50% health
  - This is also to match Bedrock Edition
- All entities with the `wither_friends` entity type tag will no longer target the Wither

  Note:  The Bedrock Edition's Wither "Dash" behavior was purposefully ignored.

#### Beacons
- Changed Beacon effect range to be 4 times as high
  - Level 1 Beacon Range:
    - 20 meters -> 80 meters
  - Level 2 Beacon Range:
    - 30 meters -> 120 meters
  - Level 3 Beacon Range:
    - 40 meters -> 160 meters
  - Level 4 Beacon Range:
    - 50 meters -> 200 meters
- This helps balance a more difficult Wither boss battle

#### Structures
- Changed Swamp Hut cauldrons to now have a random potion effect inside of them, to match Bedrock Edition

#### Agricultural XP
- Changed fully grown crops to drop 0-2 XP when reaped

#### Blocks
- Changed overworld mushrooms to always be placeable on logs
- Changed Unlit Campfires to drop when broken with a Silk Touch tool

#### Textures
- Changed Dragon Fireball texture to match Bedrock Edition
- Changed Fire Charge/Fireball texture
- Changed texture for entities on fire that are Wither Skulls, in Soul Fire, on Soul Sand, on Soul Soil, or in Soul Sand Valleys
- Added potion bottle archetypes
  - Some potions have different bottle shapes to better help visibility for those with colorblindness
  - These archetypes are:
    - Conical
    - Spherical
    - Vial
  - These archetypes are controlled via Data Tags
  - This design is to match Minecraft Dungeons
- Reverted the Uncraftable Potion texture to its pre-1.21 color (Magenta)

### Technical Changes
#### Camera Shake Events
Camera Shake events are now data-driven, similar to sound events. Camera shake event files are to be put in a `data/[datapack namespace]/subtlyd/camera_shake_event/` folder.
- Added `sound_event` field: a Sound Event ID, specifies what sound event should trigger the Camera Shake Event
- Added `range` field: a float, specifies the maximum distance that the camera shake can be experienced from
- Added `duration` field: an integer, specifies the amount of ticks that the camera shake will last
- Added `intensity` field: an optional float, specifies the intensity modifier value
  - Default: `4`

#### Data Tags
- Added `liquid_consumables` item tag
  - This is a list of consumables that can be considered liquids (e.g. stews, potions, etc.)
- Added `is_occult` damage type tag
  - This tag controls what types of damage Occult Protection can protect against
    - This includes magic, indirect magic, sonic booms, withering, and thorns
- Added `non_humanoid_armor` item tag
  - This tag is a list of armor items not for humanoids (e.g. diamond horse armor, wolf armor, etc.)
  - Any item with this tag will become enchantable with armor enchantments.
- Added `increases_magic_limit` enchantment tag
  - These enchantments increase the magic level limit of the item and decrease how quickly an item's magic level will increase
- Added `repairs_equipment` enchantment tag
  - These enchantments are incompatible with the abrading curse
- Added `conical` potion tag
  - By default, this includes potions of Strength, Weakness, Slow Falling, Wind Charging, and Decay
- Added `spherical` potion tag
  - By default, this includes potions of Water Breathing, Oozing, Infestation, and Turtle Master
- Added `vial` potion tag
  - By default, this includes potions of Swiftness, Slowness, Leaping, and Weaving

#### Enchantiblity
- The enchantability difference of two items in an anvil will scale the enchantment cost cap.
  - Enchantability difference is calculated as `|enchantability of item 1 - enchantability of item 2|`
  - The enchantment level cap is calculated by this formula: `(enchantability difference * 2) + 40`
    - Example: A diamond helmet (Enchantability level 10) in an anvil with an enchanted book (Enchantability level 0) will have a maximum enchantment cost of 55
      - If the cost gets any higher than this, the anvil will say "Unrepairable!"
    - The enchantment cap is always rounded up
    - The maximum enchantment level cap is now 100 (which can be achieved by combining two pieces of golden armor)
- Elytras now have an enchantability level of 9

- #### Entity Data
- Custom Entity Data has been changed to use pascal case, rather than snake case
  - e.g. The command for summoning a leader zombie is now `/summon zombie ~ ~ ~ {IsLeader:1b}`

#### Stats
- Added `Times Slept in a Tent` statistic

#### Sounds
- Added new sound event for the Wither:
  - `entity.wither_skeleton.summon`
- Added new sound event for Evoker Fangs:
  - `entity.evoker_fangs.appear`
- Added new sound event for Lingering Potion Area Effect Clouds:
  - `entity.area_effect_cloud.gas`
- Added new sound event for Dragon's Breath Area Effect Clouds:
  - `entity.ender_dragon.breath`

#### Textures
- Added Hardcore HUD hearts for the Poison effect
- Added Hardcore HUD hearts for the Wither effect

#### Particles
- Added `spore_cloud` particle
#### Credits
- Added ourselves (and the talented artists that make the mod's music) to the game's end credits!

#### Bug Fixes
- **MC-84595** - Fixed bug where deaths by dragon's breath were referred to as "magic"
  - It now properly states that "[Player] was roasted in dragon's breath"
- Fixed some memory leaks
- Fixed flaming arrows not setting fire to objects when shot east or west
- Optimized music tracks loading
- Optimized music track size
- Fixed Netherite items with custom names not inheriting a new rarity value
- Fixed a possible bug where reeds could prevent trees from spawning
- Fixed bug that made Killer Bunnies try to find shelter from weather
- Fixed bug that prevented custom options from saving after exiting the game
- Fixed bug preventing animals that are taking shelter from walking around
- Fixed bug allowing animal herds to panic even if the danger is environmental and unable to hurt them
- Fixed a bug causing the game to crash on launch for some players