# [5.0 Snapshot 5] - UNRELEASED
It's mod snapshot day (or night... depends on where you're from)! We have more performance upgrades alongside other small tweaks!

## New Features
### Blocks
- Added Terracotta Stairs
- Added Terracotta Slabs
- Added Dyed Terracotta Stairs
- Added Dyed Terracotta Slabs

### Blasting
- Adding Block of Raw Copper Blasting recipe
- Adding Block of Raw Iron Blasting recipe
- Adding Block of Raw Gold Blasting recipe

### Options
- Added Entity Culling option
    - Determines the entity culling method
    - Frustrum
      - The default culling method. Hides entities that are outside the player's FOV
    - Occlusion
      - Hides entities that are behind blocks or fog
      - Performs after frustrum culling

### Debug Screen
- Added `entity_culling_method` debug screen entry
- Added `visualize_entity_occlusion` debug renderer

## Changes
### World Generation
- Updated Baobab generation style to more closely match the version shown by Mojang

### Textures
- Changed Map texture to match the base game
- Changed the Adventure Mode texture to use the Buried Treasure Map texture

### Options
- Changed "Advanced Entity Animations" to "Fancy Entities"
- Changed custom options ordering

### Advancements
- Changed Subspace Bubble advancement to use the Filled Map texture
- Changed the Adventure root advancement to use the Buried Treasure Map texture
- Changed the Voluntary Exile advancement to use the Ominous Bottle texture
- Changed the Voluntary Exile advancement description, criteria, and parent

## Technical Changes
### Bug Fixes
- Fixed bug preventing some settings from being saved
- Fixed bug preventing quivers from displaying on armor stands
- Fixed bug allowing Withers to set fire when Destructive Mob Actions is set to "Off"
- Fixed Silk Touch and Shear block drops
- Fixed bug causing missing block family recipes