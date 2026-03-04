## [3.0 Snapshot 6] - 3/6/2026
### New Features
#### Zombie-Type Leaders
- Removed baby zombie type leader textures

#### Comfy Animals
- Animals will now seek out more comfortable environments!
  - Animals caught out in the rain will look for shelter
  - Non-cold acclimated animals (or their variants) will now seek out warmer spots to hang around

#### Increased Pet Follow Range
- Pets will now follow their owners for 20 meters before attempting to teleport!
  - This was increased from the default 12 meters

#### Fleeing Fauna
- Armadillos no longer curl up based on roll danger.
  - Fun fact, a group of armadillos is called a "roll"

### Visuals
### UI
- Changed the Poison-affected heart icon

### Technical Changes
#### Data Tags
- Added `can_be_scared` entity type tag.
    - This tag determines whether an animal species will panic if a nearby animal takes damage.
- Added `seeks_shelter` entity type tag.
  - This tag determines whether an animal species will seek shelter from rain.
- Added `can_seek_warmth` entity type tag.
  - This tag determines whether an animal species can seek warmth from cold environments.
    - Note: Internal code determines if a specific animal variant should be considered "warm", "temperate", or "cold". There is not currently a data-driven method for making a custom mob variant fall into one of these categories.

#### Translations
- Changed all references to "screen shake" to "camera shake"