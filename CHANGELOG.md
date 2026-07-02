# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)

## [5.0] - UNRELEASED
### New Features
#### Player-Tailored World Generation
- Added the Tailored World Generation system
- During world creation, a "Default" world type can now be customized
- Choosing the "Customize" option allows the player to modify sliders corresponding to world generation factors
  - Current customizable options:
    - Continent Scale
    - Biome Scale
    - Master World Generation Scale

    Developer's Note: Ocean Depth is planned, but for a future update. We tried it, and ultimately decided to wait because

    1) It significantly harmed performance for various reasons
    2) Leaks suggest Mojang intends to update the ocean relatively soon
    3) What fun is a deep ocean with nothing new in it yet :)

#### Cauldron Stews
- Stews can now be made in cauldrons using their crafting ingredients
- Once an ingredient is added, the cauldron's stew becomes a Light Stew
  - These can also be eaten, though have half the nutrition as a full crafted stew
- One cauldron can hold 3 bowls of stew
- Light Stews made with flowers that would be used to make a suspicious stew, do not grant status effects

#### Items
- Added Light Stew
- Added Daggers
  - Deals 4 attack damage by default
  - Can deal up to an extra 4 damage depending on how discrete the user is being
  - Can be crafted with one stick and one tool material
  - Can be found as loot in any chest that may give a sword and in most chests that can give spears
- Added Quiver
  - Can hold up to 4 stacks of arrows
  - Automatically cycles through each arrow stack

#### Combat
- Added Blade Clash
  - When two attackers attack each other simultaneously (within 10 ticks), their weapons may clash, and do no damage to either of them
  - This currently only applies to swords and daggers
- Added Shield Strength
- Changed Iron Grates to have the `dragon_immune` block tag
  - Determines how much damage and knockback a shield can absorb
  - By default, Shields have a Shield Strength of 5

#### Enchantments
- Added Enervation enchantment
  - Can be applied to daggers
  - Applies up to 4 seconds of Weakness to victims at Level III
- Added Cleaving enchantment
  - This is to match the Combat Test Snapshots

#### Blocks
- Added Perse Wildflowers
  - Can be crafted into Purple Dye
  - Can be found in Birch Forests, Meadows, Swamps, and the Dappled Forest

    Developer Notes:
    - These are inspired by Forget-Me-Not flowers
    - "Perse" is an archaic term for "blue-ish"
- Added Wood and Stripped Wood stair and slab variants
- Added snowlogging, like on Bedrock Edition
  - Only some blocks are now snow loggable:
    - Plants
    - Fences
    - Fence gates
    - Walls
    - Metal bars
    - Glass panes
  - Snowlogged blocks may generate during world generation
  - Blocks may become snowlogged during snowfall
  - Some snowlogged blocks have snowy texture variants

#### Atmospherics
- Added ambient fog to Swamp biomes
- Added ambient fog to Mangrove Swamp biomes
- Added ambient fog to Pale Garden biomes
- Added ambient fog to Dark Forest biomes
- Added ambient fog to Jungle biomes
- Added ambient fog to Bamboo Jungle biomes

#### Advancements
- Added Marking Territory advancement
  - Is granted by using a map on a banner, to create a banner marker
- Added Gather 'Round advancement
  - Is granted by to light a campfire with a stick
- Added Soup-er! advancement
  - Is granted by adding stew ingredients to a cauldron

### Changes
#### Combat
- Changed natural regeneration to be twice as fast
- Changed starvation to be twice as fast
- Changed natural regeneration to continue to 7 food points
- Changed natural regeneration to be a 1:1 transaction with food points
- Changed Arrows to not trigger invincibility frames
- Changed Arrows to not inherit the Y-axis inertia of their shooter
- Changed Tipped Arrows to scale consistently with their potion effect
- Changed fast weapons to trigger less invincibility frame time
- Changed saturation to no longer be related to fast healing
- Changed saturation to drain before hunger
- Changed consuming to be interrupted by attacks
- Changed Bow and Crossbow uncertainty to be lower
- Changed critical hits to be possible while sprinting
- Changed critical hits to be impossible for arrows that have been notched for longer than 3 seconds
- Changed Axes to accept Sweeping Edge, Looting, and Fire Aspect
- Changed Mace to accept Sweeping Edge, Knockback, and Looting
- Changed Swords to require Sweeping Edge for sweeping attacks
- Changed Axes to no longer take increased durability damage when attacking
- Changed attacking with weapons to prioritize entities over blocks, such as grass

