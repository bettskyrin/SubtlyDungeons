## [3.0 Snapshot 7] - 3/11/2026
You guys asked for a few new things so we went ahead and added them! This should be the final snapshot before a pre-release (or full release, if we don't find any bugs). See you all at Minecraft LIVE!

### New Features
#### Predators & Prey
- Predators now have a cooldown for hunting
    - Mobs that have a "feast or famine" hunting style will wait 3 in-game days before hunting again.
- Nocturnal predators will now only hunt at night
- Predators now consume the meat from animals they hunt
- Predators now heal when consuming meat
- Herbavores now heal when grazing
- Changed squids to be more likely to avoid Dolphins
- Changed dolphins to be more likely to hunt squid
- Polar Bears will now hunt for salmon and cod

#### Cozy Creatures
- Added polar bears to the list of creatures that look for shelter in the rain
- Added pandas to the list of creatures that try to cool down
- Removed polar bears, llamas, trader llamas, and pandas from the list of creatures that look for warmth
- Temperate variant animals no longer seek warmth in cold biomes unless it is actively snowing

### Visuals
- Updated Stone Pillar texture
- Updated Stone Tiles texture

### Technical Changes
#### Loot
- Removed Calamari from Polar Bear loot table

#### Data Tags
- Added `can_be_full` entity type tag
  - This tag determines whether an animal species will have a cooldown for hunting
- Added `nocturnal` entity type tag
  - This tag determines whether an animal species is nocturnal
    - This affects things like hunting times
- Added `feast_or_famine_hunter` entity type tag
  - This tag determines whether an animal species will have a 3-day-long hunting cooldown

### Bug Fixes
- Decreased Spider step sound effect frequency when climbing
- Fixed bug preventing camera shake from re-applying to shake events of the same intensity
- Rollbacked Zombie leader method to prevent Zombies with the Health Boost effect from being included