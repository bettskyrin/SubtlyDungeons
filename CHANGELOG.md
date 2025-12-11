# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)

## [1.1.1] - 12/11/2025
### Fixed
#### Textures
- Fixed item texture bug

## [1.1.0 Winds and Wetlands Update] - 12/9/2025
### Changed
#### Textures
- Updated reeds texture

## [1.1.0-alpha.5] - UNRELEASED
### Added
#### Data
- Added "burns_tents" damage tag
- Added "ignites_tents" damage tag

### Changed
- Changed tent entity rendering code
- Updated tent sleep timing method to sync with the vanilla game properly

### Fixed
- Fixed bug where custom splashes were colored white
- Fixed bug where repeatedly stopping and starting tent sleep would skip to daytime

### Deprecated
- Removed unused "is_windy" biome tag translation

## [1.1.0-alpha.4] - 12/3/2025
### Developer Notes & Technical Stuff
I plan to phase out using SemVer for various reasons. Semantic Versioning is for public APIs (which this is not), and I would like for my mod to follow a system like Mojang's, so that its versioning is familiar.
This will take effect in 2026. A new system will be announced on my Twitter or Reddit page, so keep an eye out.

I also want to say that a lot of code has been getting cleaned up to be more efficient and compatible with other mods, so a lot of the content has slowed down.

### Added
#### Visuals
- Added dragon fireball explode screen shake effect

### Changed
#### Blocks
- Reeds now generate more often

#### Textures
- Changed reeds texture to no longer be tinted by the average biome vegetation color
- New tooltip frame and background design
- New experience bar texture
- New ping textures

#### Menus
- Changed the Title Screen layout
    - Accessibility and language buttons are now in the bottom left, replacing the game version (which can be checked via the Debug Menu)
- Changed the World Selection Screen button layout
- World selection list items now take up most of the screen
- World icons are now 16:9 (455px x 256px) thumbnails
    - They also update every time the game saves, similar to Bedrock Edition

#### Mechanics
- Changed Netherite Tools and Armor to have "Uncommon" rarity level
  - This is because Netherite Upgrade Templates are Uncommon
- Changed Lingering Potions to have "Uncommon" rarity level
  - This is because Dragon's Breath is Uncommon
- Changed Tipped Arrows to have "Uncommon" rarity level
    - This is because Lingering Potions are now Uncommon
- Changed Ominous Trial Keys to have "Uncommon" rarity level
    - This is because Ominous Bottles are Uncommon
- Changed Wither Rose to have "Rare" rarity level
  - This is because Wither Skeleton Skulls are Rare

### Fixed
#### Blocks
- Reeds can now be properly bone mealed

## [1.1.0-alpha.3] - 11/23/2025
### Added
#### Blocks
- Added Reeds
  - Reeds are a common plant that can be found in shallow swamp biome waters

#### Tags
- Added "triggers_ambient_wind_block_sounds" block tag
- Added "tents" item tag

### Changed
#### Audio
- Lowered the volume of Wanderlust, Windswept Peaks, Cliffs and Canyons, Cellar, Finnbacka, Top, and Halland
- Slightly lowered the weight of Soggy Swamp playing in swamp biomes
- Altered how often cold wind ambient noises play

#### Events
- Raid difficulty logic has changed
  - "Raid difficulty" is a new value that is calculated as follows: `Raid Omen Level + Difficulty Level ID (0-3) + Raid Wave = Raid Difficulty`
    - For example, if you trigger a Level III Raid on Easy Mode, your Raid Difficulty on the second wave is `3 + 0 + 2 = 5`
  - Raid difficulty must reach level 10 before pillagers are able to spawn with Flame enchanted crossbows
    - i.e. Normal Difficulty with a raid omen of at least V, on wave 3 or higher OR Hard Difficulty with a raid omen of at least 4, on wave 3 or higher
  - Raid difficulty must reach level 7 for raid captains to spawn with Resistance II
    - Captains may still spawn in with Resistance I if the raid difficulty is at least level 4

#### Data
- `options.doCameraShake` has been renamed to `options.screen_shake`
  - Camera Shake has been renamed "Screen Shake" to match Minecraft Dungeons
- `options.doCameraShake.tooltip` has been renamed to `options.camera_shake.tooltip`
    - Its value has been changed to "Toggles the screen shake effect." to match Minecraft Dungeons

#### Tags
- Removed "tents" block tag

## [1.1.0-alpha.2] - 11/1/2025
### Added
#### Blocks
- Added Block of Charcoal
  - Works the same as a coal block
