# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0-alpha.3] - Unreleased
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