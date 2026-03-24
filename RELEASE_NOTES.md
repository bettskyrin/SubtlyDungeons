## [3.0 Wild Instincts Update] - UNRELEASED
Coming in hot alongside the Tiny Takeover is the Wild Instincts Update! This update brings a bit more life to the creatures of your world! Spot spiders climbing eerily up walls, see your farm animals look for a shelter when they're caught in the rain, and run wild with your pets like never before! Just be sure to check out the rest of the new features below. Happy exploring!

### New Features
#### The Food Chain
- Predators now have a cooldown for hunting
  - Mobs that have a "feast or famine" hunting style will wait 3 in-game days before hunting again.
- Nocturnal predators will now only hunt at night
  - This includes the Wolf, Fox, and Ocelot
- Predators now consume the meat from animals they hunt
- Predators now heal when consuming meat
- Herbivores now heal when grazing

#### Creepy Crawlies
- Spiders now climb up walls and ceilings like in the trailers (and Minecraft Dungeons)!

#### Fleeing Fauna
- Some animals now run from attackers targeting their flock!
  - These flocks have a radius of 16 meters
  - When attacked, an animal within a flock will run away from the attack to a random location within 16 meters of its current location

#### Cozy Creatures
- Animals will now seek out more comfortable environments!
  - Animals caught out in the rain will look for shelter
  - Non-cold acclimated animals (or their variants) will now seek out warmer spots to hang around
  - Non-heat acclimated animals (or their variants) will now seek out cooler spots in hot environments

#### New Predators
- Dolphins now occasionally hunt for squid and cod!
  - Dolphins can also now be fed calamari
- Polar Bears will now hunt for salmon and cod!

#### Closer Pets
- Pets will now follow their owners for 20 meters before attempting to teleport!
  - This was increased from the default 12 meters
- Pets now sprint with their owners!

#### Consumable Mushrooms
- Overworld mushrooms are now once again (yes, they used to be in vanilla Minecraft) edible!
  - Brown mushrooms make a great snack, but red mushrooms will give you poison for 3 seconds

#### Zombie Leaders
- Added a unique Zombified Piglin leader texture (from Minecraft Dungeons)
- Updated Zombie leader texture to use a modified version of the "Lobber Zombie" texture (from Minecraft Earth)
- Updated Husk leader texture
- Added baby zombie, husk, zombie piglin, and drowned leader textures

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
- Updated Stone Pillar texture
- Updated Stone Tiles texture
- Updated the Iron Grate texture
- Updated the Reeds texture

### UI
- Redesigned the world creation screen! Some features from Bedrock edition have been brought over, with a Java spin on things
- Redesigned HUD textures
  - New heart textures
    - Including absorbing, poisoned, and withered
  - New armor bar textures
  - New experience bar texture
  - New jump bar textures
  - New hunger bar textures
  - New hotbar textures
  - New crosshair texture

### Advancements
- The advancement "Balanced Diet" now includes the new consumables

### Splash Text
- Added "We <3 spiders!" splash text
- Added "Music by Grant Kirkhope!" splash text
- Added "Also try Hytale!"
- Added "Also try Minecraft Legends!"
- Added "Wild!"

### Technical Changes
#### Data Tags
- The `dripstone` block tag is now `polished_dripstone`
- The `tent` item tag is now `tents`
- Added `can_be_scared` entity type tag
  - This tag determines whether an animal species will panic if a nearby animal takes damage
- Added `seeks_shelter` entity type tag
  - This tag determines whether an animal species will seek shelter from rain
- Added `can_seek_warmth` entity type tag.
  - This tag determines whether an animal species can seek warmth from cold environments
    - Note: Internal code determines if a specific animal variant should be considered "warm", "temperate", or "cold". There is not currently a data-driven method for making a custom mob variant fall into one of these categories
- Added `can_be_full` entity type tag
  - This tag determines whether an animal species will have a cooldown for hunting
- Added `nocturnal` entity type tag
  - This tag determines whether an animal species is nocturnal
    - This affects things like hunting times
- Added `feast_or_famine_hunter` entity type tag
  - This tag determines whether an animal species will have a 3-day-long hunting cooldown

#### Loot
- Removed Calamari from Polar Bear loot table

#### Translations
- Added translations for `Dripstone`, `Stone Tiles`, `Skulls`, and `Triggers Ambient Wind Block Sounds` block tags
- Changed `options.difficulty.peaceful.info`, `options.difficulty.easy.info`, `options.difficulty.normal.info`, and `options.difficulty.hard.info` references of "mobs" to "creatures."
- Changed `multiplayer.stopSleeping` translation from "Leave Bed" to "Stop Sleeping"

#### Commands
- Added `/camerashake` command
  - Usages are `/camerashake add <targets> <intensity> <seconds>` or `/camerashake stop <targets>`

### Bug Fixes
- Fixed a bug causing players that "spam" slept in beds to skip to day
- Improved server performance for using tents
- Sleeping in tents will no longer negate fall damage
- Squids once again drop calamari
- Fixed bug preventing camera shake from re-applying to shake events of the same intensity