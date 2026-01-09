## [3.0-snapshot-2] - 1/9/2026
### New Features
#### Creepy Crawlies
- Spiders now climb up walls and ceilings like in the trailers (and Minecraft Dungeons)!

#### Consumable Mushrooms
- Overworld mushrooms are now once again (yes, they used to be in vanilla Minecraft) edible!
  - Brown mushrooms make a great snack, but red mushrooms will give you poison for 3 seconds

#### Zombie Leaders
- Added a unique Zombified Piglin leader texture (from Minecraft Dungeons)
- Updated zombie leader texture to use a modified version of the "Lobber Zombie" texture (from Minecraft Earth)

### Music
- New Underwater Music!
  - Rest in Reefs by Peter Hont
  - Coral Rise by Peter Hont
  - The Bilge by Peter Hont
  - Hydrothermal Vent by Peter Hont
  - Twilight Cavern by Peter Hont
  - The Abyssal Monument by Grant Kirkhope
  - Radiant Ravine by Grant Kirkhope
  - Molten Monument by Grant Kirkhope
  - Tropical Slime Scramble by Peter Hont
  - Primal Oil Sect by Peter Hont

### Visuals
- Added Mace Smash Air, Mace Smash Ground, Mace Smash Ground Heavy, and End Flash screen shake event
- Removed the Ender Dragon Growls screen shake event
  - This is due to this event being triggered server side (i.e. independent of what the player's actually hearing)

#### Splash Text
- Added "We <3 spiders!" splash text
- Added "Music by Grant Kirkhope!" splash text

### Technical Changes
#### Data Tags
- The "dripstone" block tag is now "polished_dripstone"
- The `tag.item.subtlyd.tent` tag is now `tag.item.subtlyd.tents`
- Added translations for `Dripstone`, `Stone Tiles`, `Skulls`, and `Triggers Ambient Wind Block Sounds` block tags

### Bug Fixes
- Fixed a bug causing players that "spam" slept in beds to skip to day
- Improved server performance for using tents
- Sleeping in tents will no longer negate fall damage