#### Discrete Actions
- Discrete actions, like sneaking, crawling, being invisible, or hiding in foliage, now conceal a player's location better
  - Being discrete now allows you to get close behind creatures without them detecting you, so long as you are not within their line of sight
- Having the glowing effect negates discrete actions
- Attacking a creature triggers a 5-second cooldown, in which you are no longer considered discrete
- Crawling now also affects the Locator Bar waypoint transmit range, like crouching

#### World Generation
- Changed all biomes to be larger
- Changed all continents to be larger
- Changed Oak Tree height
- Changed Birch Tree height
- Changed Short Grass placement chance
- Changed Bush placement chance
- Changed Snow to generate under trees in snowy biomes
- Changed Super Birch Trees to have Shelf Mushrooms generate on their trunks

##### Forest
- Changed understory to have sparse rocks
- Changed understory to have more grass
- Changed mob spawning to include rabbits
- Changed fallen logs to potentially have moss carpet generate on top
- Changed mushroom generation rate

  Developer's Note: I didn't even know that mushroom patches could spawn

##### Birch Forest
- Changed fallen logs to potentially have moss carpet generate on top

##### Dark Forest
- Changed understory to have sparse rocks
- Changed canopys to be larger
- Changed dark oak trees to be taller
- Changed the sky to be darker
- Changed the ratio of Dark Oaks trees to other vegetation and huge mushrooms
- Changed red mushroom caps to sometimes be slightly shorter
- Added small mushroom rings around huge mushrooms
- Changed mushroom generation rate

##### Dappled Forest
- Changed understory to have Perse Wildflowers

##### Swamp
- Added perse wildflowers
- Changed frog spawn rates in Swamp biomes to be higher
- Changed mushroom generation rate
- Added mud to the bottom of Swamps

##### Taiga
- Changed vegetation to be more common

##### Plains
- Changed mushroom generation rate

##### The End
- Changed Chorus Flowers to have an 85% chance of being alive
- Increased Chorus Flower growth and death volume

#### Structures
##### End Spikes
- Changed End Spikes to be more triangular

#### Mobs
- Added wall climbing to Silverfish AI
- Added wall climbing to Endermite AI

#### Items
- Changed Pottage to accept any type of edible mushroom

#### Blocks
- Changed doors to be water loggable
- Changed Brown Mushroom Blocks to have a light level of 1, to match the small brown mushroom
- Changed Shelf Mushroom to be edible
- Changed Powder Snow to decrease mining speed
- Changed Iron Grates to have the `dragon_immune` block tag

#### Crafting
- Changed map crafting recipe
  - Changed map crafting recipe to be a 9x9 of paper
    - This matches Bedrock Edition

#### Loot
- Changed Swamp Hut cauldron potions to be a data-driven loot table

#### Textures
- Changed the Leaf Litter texture to have fewer leaves and (hopefully) appear more natural
- Changed Illusioner texture to match Minecraft Dungeons

#### Sounds
- Changed Bush ambient sound to be louder
- Changed Sand ambient sound to be louder
- Changed dry vegetation ambient sound to be louder
- Changed ambient leaf block ambient sounds to be louder
- Changed Dead Bush ambient sound to be louder
- Changed Bush ambient sound subtitle to match the vanilla dry grass subtitle
- Changed ambient cold wind sounds to be less common

#### Advancements
- Changed "Traveler" advancement name to "Tentative Accommodations"

#### Splash Text
- Added "Bigger! Better!" splash text
- Added "Perse!" splash text
- Added "Any shape and size!" splash text

### Technical Changes
#### Particles
- Added `blade_clash` particle

