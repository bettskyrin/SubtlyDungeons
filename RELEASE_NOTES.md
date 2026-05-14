## [4.0 Snapshot 4] - UNRELEASED
### New Features
#### Blocks
- Added Unlit Soul Campfire
  - Is given when the player picks up an unlit Soul Campfire block with Silk Touch

### Changes
#### Textures
- Added potion bottle archetypes
  - Some potions have different bottle shapes, to better help visibility for those with colorblindness
  - These archetypes are:
    - Conical
    - Spherical
    - Vial
  - These archetypes are controlled via Data Tags
  - This design is to match Minecraft Dungeons
- Reverted the Uncraftable Potion texture to its pre-1.21 color (Magenta)

#### Blocks
- Changed Unlit Campfires to drop when broken with a Silk Touch tool

#### Loot
- Changed cows, mooshrooms, horses, llamas, mules, donkeys, and trader llamas to drop between 1 and 4 leather

### Technical Changes
#### Data Tags
- Added `conical` potion tag
  - By default, includes Strength, Weakness, Slow Falling, Wind Charging and Decay potions
- Added `spherical` potion tag
  - By default, includes Water Breathing, Oozing, Infestation, and Turtle Master potions
- Added `vial` potion tag
  - By default, includes Swiftness, Slowness, Leaping, and Weaving potions

#### Bug Fixes
- Fixed bug preventing animals seeking shelter from walking around
- Fixed bug causing the vanilla Title Screen icon buttons to be placed incorrectly