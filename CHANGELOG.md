# Changelog

## [1.0.0-SNAPSHOT] - 2025-09-24: Initial Release & Major Feature Update

### Product Feature List
- Implemented a dynamic Research Tree UI with a "snake pattern" display for research stages.
- Visual feedback for research stages: blue stained glass for unlocked, chain command block for current researchable, and red stained glass for locked stages.
- Hover functionality on all research stages to display detailed information (name, description, requirements).
- Interactive "Upgrade Menu" accessible from the current researchable stage.
- Upgrade Menu features: back button (spectral arrow), three resource input slots, and three submit buttons (lime stained glass panes).
- Logic for item submission in the Upgrade Menu, consumption of items, unlocking stages, and returning to the main research tree.
- Local storage of team-specific research progress in `teamData.json` (YAML).
- Created a comprehensive `README.md` as the main entry point for the plugin documentation.
- Created a `USER_GUIDE.md` for server administrators and players, consolidating user-facing information.
- Created a `CONTRIBUTING.md` for developers and contributors with guidelines for contribution.

### Improved List
- Updated Developer/Maintainer Documentation across `DEVELOPER_API.md`, `PLUGIN_DOCUMENTATION.md` (now `USER_GUIDE.md`), `RESEARCH_CONFIGURATION.md`, and `TEAM_SYSTEM.md`.
- Enhanced Developer Tools screen to allow testing and modification of research progression via `ResearchTreeManagementMenu`.
- Renamed the `testGui` command to `researchtree` with aliases (`rt`, `techtree`) and registered it in `plugin.yml`.
- Implemented a structured, interlinked wiki-like documentation system using `SUMMARY.md` (for GitBook compatibility).
- Cleaned up `pom.xml` by updating `maven-compiler-plugin` and `maven-shade-plugin` versions to their latest stable releases, and setting `java.version` to 16.
- Performed extensive Java code cleanup: removed unused imports, added/improved JavaDocs for all public/protected classes and methods, removed redundant code, and ensured consistent formatting across all files in the `org.solocode.techwars` package.

### Fixed List
- Resolved multiple initial compilation errors across various Java files.
- Addressed `java.lang.ArrayIndexOutOfBoundsException` in `UpgradeMenu.java` by adjusting menu row size.
- Fixed `createItem` method being undefined in `UpgradeMenu` by ensuring correct inheritance from `BaseMenu`.
- Corrected abstract method implementation issues in `BaseMenu.java` and `UpgradeMenu.java`.
- Refactored `teamSlots` in `ResearchTreeManagementMenu.java` from `List<Integer>` to `Map<Integer, Team>` to correctly associate menu slots with `Team` objects, resolving interaction issues in the developer tools.
- Removed `@NotNull` annotations from method signatures where they were causing compilation issues after JavaDocs were added.
