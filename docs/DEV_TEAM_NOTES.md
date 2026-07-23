# Development (Software Engineering & Programming) Team Notes
This file contains notes, practices, and other references for the development team.

# Repository Guide
## Structure
In general, file structure mimics that of Minecraft's source. This is to keep organization clear and consistent. If a file were to share the same name as a Mojang-created file, append "SD" to the class name.

Some quick notes:
- The `mixin` folder is exclusively for Mixin classes. Methods within this folder cannot be referenced from without.
- Mojang mappings with generated sources _(Run `./gradlew genSources`)_ are to be used for development.
- The most recent Fabic API version is used
- Javadocs are my usual comment style of choice. I personally do not write a lot of comments, but if necessary to explain reasoning, please use the `@apiNote` annotation. - KB

# Useful Resources
[Fabric Develop Page](https://fabricmc.net/develop/) - A reference of resources for Fabric projects.
[Fabric Wiki](https://wiki.fabricmc.net/) - The legacy wiki for the Fabric toolchain. Full of documentation for Fabric API and Mixin usage.
[Fabric Developer Guides](https://docs.fabricmc.net/develop/) - The (mostly) updated Fabric Documentation.
`docs\templates\STYLE_EXAMPLE.txt` - A style guide example for Java development

## Bug Tracking
Bugs are to be reported on [GitHub](https://github.com/Meander-Studios/SubtlyDungeons/issues). Our bug tracker can be found here: [Bug Tracker](https://github.com/orgs/Meander-Studios/projects/1)

Bugs are occasionally reported elsewhere ([CurseForge Comments](https://www.curseforge.com/minecraft/mc-mods/subtly-dungeons/comments)) or our [Discord](https://discord.com/channels/1463086964826701927/1463257235651166331). If they are, please create a new issue on the Bug Tracker along with a link to where the bug was reported, and by whom.

Bugs start off as unconfirmed and untriaged. Begine by triaging: determining the priority of patching the bug. Once that is done, place it in the backlog until it is ready to be started. Then, determine the estimated difficulty of patching, any linked issues, and an estimated size for patching.

Once this is complete work on the bug may begin. Time estimates are nice, but not required, as we are not a company with hard deadlines. The bug must be first re-created. If and when it is recreated, this bug can be labeled "Confirmed." Only once the bug is fully patched and tested, may the issue be Closed.

In general, we do not concern ourselves with bugs that are not on the most recent full release. The exception to this, are bugs for backported spin-off mods.

Game crashing bugs have high priority by default.

# Workflow
Typicaly development is done in 3-month cycles of themed updates. This helps us keep pace with Mojang's Game Drops. Comments on our social media suggest that people assume this is difficult. It's really not. Aldin and I can go weeks without either of us doing anything, pick back up on making updates, and still have time for making YouTube videos to post and to honestly sit on our hands sometimes.

That being said, this is possible because Mojang has made mod development much easier. We also don't have to do the same amount of backend work that they do, so don't worry about it too much.

## Planning
Our cycle begins during the end of the previous cycle. Using Discord threads within the "studio" chats, features, pillars, and goals are planned out (most ideas are kept in a massive Confluence whiteboard) and written down.

## Development
After this, feature development begins. A branch should be created (using `develop` as the source) using the naming convention: `update/updateCodename`. If multiple people can/are/will be working on an update, using the update branch as a source, another branch should be made for a particular feature using the naming convention: `feature/nameForBranch`.

Once development is complete, a pull request is made for `develop`. Code review will begin, and once approved, `develop` will be merged onto `main`.

### Snapshots
We release snapshots on a Wednesday or Thursday, following the Minecraft snapshot. Mojang likely releases snapshots on Tuesdays, so that they may use the rest of week to fix bugs before the weekend. For us to consistently release on a Tuesday would be... optimistic at best. So a day or two (or more) afterward works well.
Snapshots are considered Beta builds, while any private builds that are created prior to snapshots are considered Alpha builds.

It's also notable that we typically add 1-2 "non-gameplay" feature with each update (e.g. Command Macros, a new Data-Driven system, etc.), typically near the middle of the development cycle.

Bugs are typically patched in each snapshot, though occasionally some bugs are brought up that don't pertain to the version being currently developed. These bugs are typically patched in the later part of the development cycle. If a bug is particularly large or if multiple people are working on a branch, another branch should be made using the naming convention: `bugfix/nameForBranch`.