#### Statistics
- Added `damage_blocked_by_weapon` statistic

#### Attributes
- Added `shield_strength` attribute

#### Data Tags
- Added `is_foggy` biome tag
- Added `is_very_foggy` biome tag
- Added `has_cespitose` biome tag
- Added `triggers_ambient_bush_block_sounds` block tag
- Added `huge_glowshroom_can_place_on` block tag
- Added `stew_ingredient` block tag
- Added `daggers` item tag
- Added `can_parry_swords` item tag
- Added `can_parry_daggers` item tag
- Added `sweeping_weapon` item tag
- Added `tall_plants` block tag
- Removed `has_ambient_block_sounds` block tag
- Removed `skull_block` block tag

#### Bug Fixes
- Fixed bug causing a client/server de-sync when lighting a campfire with sticks
- Fixed bug where unknown_server.png (commonly known as pack.png) was stretched when there's an issue with a world thumbnail

## [4.0 Magical Madness Update] - 6/16/26
### New Features
#### Enchantments
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm
- Added Glyph Affinity enchantment
  - A treasure enchantment which increases the Enchantment Limit by a factor of 1.5
- Added Illager's Bane enchantment
  - Does increased damage to illagers
    - Each level adds 2.5 damage to an attack
  - Is incompatible with Bane of Arthropods, Sharpeness, Density, Breach, and Impaling
  - Is obtainable for enchanted books or equipment via villager trading
- Added Animal Armor enchantments
  - Horse armor may now be enchanted
  - Wolf armor may now be enchanted
  - Nautilus armor may now be enchanted

#### Curses
- Added Curse of Abrading
  - Affected equipment loses durability thrice as fast

#### Charged Tridents
- Tridents enchanted with Channeling can now be charged for 4 seconds to create a Charged Trident
- Charged Tridents can summon a lightning bolt regardless of the current weather
- Charged Tridents have a visual effect similar to that of Charged Creepers
- Tridents may only be charged if the user's dimension is capable of having weather

#### The Nether
- Huge Warped Fungus
  - Now have Warped Overhang
  - There is a 10% chance for Warped Roots to grow on top of the fungus cap
  - This design is inspired by Minecraft Dungeons!

#### Blast Fungus
- Blast Fungi can be crafted shapelessly with a Crimson Fungus and Warped Fungus
  - Once thrown, a blast fungus explodes on impact, doing no damage to surrounding blocks

#### Potions
- Added Potion of Decay
  - Can be brewed by adding a Wither Skeleton Skull to an Awkward Potion
- Added Elixir of the Coven
  - Removes all negative potion effects
  - Can only be obtained by killing a Witch in a Swamp
    - If killed in a Swamp biome, Witches have a 5% chance of dropping 1 bottle
    - If killed in a Witch Hut, Witches have a 50% chance of dropping 1 bottle

#### Potion Effects
- Changed entities with Fire Resistance to no longer be visually set on fire
- High enchantability (level 15 or higher) armors will turn invisible with the wearer
- Changed Jump Boost effect to be increased by half a meter
  - The common Potion of Leaping will now allow the player to leap 2 meters high by default

#### Cauldron Potions
- Added Potion Cauldron block entity
- Behavior mimics Bedrock Edition
  - Cauldrons will now hold potions inside of them
  - Tipped arrows may now be created using cauldrons
  - Have a maximum of 6 levels
    - However, there are still only 3 visual levels

#### Blocks
- Added Basalt Slab
- Added Soul Jack o'Lantern
  - Can be crafted with a carved pumpkin and soul torch
- Added Warped Overhang block
  - Generates beneath Warped Wart blocks
  - Obtainable with silk touch tools or shears
  - Can be used to craft Warped Wart Blocks
- Added Unlit Soul Campfire
  - Is given when the player picks up an unlit Soul Campfire block with Silk Touch

