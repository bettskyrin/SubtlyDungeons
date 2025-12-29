## [2.0] - 12/29/2025
#### Developer Notes:
Oh boy. Mojang changed the Minecraft version system... which made me reconsider our current versioning system (SemVar). Semantic Versioning is specifically for APIs (A.K.A. not a mod), so I think it would make sense to change up the versioning system for the mod as well. `MAJOR.MINOR.PATCH` isn't bad, though part of what this mod aims to do, is to re-capture the spirit of old Mojang (which includes having more "random" updates, that don't necessarily add groundbreaking changes (i.e. I would never want to bump the `MAJOR` number).

Because of this, I think it's most fitting that going forward we will adopt a `UPDATE.HOTFIX-BUILD` system. The Minecraft version suffix will also be dropped, for a few reasons:

1) Fabric will warn you if you install on the wrong version
2) The websites that this mod is currently available on (Modrinth and Curseforge) have filtering systems for the correct Minecraft version
3) The mod intends to have a "foward looking" development cycle, so you can expect updates to focus on whatever the newest update is.

An example using this current update is: `2.0`. If a snapshot or beta is openly released, you can expect a suffix (e.g. `2.0-snapshot` or `2.0-beta`)

### New Features
#### UI
- Added a render of the player on the Title Screen

#### Visuals
- Added a breaking animation for tents

### Changes
#### Visuals
- The method for rendering zombie leaders has changed to increase vanilla and mod compatability
- Changed the camera angle for sleeping in a tent to look straight up

#### Mechanics
- The custom zombie leader health boost mechanics have been removed in favor of the vanilla implementation
- Changed the hitbox of tent entities to be more precise

#### Data
- Changed Screen Shake translation keys
  - `options.screen_shake` is now `options.accessibility.screen_shake`
  - `options.screen_shake`.tooltip is now `options.accessibility.screen_shake.tooltip`
- Added tent entity translations