## [5.0 Snapshot 1] - UNRELEASED
### New Features
#### Player-Tailored World Generation
- Added the Tailored World Generation system
- During world creation, a "Default" world type can now be customized
- Choosing the "Customize" option allows the player to modify sliders corresponding to world generation factors
  - Added customizable option:
    - Continent Scale
    - Biome Scale
  - There is also a Master scale, which will adjust these values  together.

#### Blocks
- Added Perse Wildflowers
  - Can be crafted into Purple Dye
  - Can be found in Birch Forests, Meadows, Swamps, and the Dappled Forest
  - Developer Notes:
    - These are inspired by Forget-Me-Not flowers
    - "Perse" is an archaic term for "blue-ish" :D
- Added Hay Stairs
- Added Hay Slab
- Added snowlogging, like on Bedrock Edition
  - Only some blocks are now snow loggable:
    - All flowers
    - All grasses
    - All bushes/ferns
    - All tree saplings
    - All mushrooms
    - All fences
    - All fence gates
    - All walls
    - Iron bars
    - All glass panes
  - Snowlogged blocks may generate during world generation
  - Blocks may be snowlogged during snowfall
- Changed doors to be water loggable

#### World Generation
- Added Warm River biome
- Added Cold River biome
- Changed all biome sizes to be 50% larger
- Changed all ocean depths to be 50% deeper
- Changed Oak tree height
- Changed Birch tree height
- Changed grass placement rate
- Changed bush placement rate
- Changed snow to generate under trees in snowy biomes

##### Forest
- Added sparse rocks to the understory
- Added more grass to the understory
- Added rabbit spawning to biome
- Added moss carpet to fallen logs

##### Birch Forest
- Added more grass to the understory
- Added moss carpet to fallen logs

##### Dark Forest
- Changed canopys to be larger
- Changed the sky to be darker
- Changed the ratio of Dark Oaks trees to other vegetation
- Added sparse rocks to the understory

##### Swamp
- Changed frog spawn rates in Swamp biomes to be higher
- Added tadpole spawning to Swamp biomes

#### Atmospherics
- Added ambient fog to Swamp biomes
- Added ambient fog to Mangrove Swamp biomes
- Added ambient fog to Pale Garden biomes

### Changes
#### Crafting
- Changed map crafting recipe
  - Changed map crafting recipe to be a 9x9 of paper
    - This matches Bedrock Edition

#### Sounds
- Changed Bush ambient sound to be louder
- Changed Sand ambient sound to be louder
- Changed dry vegetation ambient sound to be louder
- Changed Dead Bush ambient sound to be louder
- Changed Bush ambient sound subtitle to match the vanilla dry grass subtitle

#### Splash Text
- Added "Bigger! Better!" splash text
- Added "Perse!" splash text
- Added "Any shape and size!" splash text

### Technical Changes
#### Data Tags
- Added `is_foggy` biome tag
- Added `is_very_foggy` biome tag
- Added `triggers_ambient_bush_block_sounds` block tag
- Removed `has_ambient_block_sounds` block tag