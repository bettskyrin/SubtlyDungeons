# Pull Request Checklist

## Checklist
*Must be completed before merging into main*

- [ ] **Version Bump:** Updated `gradle.properties` to the new version number?
- [ ] **Dependencies:** Checked `fabric.mod.json` for correct Minecraft/Loader versions?
- [ ] **Changelog:** Updated `CHANGELOG.md` and `RELEASE_NOTES.md` with a summary of changes?
- [ ] **Server Test:** Did you run this on a dedicated server to check for Server-side-only crashed?
- [ ] **Lang Files:** Did you add translation keys for new blocks/items/tags?

###  Things To Think About
- Is it broken? Test for crashes. Go wild, try different entity and block interactions, looking for broken behavior or weird interactions. *Try* to get unintended behavior.
    - Test new features. Ensure all new blocks, items, and mechanics work as intended.
- Is it performant? Check for any significant FPS drops.
- Is it readable? Is it hard to understand? Make sure you write Javadoc comments.

// TODO