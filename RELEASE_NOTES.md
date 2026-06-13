## [5.0 Snapshot 1] - UNRELEASED
### New Features
#### Player-Tailored World Generation
- Added the Tailored World Generation system
- During world creation, a "Default" world type can now be customized
- Choosing the "Customize" option allows the player to modify sliders corresponding to world generation factors
  - Added customizable option:
    - Continent Scale
    - Biome Scale
  - There is also a Master scale, which will adjust these values together.

#### Blocks
- Added Perse Wildflowers
  - Can be crafted into Purple Dye
  - Can be found in Birch Forests, Meadows, Swamps, and the Dappled Forest
  - Developer Notes:
    - These are inspired by Forget-Me-Not flowers
    - "Perse" is an archaic term for "blue-ish" :D
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
- Changed Brown Mushroom Blocks to have a light level of 1, to match the small brown mushroom

#### World Generation
- Changed all biomes to be larger
- Changed all continents to be larger
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
- Added sparse rocks to the understory
- Changed canopys to be larger
- Changed dark oak trees to be taller
- Changed the sky to be darker
- Changed the ratio of Dark Oaks trees to other vegetation and huge mushrooms
- Changed red mushroom caps to be slightly shorter
- Added small mushroom rings around huge mushrooms

##### Swamp
- Changed frog spawn rates in Swamp biomes to be higher
- Added tadpole spawning to Swamp biomes

#### Atmospherics
- Added ambient fog to Swamp biomes
- Added ambient fog to Mangrove Swamp biomes
- Added ambient fog to Pale Garden biomes
- Added ambient fog to Dark Forest biomes

### Changes
#### Crafting
- Changed map crafting recipe
  - Changed map crafting recipe to be a 9x9 of paper
    - This matches Bedrock Edition

#### Textures
- Changed the Leaf Litter texture to have fewer leaves and (hopefully) appear more natural

#### Sounds
- Changed Bush ambient sound to be louder
- Changed Sand ambient sound to be louder
- Changed dry vegetation ambient sound to be louder
- Changed Dead Bush ambient sound to be louder
- Changed Bush ambient sound subtitle to match the vanilla dry grass subtitle
- Changed ambient cold wind sounds to be less common

#### Splash Text
- Added "Bigger! Better!" splash text
- Added "Perse!" splash text
- Added "Any shape and size!" splash text

### Technical Changes
#### Data Tags
- Added `is_foggy` biome tag
- Added `is_very_foggy` biome tag
- Added `has_cespitose` biome tag
- Added `triggers_ambient_bush_block_sounds` block tag
- Removed `has_ambient_block_sounds` block tag