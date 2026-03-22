## [4.0 Snapshot 1] - UNRELEASED
### New Features
#### Enchantments
- Added Occult Protection enchantment
  - A protection enchantment that protects the wearer from magical forms of harm
- Added Animal Armor enchantments
  - Horse armor may now be enchanted
  - Wolf armor may now be enchanted
  - Nautilus armor may now be enchanted

#### Curses
- Added Curse of Decaying
  - Affected equipment loses durability twice as fast

#### Speedy Potions
- All drinks (potions, milk buckets, stews, etc.) are now consumed faster!
    - The time has been reduced to 20 ticks, to match the Combat Tests

#### Stackable Potions
- Consumable potions are now stackable up to 16 items
  - This is to match the combat tests

#### Burn No More!
- Players with the Fire Resistance effect will no longer be visually set on fire!

### Technical Changes
#### Data Tags
- Added `liquid_consumables` item tag
  - This is a list of consumables that can be considered liquids (e.g. stews, potions, etc.)
- Added `is_occult` damage type tag
  - This tag controls what types of damage Occult Protection can protect against
    - This includes magic, indirect magic, sonic booms, withering, and thorns
- Added `non_humanoid_armor` item tag
  - This tag is a list of armor items not for humanoids (e.g. diamond horse armor, wolf armor, etc.)
  - Any item with this tag will become enchantable with armor enchantments.

#### Bug Fixes
- Fixed vanilla bug where deaths by dragon's breath were referred to as "magic"
  - It now properly states that "[Player] was roasted in dragon's breath"