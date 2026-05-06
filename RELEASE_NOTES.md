## [4.0 Snapshot 2] - 5/5/26
Coming back at you with another snapshot! Increase the enchantment cap with Glyph Affinity or charge up your Channeling trident... so long as you're not indoors! Today's snapshot is smaller than before, but should be the last features directly relating to Enchantments. On to the next magical category!

### New Features
#### Enchanting
- Added Glyph Affinity enchantment
    - A treasure enchantment which increases the Enchantment Limit by a factor of 1.5
- Magic level now increases when an item is enchanted via enchanting table.
- Non-Humanoid armors are no longer enchantable via enchanting table.
- Added sound effect for Frosted Ice (ice created by the Frost Walker enchantment)

#### Charged Tridents
- Tridents enchanted with Channeling can now be charged for 4 seconds to create Charged Trident
- Charged Tridents can summon a lightning bolt regardless of the current weather
- Charged Tridents have a visual effect similar to that of Charged Creepers
- Tridents may only be charged if the user's dimension is capable of having weather

#### Blocks
- Added Basalt Slab
- Harvesting fully grown crops now provides 0-2 experience, down from the previous 0-3.
- Warped Overhang can now be used to craft Warped Wart Blocks

#### Loot
- Horse Armor is now enchanted in Ancient Cities, End Cities, Nether Fortresses, and Stronghold Corridors

### Technical Changes
#### Data Tags
- Added `increases_magic_limit` enchantment tag
  - These enchantments increase the magic level limit of the item and decrease how quickly an item's magic level will increase
- Added `repairs_equipment` enchantment tag
  - These enchantments are incomptabile with the abrading curse

#### Enchantiblity
- The enchantment level cap is now calculated by this formula: `(enchantability difference * 2) + 40`

#### Entity Data
- Custom Entity Data has been changed to use camel case, rather than snake case
  - e.g. The command for summoning a leader zombie is now `/summon zombie ~ ~ ~ {IsLeader:1b}`

#### Sounds
- Removed Soggy Swamp by Johan Johnson (Swamp, Jungle, Sparse Jungle)
  - Developer Note: This music track seems to try and create a specific feeling for the listener. One of the things that we think makes Minecraft music so great, is how neutral it is. A song may make you feel alone, inspired, alien, etc. This song didn't allow for this very much.

#### Bug Fixes
- Fixed bug preventing the Magic Level value from going up properly
- Fixed bug where the cost of expensive anvil repairs and enchants weren't colored properly
- Fixed Gurgle missing texture bug
- Fixed Warped Overhang not being mineable
- Fixed a possible bug where reeds could prevent trees from spawning