## [4.0 Snapshot 2] - UNRELEASED

### New Features
#### Enchanting
- Added Glyph Affinity enchantment
    - An enchantment which increases the Enchantment Limit by a factor of 1.5
    - Is a treasure enchantment

#### Blocks
- Harvesting fully grown crops now provides 0-2 experience, down from the previous 0-3.

### Technical Changes
#### Data Tags
- Added `increases_magic_limit` enchantment tag
  - These enchantments increase the magic level limit of the item and decrease how quickly an item's magic level will increase
- Added `repairs_equipment` enchantment tag
  - These enchantments are incomptabile with the abrading curse

### Bug Fixes
- Fixed bug preventing the Magic Level value from going up properly
- Fixed bug where the cost of expensive anvil repairs and enchants weren't colored properly