- Added Iron Grate
  - Can be crafted or cut from iron blocks
  - Is waterloggable
- Added Chiseled Polished Dripstone
- Added Chiseled Stone
- Added Polished Dripstone
    - Added stair, slab, and wall variants
- Added Stone Pillar
- Added Stone Tiles
  - Added stair, slab, and wall variants
- Added snow brick wall and snow brick family stonecutting recipes

#### Sounds
- Added sound for lighting a campfire with sticks
- Added new music
  - Secrets in the Forest by Crispin Hands (Menu, Meadow, and Flower Forest)
  - Cacti Canyon by Johan Johnson (Badlands)
  - Creeper Pit by Peter Hont (Jungle)
  - Desert Temple by Johan Johnson (Desert)
  - Fizz by Johan Johnson (Dripstone Caves, Badlands, Eroded Badlands, Desert)
  - Halland by Johan Johnson (Menu, Cherry Grove, Flower Forest, Old Growth, Meadow, Lush Caves)
  - Haven by Johan Johnson (Deep Dark, Dripstone Caves)
  - Intertile by Peter Hont (Dripstone Caves)
  - Pumpkin Pastures by Johan Johnson (Badlands)
  - Skogsstuga by Peter Hont (Desert and Badlands)
  - Soggier Cave by Johan Johnson (Badlands)
  - Soggy Swamp by Johan Johnson (Swamp, Jungle, Sparse Jungle)
  - Squid Coast by Johan Johnson (Jagged Peaks, Stony Peaks, Dripstone Caves)

#### UI
- Added new splash texts
  - Added "Pretty tents!"
  - Added "R.I.P. trout.png"
    - A developer inside joke, as it was decided to not add trouts to the mod
  - Added "L-l-l-lava!"
  - Added "Music by Peter Hont!"
  - Added "Music by Crispin Hands!"
  - Added "Music by Johan Johnson!"
  - Added "Welcome back Dinnerbone!"
    - Referencing the Mojang developer Dinnerbone's return to development, following a mental health leave
  - Added "Windy!"

### Changed
#### Items
- Tents can now be used as furnace fuel
  - Can cook up to 3 items per tent

#### Textures
- Changed all tent entity textures
- Changed all tent item textures
- Changed default filled map texture

#### Sounds
- Howling winds are slightly less common
- Cliffs and Canyons by Crispin Hands now plays in the menu
- Wanderlust by Peter Hont now plays in the menu
- Adjusted biome specific track weights so that they play more often

#### Gamerules
- Changed "doArrowArson" to "arrow_arson"

## [1.1.0-alpha.1] - 10/26/2025
### Added
#### Blocks
- Added Snow Brick block family
  - Snow Bricks
    - Crafted from 4 snow blocks
    - Best mined with a pickaxe
  - Snow Brick Stairs
      - Crafted from 6 snow bricks block
  - Snow Brick Slab
      - Crafted from 3 snow bricks block

#### Sounds
- Added new music
    - The Green Expanse by Crispin Hands (Game and Menus)
    - Top by Peter Hont (Snowy Slopes and Frozen Peaks)
    - Cliffs and Canyons by Crispin Hands (Meadows and Game)
    - Wanderlust by Peter Hont (Game)
    - Cellar by Johan Johnson (Deep Dark)
    - Windswept Peaks by Peter Hont (Frozen Peaks and Snowy Slopes)
    - Finnbacka by Peter Hont (Stony Peaks)
- Added unique sounds for Snow Brick type blocks

#### Gamerules
- Added doArrowArson
  - Can prevent flaming arrows from setting fires
    
#### Tags
- Added new tags
  - Added "Tents" item tag
  - Added "snow_bricks" item tag
  - Added "snow_bricks" block tag
  - Added "skull_block" block tag
  - Added skull blocks to "mineable/axe" tag

#### Accessibility
- Added new accessibility setting option for turning off the camera shake effect

### Changed
#### Sounds
- Changed bush ambient wind noise to play less often
- Ice can now trigger ambient wind noises
- Increased volume of ambient bush and cold wind noises
- Changed "Wind blows" to "Wind howls"
- Changed "Wind blows gently" to "Wind blows"

#### Textures
- Changed orange, yellow, brown, lime, cyan, light blue, and magenta tent entity colors

### Fixed
- Fixed fishing so that now fishing for fish ACTUALLY doesn't work in the end

## [1.0.0 Simply Survival Update] - 10/17/25
### Added
#### Sounds
- Added bush wind ambiance
- Added cold wind ambiance to more cold biomes