#### Music
- Added 7 new background music tracks:
  - Guldrum by Peter Hont
    - Plays in Nether Wastes
  - Warped Forest by Eugnosis
    - Plays in Warped Forests
  - Soulsand Valley by Rostislav Trifonov
    - Plays in Soul Sand Valleys
  - Ashes by Peter Hont
    - Plays in Basalt Deltas
  - Basalt Deltas by Peter Hont
    - Plays in Basalt Deltas
  - Crimson Forest by Eugnosis
    - Plays in Crimson Forests
  - Excuse by C418
    - Plays in Warped Forests and Nether Wastes
- Changed the unused Warped Forest mood sound "mood 8" to be used
- Changed Dalarna to have a chance to play on the main menu
- Music volume has been refactored
- Removed Soggy Swamp by Johan Johnson (Swamp, Jungle, Sparse Jungle)
  - Developer Note: This music track seems to try to create a specific feeling for the listener. One of the things that we think makes Minecraft music so great, is how neutral it is. A song may make you feel alone, inspired, alien, etc. This song didn't allow for this very much.

#### Advancements
- Added new "Traveler" adventure challenge
  - Awarded for sleeping in a tent at least 1 km away from spawn or in a different dimension

#### Loot
- Added Enchanted Horse Armor loot pool to Ancient Cities, End Cities, Nether Fortresses, and Stronghold Corridors
  - This replaces the unenchanted Horse Armor loot pool
- Changed cows, mooshrooms, horses, llamas, mules, donkeys, and trader llamas to drop between 1 and 4 leather

#### Sounds
- Added sound effects for Frosted Ice (ice created by the Frost Walker enchantment)
- Added new flaming arrow sound effect
- Added new Ghast fireball shooting sound  effect
- Added new Blaze fireball shooting sound effect
- Added new Ender Dragon fireball shooting sound effect
- Added sound effects for Lingering Potion Area Effect Clouds
- Added sound effects for Dragon's Breath Area Effect Clouds
- The Hardcore activate sound has been slightly altered

#### Settings
- Added Video Settings option for toggling the experimental UI changes

#### Splash Text
- Added "Music by Rostislav Trifonov!"
- Added "Music by Eugnosis!"

### Changes
#### Enchanting
- Changed Enchanting tables to accept carpets, potted plants, candles, chains, cauldrons, brewing stands, and chiseled bookshelves within the enchanting area
- Changed Anvil Enchanting
  - The "Too Expensive!" message has been replaced with "Unrepairable!"
    - The idea of the "Too Expensive!" repair limit is to mimic how, in real life, you can't keep repairing an object over and over, forever. While we are not removing this feature, we have decided to rename it to help make this more clear.
  - The limit for adding enchantments is now tied to enchantability
    - The message for an item with too many enchantments is "Magic Capacity Met!"
  - Repairing an item enchanted with mending will have an enchantment level cost of 1.
  - Enchanting and repairing an item enchanted with mending will have a decreased enchantment cost
    - This cost is reduced by 4 levels, to a minimum of 1 level.
  - Repairing and Enchanting in an anvil have been split into two numbers.
    - Repair Cost (also known as the Prior Work Penalty): Functions the same as it did before, but now, enchanting does not increase the Repair Cost
    - Magic Level: Is a separate number that increases each time an item is enchanted. An item can be enchanted until you reach the magic limit, as mentioned above.

#### Enchantments
- Changed Piercing to reduce the effectiveness of armor by 10% with each level
- Changed Impaling to affect all mobs that are in contact with water for parity with Bedrock Edition

#### Potions
- Changed liquid consumables to be consumed faster!
  - The time has been reduced to 20 ticks, to match the Combat Tests
- Changed consumable potions to be stackable up to 16 items
  - This is to match the combat tests
- Changed potions naming to be based on their enhancement
  - Example: A Potion of Swiftness that lasts for 8 minutes is now called a "Long Potion of Swiftness"
  - Example: A Potion of Swiftness that provides Speed II is now called a "Strong Potion of Swiftness"
  - This is based on their internal code names

#### The Wither
- Changed launched Wither Skulls to be on fire
  - This was originally intended by Mojang, but caused crashes
  - The skull flames are blue, to match Soul Fire
  - These skulls now place Soul Fire when they explode
