## [5.0 Snapshot 2] - 6/25/26
### New Features
#### Wind Charge Buff
- Wind Charges now disperse Area Effect Clouds

#### Advancements
- Added I Am Bush advancement
  - Is awarded for performing a stealth attack from within a tall plant

### Changes
#### Enchantments
- Changed Enervation to induce up to 4 seconds of Weakness
- Changed Enervation to only ever induce Weakness I
- Changed Mace to accept Knockback and Looting

#### Items
- Changed Quiver use action to equip it, rather than empty its contents
- Changed Dagger to be able to parry swords in blade clashes
- Changed Trident animation to use the Stab animation
- Changed Wheat to be a Llama tempting item

#### Blocks
- Changed Iron Grates to have the `dragon_immune` block tag

#### Textures
- Changed `blade_clash` particle texture
- Changed Illusioner texture to match Minecraft Dungeons
- Changed Dagger texture

#### Sounds
- Increased the chance of cold wind sounds playing

### Technical Changes
#### Data Tags
- Added `tall_plants` block tag

#### Bug Fixes
- Fixed bug allowing the Ender Dragon to destroy Iron Grates
- Fixed bug causing a client/server de-sync when lighting a campfire with sticks
- Fixed bug where unknown_server.png (commonly known as pack.png) was stretched when there's an issue with a world thumbnail
- Fixed bug causing arrows to disappear if a stack was added onto them
- Fixed bug preventing daggers from attacking through foliage
- Fixed bug preventing the Max Snow Accumulation Height GameRule from affecting snowlogged blocks
- Fixed bug causing Piglins to not be distracted by Golden Daggers