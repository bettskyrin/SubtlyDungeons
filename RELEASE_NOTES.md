## [4.0 Snapshot 1] - UNRELEASED
### New Features
#### Enchantments
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm

#### Speedy Potions
- All drinks (potions, milk buckets, stews, etc.) are now consumed faster!
    - The time has been reduced to 20 ticks, to match the Combat Tests

#### Burn No More!
- Players with the Fire Resistance effect will no longer be visually set on fire!

### Technical Changes
#### Data Tags
- Added `liquid_consumables` item tag
  - This is a list of consumables that can be considered liquids (e.g. stews, potions, etc.)
- Added `is_occult` damage type tag
  - This tag controls what types of damage Occult Protection can protect against
    - This includes magic, indirect magic, sonic booms, 