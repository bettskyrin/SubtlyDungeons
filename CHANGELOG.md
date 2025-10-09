# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

- Added Tent
  - Can be slept in at any time, to skip to either day or nighttime
  - Does not reset your spawn point

#### Visuals
- Added camera shake events
  - Loud events can now shake the camera
  - This applies to: Ravager roars, Ender Dragon roars and growls, the Warden roar, emergence, and sonic shriek, end gateway creation, lightning strikes, and explosions

- Leader zombies, drowned, and husks have a unique texture to help differentiate them from the normal versions