- Changed Wither health to scale with difficulty to match Bedrock Edition
  - Easy 300 HP -> 300 HP
  - Normal 300 HP -> 450 HP
  - Hard 300 HP -> 600 HP
- The Wither now dive bombs and summons Wither Skeletons when it has reached 50% health
  - This is also to match Bedrock Edition
- All entities with the `wither_friends` entity type tag will no longer target the Wither

  Note:  The Bedrock Edition's Wither "Dash" behavior was purposefully ignored.

#### Beacons
- Changed Beacon effect range to be 4 times as high
  - Level 1 Beacon Range:
    - 20 meters -> 80 meters
  - Level 2 Beacon Range:
    - 30 meters -> 120 meters
  - Level 3 Beacon Range:
    - 40 meters -> 160 meters
  - Level 4 Beacon Range:
    - 50 meters -> 200 meters
  - This helps balance a more difficult Wither boss battle

#### Structures
- Changed Swamp Hut cauldrons to now have a random potion effect inside of them, to match Bedrock Edition

#### Agricultural XP
- Changed fully grown crops to drop 0-2 XP when reaped

#### Blocks
- Changed overworld mushrooms to always be placeable on logs
- Changed Unlit Campfires to drop when broken with a Silk Touch tool

#### Textures
- Changed Dragon Fireball texture to match Bedrock Edition
- Changed Fire Charge/Fireball texture
- Changed texture for entities on fire that are Wither Skulls, in Soul Fire, on Soul Sand, on Soul Soil, or in Soul Sand Valleys
- Added potion bottle archetypes
  - Some potions have different bottle shapes to better help visibility for those with colorblindness
  - These archetypes are:
    - Conical
    - Spherical
    - Vial
  - These archetypes are controlled via Data Tags
  - This design is to match Minecraft Dungeons
- Reverted the Uncraftable Potion texture to its pre-1.21 color (Magenta)

### Technical Changes
#### Camera Shake Events
Camera Shake events are now data-driven, similar to sound events. Camera shake event files are to be put in a `data/[datapack namespace]/subtlyd/camera_shake_event/` folder.
- Added `sound_event` field: a Sound Event ID, specifies what sound event should trigger the Camera Shake Event
- Added `range` field: a float, specifies the maximum distance that the camera shake can be experienced from
- Added `duration` field: an integer, specifies the amount of ticks that the camera shake will last
- Added `intensity` field: an optional float, specifies the intensity modifier value
  - Default: `4`

#### Data Tags
- Added `liquid_consumables` item tag
  - This is a list of consumables that can be considered liquids (e.g. stews, potions, etc.)
- Added `is_occult` damage type tag
  - This tag controls what types of damage Occult Protection can protect against
    - This includes magic, indirect magic, sonic booms, withering, and thorns
- Added `non_humanoid_armor` item tag
  - This tag is a list of armor items not for humanoids (e.g. diamond horse armor, wolf armor, etc.)
  - Any item with this tag will become enchantable with armor enchantments.
- Added `increases_magic_limit` enchantment tag
  - These enchantments increase the magic level limit of the item and decrease how quickly an item's magic level will increase
- Added `repairs_equipment` enchantment tag
  - These enchantments are incompatible with the abrading curse
- Added `conical` potion tag
  - By default, this includes potions of Strength, Weakness, Slow Falling, Wind Charging, and Decay
- Added `spherical` potion tag
  - By default, this includes potions of Water Breathing, Oozing, Infestation, and Turtle Master
- Added `vial` potion tag
  - By default, this includes potions of Swiftness, Slowness, Leaping, and Weaving

#### Enchantiblity
- The enchantability difference of two items in an anvil will scale the enchantment cost cap.
  - Enchantability difference is calculated as `|enchantability of item 1 - enchantability of item 2|`
  - The enchantment level cap is calculated by this formula: `(enchantability difference * 2) + 40`
    - Example: A diamond helmet (Enchantability level 10) in an anvil with an enchanted book (Enchantability level 0) will have a maximum enchantment cost of 55
      - If the cost gets any higher than this, the anvil will say "Unrepairable!"
    - The enchantment cap is always rounded up
    - The maximum enchantment level cap is now 100 (which can be achieved by combining two pieces of golden armor)
