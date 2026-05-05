## [4.0 Snapshot 2] - UNRELEASED

### New Features
#### Enchanting
- Added Glyph Affinity enchantment
    - An enchantment which increases the Enchantment Limit by a factor of 1.5
    - Is a treasure enchantment
- Magic level now increases when an item is enchanted via enchanting table.
- Non-Humanoid armors are no longer enchantable via enchanting table.

#### Enchantiblity
- The enchantment level cap is now calculated by this formula: `(enchantability difference * 2) + 40`

#### Blocks
- Harvesting fully grown crops now provides 0-2 experience, down from the previous 0-3.
- Warped Overhang can now be used to craft Warped Wart Blocks
- Added Basalt Slab

#### Loot
- Horse Armor is now enchanted in Ancient Cities, End Cities, Nether Fortresses, and Stronghold Corridors

#### Sounds
- Removed Soggy Swamp by Johan Johnson (Swamp, Jungle, Sparse Jungle)
  - Developer Note: This music track seems to try and create a specific feeling for the listener. One of the things that we think makes Minecraft music so great, is how neutral it is. A song may make you feel alone, inspired, alien, etc. This song didn't allow for this very much.
- Added sound effect for Frosted Ice (ice created by the Frost Walker enchantment)

### Technical Changes
#### Data Tags
- Added `increases_magic_limit` enchantment tag
  - These enchantments increase the magic level limit of the item and decrease how quickly an item's magic level will increase
- Added `repairs_equipment` enchantment tag
  - These enchantments are incomptabile with the abrading curse

### Bug Fixes
- Fixed bug preventing the Magic Level value from going up properly
- Fixed bug where the cost of expensive anvil repairs and enchants weren't colored properly
- Improved determinism