### Removed
- The music track "Alone with the Sky" no longer plays in forest-type biomes

## [1.0.0-alpha.4] - 10/17/25
### Added
#### Sounds
- Added 3 new songs
  - Droopy Likes Your Face by C418 (Plays in Creative Mode)
  - Droopy Likes Ricochet by C418 (Plays in Creative Mode)
  - Dalarna by Peter Hont (Plays in forest-type biomes)
  - Alone with the Sky by Crispin Hands (Plays in forest-type and grove biomes)
- Added cold wind ambience
    - Plays in very cold or mountainous biomes
        - Excludes the End Dimension
- Added new Ravager roar and bite sounds

## [1.0.0-alpha.3] - 10/15/25
### Added
#### Food
- Added Pottage
  - Crafted from a wheat sheaf, carrot, brown mushroom, and bowl
  - Restores 6 hunger points and 7.2 saturation points

#### Mechanics
- Unlit Campfire
- Named "Campfire". The original "Campfire" has been renamed to "Lit Campfire"
    - Can be crafted from sticks and logs
    - Can be lit traditionally or with sticks (with a 70% chance to fail)
- Fishing is now biome dependent! There's now strategy involved
  - If the fish can't be found swimming around in the water, you probably can't fish for it!
    - e.g. You won't find salmon in mangrove swamps, only tropical fish, so that's what you'll get from fishing
    - Salmon can be found anywhere above ground, but chances are increased in rivers and some ocean biomes
    - Cod love cold and/or deep waters
    - Tropical fish and pufferfish love warm waters
    - Squid can be found in rivers and oceans
  - You can not fish in The End dimension anymore

### Changed
- Doubled "Warden emerge" camera shake distance

## [1.0.0-alpha.2] - 10/11/2025
### Fixed
- Fixed ordering of tent item variants in the creative inventory
- Fixed apple pies not crafting with blue and brown eggs

## [1.0.0-alpha.1] - 10/11/2025
### Added
#### Mechanics
- Zombie hoard leaders now have increased health
  - This fixes a long-running bug
  - The zombie leader's health scales based on local difficulty (i.e. they get stronger the more you play), ranging from 10 Hearts (20HP) to 50 Hearts (100HP)
      
- Raids have been tweaked
  - Pillagers may now shoot flaming arrows
    - This is only on Normal difficulty with Level V raids or Hard difficulty with Level IV or V raids
    - The likelihood of pillagers being able to shoot flaming arrows scales with the game difficulty
      - Though for all difficulties there's a 50% chance the enchantment pillagers spawn with, is flame
  - Raiders now get a boost of resistance once they become a captain
    - This scales with difficulty and raid level
    
- Flaming arrows can now ignite flammable materials
  - This is to make raids more dangerous and dynamic :)
  - Flaming arrows shot onto the east and west side of flat surfaces don't cause fire
    - "Lore" reason: Wind only blows north to south
    - Meta reason: There's a bug in Mojang's code preventing proper block detection
    - This can help balance out flaming arrows a little
  - Works with mobGriefing

- Tent
  - Can be slept in at nighttime
  - Does not reset your spawn point
  - Has 16 color variants

#### Food
- Apple Pie
  - Can be crafted with an apple, sugar, and egg
  - Restores 8 hunger points
  - Restores 4.8 saturation points
  - There's a chance to find it in plains villager houses
  - Heroes of the Village have a chance to be thrown one by farmer villagers

- Calamari
  - Drops from squid, glow squid, or, more rarely, polar bears
  - Can be fished for
  - Restores 3 hunger points
  - Restores 0.6 saturation points
  - Can be fed to cats and wolves
  - There's a chance to find it in fisher cottages
  - Heroes of the Village have a chance to be thrown one by fisherman villagers

- Cooked Calamari
    - Drops from burning squid, glow squid, or, more rarely, polar bears
    - Can be cooked from calamari
    - Restores 5 hunger points
    - Restores 6 saturation points
    - Can be fed to wolves
    - There's a chance to find it in fisher cottages
    - Heroes of the Village have a chance to be thrown one by fisherman villagers

#### Visuals
- Added camera shake events
  - Loud events can now shake the camera
  - This applies to: Ravager roars, Ender Dragon roars and growls, the Warden roar, emergence, and sonic shriek, end gateway creation, lightning strikes, and explosions

- Leader zombies, drowned, and husks have a unique texture to help differentiate them from the normal versions