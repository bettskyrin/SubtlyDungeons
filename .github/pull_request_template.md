# Pull Request Checklist

## Pre-Release Checklist
*Must be completed before merging into Main*

- [ ] **Version Bump:** Updated `gradle.properties` AND `publish.yml` to the new version number?
- [ ] **Dependencies:** Checked `fabric.mod.json` for correct Minecraft/Loader versions?
- [ ] **Lang Files:** Did you add translation keys for new blocks/items?
- [ ] **Server Test:** Did you run this on a dedicated server to check for Client-side only crashes?
- [ ] **Changelog:** Updated `CHANGELOG.md` with a summary of changes?

##  Testing Steps
1. Test for crashes. Go wild, try different entity and block interactions, looking for broken behavior.
2. Test new features. Ensure all new blocks, items, and mechanics work as intended.
3. Verify performance. Check for any significant FPS drops or lag spikes.