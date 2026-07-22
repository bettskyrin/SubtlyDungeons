## [5.0 Snapshot 4] - UNRELEASED

### New Features
#### World Generation
##### Savanna
- Added Baobab trees
  - Similarly to Pine trees, these trees do not have a unique wood type, but are a special type of Acacia tree
  - Can be grown from a 2x2 of Acacia saplings

#### Game Rules
- Added Smart Mobs game rule
  - Toggles advanced mob behavior (like flock panicking)
  - Does not include wall climbing hitbox adjustments

### Changes
#### Sounds
- Removed ambient Tall Grass block sounds from Pale Gardens
- Removed ambient Pale Oak Leaves block sounds
- Removed ambient Leaves block sounds from cold biomes

#### Settings
- Added a warning for disabling advanced entity animations

### Technical Changes
#### Bug Fixes
- Fixed bug causing spider jockeys to suffocate if their spider reaches a ceiling
- Fixed bug that could crash servers when players try to disable advanced entity animations

#### Data Components
- Added `tent/color` data component

#### Data Tags
- Added `scansorial` entity type tag
- Added `silent_foliage` block tag
- Removed `can_break_tents` damage type tag
- Removed `always_kills_tent` damage type tag
- Removed `ignites_tents` damage type tag
- Removed `burns_tents` damage type tag