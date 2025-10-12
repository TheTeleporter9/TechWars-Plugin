# TechWars User Guide

## Overview

This guide provides information for server administrators and players on how to use and interact with the TechWars plugin. TechWars introduces a team-based research progression system, allowing players to unlock new technologies and abilities as their team progresses.

## Installation (For Server Administrators)

1.  **Download the Plugin:** Obtain the latest `TechWars-1.0-SNAPSHOT.jar` from the releases page.
2.  **Place in `plugins` folder:** Copy the downloaded `.jar` file into your Minecraft server's `plugins/` directory.
3.  **Start/Restart Server:** Start or restart your Minecraft server to load the plugin and generate the default `config.yml` file.
4.  **Configure:** Edit the `plugins/TechWars/config.yml` file to set up teams, define research stages, and configure developer access. For detailed configuration, refer to `docs/RESEARCH_CONFIGURATION.md`.

## Configuration (For Server Administrators)

The main configuration file is `config.yml`, located in `plugins/TechWars/`. Here you can define:

*   **Developers:** A list of player names who have access to developer tools. (Refer to `DEVELOPER_API.md` for more details).
*   **Teams:** Define teams and their initial members using the following format:

    ```yaml
    Teams:
      TeamName:
        - player1
        - player2
      AnotherTeam:
        - player3
        - player4
    ```

### Important Notes on Configuration

*   Indentation is crucial in YAML configuration.
*   Player names are case-sensitive.
*   Players can be offline when added to teams; they will be associated when they join.
*   Developers must be listed exactly as their Minecraft usernames.

## Team System

TechWars features a dynamic team system. Players can be part of a team, and their research progress is tracked per team. Team members can contribute to research, and the benefits of unlocked stages apply to all members of the team.

### Team Member Management

*   **Adding members:** Online players are added using in-game commands or developer tools. Offline players can be added via `config.yml`.
*   **Checking membership:** The plugin automatically identifies a player's team for features like chat prefixes and research progress.

## Research Tree

The research tree is an interactive in-game menu where teams unlock new stages. Each stage has specific item requirements that team members must gather and submit.

### Interacting with the Research Tree

1.  **Open the Research Tree:** Use the command `/researchtree` (or aliases `/rt`, `/techtree`).
2.  **View Stages:** The GUI displays stages with different colors:
    *   **Blue Stained Glass:** Stages that your team has already unlocked. Hover over them to see the stage name and description.
    *   **Chain Command Block:** The current stage your team is actively researching. Click this to open the `Upgrade Menu` to submit resources.
    *   **Red Stained Glass:** Locked stages that your team has not yet reached. Hover over them to see what you can unlock in the future.
3.  **Unlocking Stages:** Progress is sequential. You can only unlock the stage indicated by the `Chain Command Block` after fulfilling its requirements.

### Upgrade Menu

When you click on the `Chain Command Block` in the Research Tree, the `Upgrade Menu` opens:

*   **Back Button:** A golden arrow in the top-left slot (`0`) to return to the main Research Tree.
*   **Resource Slots:** Three slots (at `2, 3, 4`) where team members can place required items.
*   **Submit Buttons:** Three lime-stained glass panes on the far right (`8, 17, 26`) to submit the placed resources.

Upon successful submission of all required items, the items are consumed, the stage is unlocked for your team, and you are returned to the main Research Tree menu.

## Commands

### Player Commands

*   `/researchtree` (aliases: `/rt`, `/techtree`): Opens the team's research tree GUI.

### Admin Commands (Refer to `DEVELOPER_API.md` for full details)

*   `/dev`: Opens the developer menu.
*   `/researchadmin <unlock|reset|copy|test> [args...]`: Research administration commands.
*   `/techwars reload`: Reloads plugin configuration.

## Configuration Updates

The plugin automatically updates its configuration in response to:

1.  Server startup/reload.
2.  Manual `/techwars reload` command.
3.  Changes made through the `/dev` menu.
4.  Direct modifications to `config.yml` (requires `/techwars reload` or server restart).

## Troubleshooting

### Common Issues

1.  **Player not recognized as developer:**
    *   Check exact username spelling in `config.yml` under the `Developers` section.
    *   Ensure proper YAML formatting.
    *   Reload configuration (`/techwars reload`).

2.  **Player not in a team:**
    *   Verify team configuration format in `config.yml`.
    *   Check for case-sensitive username matches.
    *   Ensure the team was loaded properly (check server logs).

3.  **Configuration not updating:**
    *   Check file permissions for `config.yml`.
    *   Use `/techwars reload` command.
    *   Verify YAML syntax for errors.

### Debug Tips

*   Always check the server console for error messages or warnings from the TechWars plugin.
*   Use the `/dev` menu to verify current configurations and team data.
*   Test changes on a separate development server before deploying to a live environment.

## See Also

*   [Introduction to TechWars](../README.md)
*   [Research Configuration Guide](../docs/RESEARCH_CONFIGURATION.md)
*   [Team System Documentation](../docs/TEAM_SYSTEM.md)
*   [Developer API Documentation](../docs/DEVELOPER_API.md)

---
*Generated by TechWars Plugin Documentation Assistant.*
