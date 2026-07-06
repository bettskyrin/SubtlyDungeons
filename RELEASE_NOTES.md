## [5.0 Snapshot 3] - UNRELEASED
### New Features
#### Tailored World Generation
- Added Erosion slider

#### Items
- Added Heavy Shield
  - Has a Shield Strength of 10
  - Has a blocking delay of 5 ticks
  - Can become disabled for 1.6 seconds, instead of 5 seconds
- Added Dyed Quivers
  - Can be crafted by combining a quiver with any dye

#### Sounds
- Added ambient Leaves block sounds
- Added ambient Short Grass block sounds
- Added ambient Red Shrub sounds

#### Settings
- Added Video Settings option for toggling advanced entity animations (e.g. Arthropods moving horizontally up walls)
- Added Accessibility Settings option for toggling a blocking shield's visibility

### Changes
#### Items
- Changed Shield to no longer have a blocking delay

#### Blocks
- Changed Powder Snow to decrease mining speed on hard difficulty

#### World Generation
##### Birch Trees
- Changed Super Birch Trees to be able to have shelf mushrooms generate higher on their trunks

#### Textures
- Changed Quiver texture

#### Loot
- Changed Quivers to no longer be found in Weaponsmith House Chests
- Changed Quivers to be found in Tannery Chests
- Changed Pillager Outpost Chests to possibly have Heavy Shields and Quivers
- Changed Weaponsmith House Chests to possibly have Heavy Shields
- Changed Woodland Mansion Chests to possibly have Heavy Shields
- Changed Leatherworkers to gift Bundles and Quivers

#### Advancement
- Changed I Am Bush advancement to have Monster Hunter as its parent

### Technical Changes
#### Attributes
- Changed `attribute.name.subtlyd.shield_strength` to `attribute.name.generic.shield_strength`

#### Data Tags
- Added `triggers_ambient_grass_block_sounds` block tag
- Added `quivers` item tag
- Added `shields` item tag

#### Bug Fixes
- Fixed bug causing Tents to display the wrong error when trying to sleep during the day
- Fixed bug preventing Powder Snow from replacing potions in a Cauldron
- Fixed bug that would grant the Soup-er advancement for trying to put stew ingredients in cauldrons that cannot receive them
- Fixed bug preventing Redstone comparisions to work with Potion & Stew Cauldrons
- Fixed bug that caused Water Bottles to completely fill Cauldrons
- Fixed bug causing snowfall to replace foliage
- Fixed bug preventing snowfall from snowlogging blocks
- Fixed bug causing Frozen Rivers to not be fully frozen after world generation
- Fixed bug causing empty snow patches in snowy biomes