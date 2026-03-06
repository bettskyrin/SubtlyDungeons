## [3.0 Snapshot 7] - 3/11/2026
### New Features
#### Cozy Creatures
- Non-heat acclimated animals (or their variants) will now seek out cooler spots in hot environments
- Improved animal "cozy spot" prioritization

#### Closer Pets
- Pets now sprint with their owners!

### Technical Changes
- Added `can_seek_shade` entity type tag.
    - This tag determines whether an animal species can seek cooler spots in hot environments.
#### Performance
- Optimized pathfinding logic for animals seeking shelter

### Bug Fixes
- Fixed a bug that made spiders clawling upwards at an angle face the opposite direction