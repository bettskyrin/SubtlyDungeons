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
#### Tents
- Tents now continuously check to see if there are sturdy blocks below it
  - The check for this includes the 4 corners of the hitbox.

#### Wither
- Wither Skull explosions can now only convert `dirt` block tag blocks to Soul Soil 

#### Sounds
- Removed ambient Tall Grass block sounds from Pale Gardens
- Removed ambient Pale Oak Leaves block sounds
- Removed ambient Leaves block sounds from cold biomes

#### Settings
- Added a warning for disabling advanced entity animations

#### Advancements
- Changed Adventure root advancement to use the Filled Map texture

### Technical Changes
#### Mobs
- Improved mob environment detection AI

#### Camera Shake Events
- Changed `range` field to accept an integer value

#### Data Components
- Added `tent/color` data component

#### Data Tags
- Added `scansorial` entity type tag
- Added `silent_foliage` block tag
- Added `arrow_flammable` block tag
- Added `causes_flock_panic` damage type tag
- Removed `can_break_tents` damage type tag
- Removed `always_kills_tent` damage type tag
- Removed `ignites_tents` damage type tag
- Removed `burns_tents` damage type tag

#### Bug Fixes
- Fixed bug causing spider jockeys to suffocate if their spider reaches a ceiling
- Fixed bug that could crash servers when players try to disable advanced entity animations
- Fixed [BUG #76] Tents Z-Fight with their pegs
- Fixed missing Blast Fungus entity translation
- Fixed bug causing command macro buttons to activate their primary key press action
- Fixed bug causing sleeping in Tents to cause Minecraft to freeze
- Fixed bug causing Tentative Accomodations to be granted to those who have yet to sleep in beds
