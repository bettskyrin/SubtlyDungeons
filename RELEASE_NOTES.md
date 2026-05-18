## [5.0 Snapshot 1] - UNRELEASED
### New Features
#### Snowlogging
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
- The snow layers may be removed by using a shovel on the snowlogged block
- Snow layers build up during 

#### World Generation
- Increased Oak tree height
- Increased Birch tree height
- Increased grass placement rate
- Increased bush placement rate

#### Biomes
- Changed all biome sizes to be 50% larger
- Changed all ocean depths to be 50% deeper

##### Forest
- Added sparse rocks to the understory
- Added more grass to the understory
- Added rabbit spawning to biome

##### Dark Forest
- Changed canopys to be larger
- Added sparse rocks to the understory
- Changed the sky to be darker

##### Swamp
- Changed frog spawn rates in Swamp biomes to be higher
- Added tadpole spawning to Swamp biomes

#### Water Logging
- Doors are now water loggable

#### Fog
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
- Changed dead bush ambient sound to be louder
- Changed Bush ambient sound subtitle to match the vanilla dry grass subtitle

### Technical Changes
#### Data Tags
- Added `is_foggy` biome tag
- Added `is_very_foggy` biome tag
- Added `triggers_ambient_bush_block_sounds` block tag
- Removed `has_ambient_block_sounds` block tag