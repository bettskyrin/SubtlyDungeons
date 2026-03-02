## [3.0 Snapshot 6] - 1/30/2026
### New Features
#### Zombie-Type Leaders
- Updated baby zombie leader texture

#### Increased Pet Follow Range
- Pets will now follow their owners for 20 meters before attempting to teleport!
  - This was increased from the default 12 meters

### Visuals
- Added Mace Smash Air, Mace Smash Ground, Mace Smash Ground Heavy, and End Flash screen shake event
- Removed the Ender Dragon Growls screen shake event
  - This is due to this event being triggered server side (i.e. independent of what the player's actually hearing)

### UI
- Tweaked the Poison-affected heart icon

### Technical Changes
#### Data Tags
- Created `can_be_scared` entity type tag.
    - This tag determines whether an animal species will panic if a nearby animal takes damage.

#### Translations
- Changed all references to "screen shake" to "camera shake"