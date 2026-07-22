# Asset Team Notes
This file contains notes and other references for the asset team.

## Repository Guide
`/assets` - Folder to store raw asset files that may recieve updates and need to be tracked. (e.g. entity models (Entity models must be stored as .bbmodel files, but are not used in that format for the actual code), etc.)
`.gitignore` - File storing any files or folders that should not be uploaded to GitHub. Use this for WIP files that do not need to be shared.
`docs` - Folder containing documentation, templates, and other resources for the development team

### Dictionary
**git** - A type of "Version Control Software" (VCS). Helps developers keep track of updated and older version of code.
**commit** - A git "edit"
**commit message** - A message written to explain what changes were made in that partiuclar commit/"edit"

## Useful Resources
### Visual
[Blockbench](https://www.blockbench.net/) - Modeling/texturing software. Feel free to explore plugins that could make your work easier. Also check out their Wiki for use guides and a Minecraft Style guide
[Minecraft Assets Explorer](https://mcasset.cloud/) - Website that lets you browse Minecraft assets (textures, sounds, models, fonts, shaders, etc.)
[Minecraft Dungeons Textures](https://minecraft.wiki/w/Dungeons:List_of_prefab_textures) - List of some textures used in Minecraft Dungeons

[Zeit](https://sites.google.com/view/zeits-portfolio) - Someone who has helped us with models before, on commision. Depending on our financial state, we may be able to afford a commission for complex models or animations.

### Audio
[Audacity](https://www.audacityteam.org/next/) - Audio editing software. All audios must be saved in the OGG (.ogg) file format 
[Pixabay]([https://pixabay.com/](https://pixabay.com/sound-effects/)) - Royalty free sound effects. See notes below.

## Subtly Dungeons Resources
[Soundtrack](https://www.youtube.com/playlist?list=PLF4UTbnOOeMY) - All music featured within Subtly Dungeons.
[Saved Audio](https://www.youtube.com/playlist?list=PLkg3oeBK2SDKA04SEyeih9VzGEx8tEgMy) - Audio Tracks that we find interesting/useful. Some tracks can be sampled for sound effects.

## Other Notes
### Modeling
- Do not use libraries or APIs for animations or texturing. Subtly Dungeons is meant to keep APIs to a minimum - KB
- Do not use plugins designed for Bedrock Edition. Subtly Dungeons is a Java Fabric mod that uses unobfuscated (sometimes may be called Mojang mappings) mappings. NOT Yarn mappings. - KB
- All visual asset approval go through Aldin

### Sound Design
- Use foley when possible. Try weird things. Distort the sounds. The campfire sound was made by me scraping the side of a pizza box with my fingernail. - KB
- If foley is yielding no results, you may use royalty-free sound effects. Though this is discouraged. - KB

### Misc Notes