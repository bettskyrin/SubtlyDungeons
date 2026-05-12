## [4.0 Snapshot 3] - 5/12/26
Test the hottest new mechanics in Subtly Dungeons 4.0 snapshot 3! Pitch a tent far from home to earn the new Traveler advancement – just make sure you pack the all-new Elixir to cure any nasty curses along the way! A new Illager's Bane enchantment gives you the upper hand against woodland mansions, but who will you test it on first? It's up to you to find out! Finally, check out the fiery new visuals for Wither Skulls and enjoy a smoother experience with our latest game optimizations.

### New Features
#### Enchantments
- Added Illager's Bane enchantment
  - Does increased damage to illagers
    - Each level adds 2.5 damage to an attack
  - Is incompatible with Bane of Arthropods, Sharpeness, Density, Breach, and Impaling
  - Is obtainable for enchanted books or equipment via villager trading

#### Potions
- Added Elixir
  - Removes all negative potion effects
  - Can only be obtained by killing a Witch in a Swamp
    - If killed in a Swamp biome, Witches have a 5% chance of dropping 1 bottle
    - If killed in a Witch Hut, Witches have a 50% chance of dropping 1 bottle

#### Blocks
- Added Soul Jack o'Lantern
  - Can be crafted with a carved pumpkin and soul torch

#### Cauldron Potions
- Added Potion Cauldron block entity
- Behavior matches Bedrock Edition
  - Cauldrons will now hold potions inside of them
  - Tipped arrows may now be created using cauldrons

#### Sounds
- Added new flaming arrow sound effect
- Added new Ghast fireball shooting sound  effect
- Added new Blaze fireball shooting sound effect
- Added new Ender Dragon fireball shooting sound effect
- Added sound effects for Lingering Potion Area Effect Clouds
- Added sound effects for Dragon's Breath Area Effect Clouds

#### Advancements
- Added new "Traveler" adventure challenge
  - Awarded for sleeping in a tent at least 1 km away from spawn or in a different dimension

#### Settings
- Added Video Settings option for toggling the experimental UI changes

### Changes
#### Structures
- Changed Swamp Hut cauldrons to now have a random potion effect inside of them, to match Bedrock Edition

#### Piercing Enchantment
- Changed Piercing to reduce the effectiveness of armor by 10% with each level

#### Impaling Enchantment
- Changed Impaling to affect all mobs that are in contact with water for parity with Bedrock Edition

#### Abrading Curse
- Changed Abrading Curse do 3 durability damage to an item

#### Blast Fungus
- Changed particle effects
  - Added new cloud particle effect
- Changed damage output to be slightly higher

#### Textures
- The Dragon Fireball texture has been changed to match Bedrock Edition
- The Fire Charge/Fireball texture has been changed
- The On Fire texture for entities has changed depending on whether they are standing in Soul Fire, on Soul Sand, or on Soul Soil
- Wither Skulls now burn with Soul Fire

### Technical Changes
#### Particles
- Added `spore_cloud` particle

#### Sounds
- Added new sound event for Lingering Potion Area Effect Clouds:
  - `entity.area_effect_cloud.gas`
- Added new sound event for Dragon's Breath Area Effect Clouds:
  - `entity.ender_dragon.breath`

#### Stats
- Added `Times Slept in a Tent` statistic

#### Bug Fixes
- Fixed bug that prevented enchanted books from using their vanilla enchantment costs to calculate Magic Capacity
  - Enchanted books' enchantability value now functions as a fallback
- Fixed bug that allowed a charged Channeling trident to be thrown through a portal and summon lightning
- Fixed bug that made Killer Bunnies try and find shelter from weather
- Fixed bug that prevented custom options from saving after exiting the game