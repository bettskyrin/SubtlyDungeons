## [4.0 Snapshot 4] - UNRELEASED

#### Warning
Some of the 4.0 items have had their item IDs altered, meaning these items will disappear from your worlds. These items are of uncommon rarity, so we believe this shouldn't disrupt many people's worlds, if any at all.
These items are the Elixir (now called the Elixir of the Coven) and the Potion of Decay

### New Features
#### Blocks
- Added Unlit Soul Campfire
  - Is given when the player picks up an unlit Soul Campfire block with Silk Touch

### Changes
#### Elixirs
- Changed Elixir to be named Elixir of the Coven

#### Potion Effects
- Changed Jump Boost effect to be increased by half a meter
  - The common Potion of Leaping will now allow the player to leap 2 meters high by default

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
  - By default, this includes potions of Strength, Weakness, Slow Falling, Wind Charging and Decay 
- Added `spherical` potion tag
  - By default, this includes potions of Water Breathing, Oozing, Infestation, and Turtle Master
- Added `vial` potion tag
  - By default, this includes potions of Swiftness, Slowness, Leaping, and Weaving

#### Bug Fixes
- Fixed bug preventing animals seeking shelter from walking around
- Fixed bug causing the vanilla Title Screen icon buttons to be placed incorrectly
- Fixed bug giving Potions of Decay an improper namespace ID