## [4.0 Snapshot 1] - UNRELEASED
It's snapshot time! We've got some nice magical tweaks and technical upgrades for you!

### New Features
#### Enchanting
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm
- Added Animal Armor enchantments
  - Horse armor may now be enchanted
  - Wolf armor may now be enchanted
  - Nautilus armor may now be enchanted
- Enchanting tables now accept carpets, potted plants, candles, chains, cauldrons, brewing stands, and chiseled bookshelves within the enchanting area
- Tweaked Anvil Enchanting
  - The "Too Expensive!" message has been replaced with "Unrepairable!"
    - The idea of the "Too Expensive!" repair limit is to mimic how in real life, you can't keep repairing an object over and over, forever. While we are not removing this feature, we have decided to rename it, to help make this more clear.
  - The limit for adding enchantments is now tied to enchantibility
    - The message for an item with too many enchantments is "Magic Capacity Met!"
  - Repairing an item enchanted with mending will have an enchantment level cost of 1.
  - Enchanting and repairing an item enchanted with mending will have a decreased enchantment cost
    - This cost is reduced by 4 levels

#### Enchantiblity
- The enchantibility difference of two items in an anvil will scale the enchantment cost cap.
  - Enchantibility difference is calculated as `|enchantibility of item 1 - enchantibility of item 2|` 
  - The enchantment level cap is now calculated by this formula: `(enchantibility difference * 2.4) + 40`
      - Example: A diamond helmet (Enchantibility level 10) in an anvil with an enchanted book (Enchantability level 0) will have a maximum enchantment cost of 55
        - If the cost gets any higher than this, the anvil will say "Unrepairable!"
      - The enchantment cap is always rounded up
      - The maximum enchantment level cap is now 100 (which can be achieved by combining two pieces of golden armor)
- High enchantibility (level 15 or higher) armors will turn invisible with the wearer
- Elytras now have an enchantibility level of 9

#### Curses
- Added Curse of Abrading
  - Affected equipment loses durability twice as fast

#### The Nether
- Huge Warped Fungus
  - Added Warped Overhang block
    - Generates beneath Warped Wart blocks
    - Obtainable with silk touch tools or shears
  - There is a 10% chance for Warped Roots to grow on top of the fungus cap
  - This design is inspired by Minecraft Dungeons!

#### Blast Fungus
- Blast Fungi can be crafted shapelessly with a Crimson Fungus and Warped Fungus
  - Once thrown, a blast fungus explodes on impact, doing no damage to surrounding blocks

#### Potion Tweaks
- All drinks (potions, milk buckets, stews, etc.) are now consumed faster!
    - The time has been reduced to 20 ticks, to match the Combat Tests
- Consumable potions are now stackable up to 16 items
  - This is to match the combat tests
- Potions are now named depending on their enhancement
  - Example: A Potion of Swiftness that lasts for 8 minutes is now called a "Long Potion of Swiftness"
  - Example: A Potion of Swiftness that provides Speed II is now called a "Strong Potion of Swiftness"
  - This is based on their internal code names

#### New Potions
- Added Potion of Decay to match Bedrock Edition

#### Potion Effects
- Players with the Fire Resistance effect will no longer be visually set on fire

#### The Wither
- Wither skulls fired at the player are now on fire!
  - This was originally intended by Mojang, but caused crashes
- The Wither's health now scales with difficulty to match Bedrock Edition
  - Easy 300 HP -> 300 HP
  - Normal 300 HP -> 450 HP
  - Hard 300 HP -> 600 HP
- The Wither now dive bombs and summons Wither Skeletons when it has reached 50% health
  - This is also to match Bedrock Edition
- All entities with the `wither_friends` entity type tag will no longer target the Wither

  Note:  The Bedrock Edition's Wither "Dash" behavior was purposefully ignored.

#### Beacons
- Beacon effect range has been quadrupled
  - Level 1 Beacon Range:
    - 20 meters -> 80 meters
  - Level 2 Beacon Range:
    - 30 meters -> 120 meters
  - Level 3 Beacon Range:
    - 40 meters -> 160 meters
  - Level 4 Beacon Range:
    - 50 meters -> 200 meters

#### Blocks
- Overworld mushrooms may now be placed on logs regardless of light level

#### Splash Text
- Added "Music by Rostislav Trifonov!"
- Added "Music by Eugnosis!"

### Technical Changes
#### Camera Shake Events
Camera Shake events are now data driven, similar to sound events. Camera shake event files are to be put in a `data/[datapack namespace]/subtlyd/camera_shake_event/` folder.
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

#### Sounds
- Added new sound event for the Wither:
  - `entity.wither_skeleton.summon`
- Added new sound event for Evoker Fangs:
  - `entity.evoker_fangs.appear`
- Added 7 New Songs
  - Guldrum by Peter Hont
    - Plays in Nether Wastes
  - Warped Forest by Eugnosis
    - Playes in Warped Forests
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
- Music volume has been refactored
- The unused Warped Forest mood sounds are now used

#### Textures
- Added Hardcore HUD hearts for the Poison effect
- Added Hardcore HUD hearts for the Wither effect

#### Bug Fixes
- **MC-84595** - Fixed bug where deaths by dragon's breath were referred to as "magic"
  - It now properly states that "[Player] was roasted in dragon's breath"
- Fixed some memory leaks
- Fixed flaming arrows not setting fire to objects when shot east or west
- Optimized music tracks loading
- Fixed Netherite items with custom names not inheriting a new rarity value