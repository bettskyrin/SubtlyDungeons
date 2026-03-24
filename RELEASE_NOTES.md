## [4.0 Snapshot 1] - UNRELEASED
### New Features
#### Enchanting
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm
- Added Animal Armor enchantments
  - Horse armor may now be enchanted
  - Wolf armor may now be enchanted
  - Nautilus armor may now be enchanted
- Enchanting tables now accept carpets, potted plants, candles, chains, cauldrons, brewing stands, and chiseled bookshelves within the enchanting area

#### Curses
- Added Curse of Decaying
  - Affected equipment loses durability twice as fast

#### Enchantibility
- High enchantibility (level 15 or higher) armors will turn invisible with the wearer

#### Speedy Potions
- All drinks (potions, milk buckets, stews, etc.) are now consumed faster!
    - The time has been reduced to 20 ticks, to match the Combat Tests

#### Stackable Potions
- Consumable potions are now stackable up to 16 items
  - This is to match the combat tests

#### Burn No More!
- Players with the Fire Resistance effect will no longer be visually set on fire!

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

#### The Wither
- Wither skulls fired at the player are now on fire!
  - This was originally intended by Mojang, but caused crashes
- The Wither's health now scales with difficulty to match Bedrock Edition
  - Easy 300 HP -> 300 HP
  - Normal 300 HP -> 450 HP
  - Hard 300 HP -> 600 HP
- The Wither now summons Wither Skeletons when it has reached 50% health
  - This is also to match Bedrock Edition
- All entities with the `wither_friends` entity type tag will no longer target the Wither

  Note:  The Bedrock Edition's Wither "Dash" behavior was purposefully ignored.

### Technical Changes
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

#### Textures
- Added Hardcore HUD hearts for the Poison effect
- Added Hardcore HUD hearts for the Wither effect

#### Bug Fixes
- Fixed vanilla bug where deaths by dragon's breath were referred to as "magic"
  - It now properly states that "[Player] was roasted in dragon's breath"