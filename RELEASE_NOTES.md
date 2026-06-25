## [5.0 Snapshot 1] - UNRELEASED
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
- Added weapon clashes
  - When two attackers attack each other simultaneously (within 10 ticks), if they are weilding the same weapon, their weapons may clash, and do no damage to either of them
  - This currently only applies to swords and daggers
- Added Shield Strength
  - Determines how much damage and knockback a shield can absorb
  - By default, Shields have a Shield Strength of 5

#### Enchantments
- Added Enervation enchantment
  - Can be applied to daggers
  - Applies 2 seconds of Weakness to victims
    - For each level, this time increases by 1 second
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
- Changed Maces to accept Sweeping Edge
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
- Changed Oak tree height
- Changed Birch tree height
- Changed grass placement chance
- Changed bush placement chance
- Changed snow to generate under trees in snowy biomes

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

#### Arthropod Pathfinding
- Added wall climbing to Silverfish AI
- Added wall climbing to Endermite AI


#### Items
- Changed Pottage to accept any type of edible mushroom

#### Blocks
- Changed doors to be water loggable
- Changed Brown Mushroom Blocks to have a light level of 1, to match the small brown mushroom
- Changed Shelf Mushroom to be edible
- Changed Powder Snow to decrease mining speed

#### Crafting
- Changed map crafting recipe
  - Changed map crafting recipe to be a 9x9 of paper
    - This matches Bedrock Edition

#### Loot
- Changed Swamp Hut cauldron potions to be a data-driven loot table

#### Textures
- Changed the Leaf Litter texture to have fewer leaves and (hopefully) appear more natural

#### Sounds
- Changed Bush ambient sound to be louder
- Changed Sand ambient sound to be louder
- Changed dry vegetation ambient sound to be louder
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
- Removed `has_ambient_block_sounds` block tag
- Removed `skull_block` block tag