- Elytras now have an enchantability level of 9

- #### Entity Data
- Custom Entity Data has been changed to use pascal case, rather than snake case
  - e.g. The command for summoning a leader zombie is now `/summon zombie ~ ~ ~ {IsLeader:1b}`

#### Stats
- Added `Times Slept in a Tent` statistic

#### Sounds
- Added new sound event for the Wither:
  - `entity.wither_skeleton.summon`
- Added new sound event for Evoker Fangs:
  - `entity.evoker_fangs.appear`
- Added new sound event for Lingering Potion Area Effect Clouds:
  - `entity.area_effect_cloud.gas`
- Added new sound event for Dragon's Breath Area Effect Clouds:
  - `entity.ender_dragon.breath`

#### Textures
- Added Hardcore HUD hearts for the Poison effect
- Added Hardcore HUD hearts for the Wither effect

#### Particles
- Added `spore_cloud` particle

#### Credits
- Added ourselves (and the talented artists that make the mod's music) to the game's end credits!

#### Bug Fixes
- **MC-84595** - Fixed bug where deaths by dragon's breath were referred to as "magic"
  - It now properly states that "[Player] was roasted in dragon's breath"
- Fixed some memory leaks
- Fixed flaming arrows not setting fire to objects when shot east or west
- Optimized music tracks loading
- Optimized music track size
- Fixed Netherite items with custom names not inheriting a new rarity value
- Fixed a possible bug where reeds could prevent trees from spawning
- Fixed bug that made Killer Bunnies try to find shelter from weather
- Fixed bug that prevented custom options from saving after exiting the game
- Fixed bug preventing animals that are taking shelter from walking around
- Fixed bug allowing animal herds to panic even if the danger is environmental and unable to hurt them
- Fixed a bug causing the game to crash on launch for some players

## [3.0 Wild Instincts Update] - 3/24/26
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

## [2.0] - 12/29/2025
#### Developer Notes:
Oh boy. Mojang changed the Minecraft version system... which made me reconsider our current versioning system (SemVar). Semantic Versioning is specifically for APIs (A.K.A. not a mod), so I think it would make sense to change up the versioning system for the mod as well. `MAJOR.MINOR.PATCH` isn't bad, though part of what this mod aims to do, is to re-capture the spirit of old Mojang (which includes having more "random" updates, that don't necessarily add groundbreaking changes (i.e. I would never want to bump the `MAJOR` number).

Because of this, I think it's most fitting that going forward we will adopt a `UPDATE.HOTFIX-BUILD` system. The Minecraft version suffix will also be dropped, for a few reasons:

1) Fabric will warn you if you install on the wrong version
2) The websites that this mod is currently available on (Modrinth and Curseforge) have filtering systems for the correct Minecraft version
3) The mod intends to have a "forward-looking" development cycle, so you can expect updates to focus on whatever the newest update is.

An example using this current update is: `2.0`. If a snapshot or beta is openly released, you can expect a suffix (e.g. `2.0-snapshot` or `2.0-beta`) 

### New Features
#### UI
- Added a render of the player on the Title Screen

#### Visuals
- Added a breaking animation for tents

### Changes
#### Visuals
- The method for rendering zombie leaders has changed to increase vanilla and mod compatibility
- Changed the camera angle for sleeping in a tent to look straight up

#### Mechanics
- The custom zombie leader health boost mechanics have been removed in favor of the vanilla implementation
- Changed the hitbox of tent entities to be more precise

#### Data
- Changed Screen Shake translation keys
  - `options.screen_shake` is now `options.accessibility.screen_shake`
  - `options.screen_shake`.tooltip is now `options.accessibility.screen_shake.tooltip`
- Added tent entity translations

### Bug Fixes
- Fixed a bug where Stone Pillar blocks could not be broken by a pickaxe
- Fixed a bug where tent entities lacked display names

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