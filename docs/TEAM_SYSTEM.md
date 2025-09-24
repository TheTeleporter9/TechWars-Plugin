# TechWars Team System Documentation

## Overview
The TechWars plugin includes a dynamic team system that allows for team-based functionality and data storage. Teams are configured in the `config.yml` file and their data is managed programmatically through the `TeamManager` API. Team progress for features like the Research Tree is stored in `teamData.json`.

## Configuration (for Server Administrators)
Teams are initially defined in the `config.yml` file. This section details the structure for defining teams.

```yaml
Teams:
    TeamName:
        color: "#FF0000" # Optional: specify a hex color for the team
        members:
            - player1
            - player2
            - player3
    AnotherTeam:
        color: "#0000FF"
        members:
            - player4
            - player5
```

-   **`TeamName`**: The unique identifier for the team. This is used in commands and internal references.
-   **`color`**: (Optional) A hex color code (e.g., "#FF0000" for red) that will be used for team-related displays, such as chat prefixes. If not specified, a random color will be assigned.
-   **`members`**: A list of player names who are part of this team. Players can be online or offline when added; offline players will be linked to the team upon joining the server.

## API Usage (for Developers)

### Accessing the Team Manager
The primary class for interacting with the team system is `org.solocode.teams.TeamManager`. You can access its instance via the main plugin class:

```java
TeamManager teamManager = TechWars.getInstance().getTeamManager();
```

### Working with Teams

*   **Get a player's team:**
    ```java
    Optional<Team> playerTeam = teamManager.getPlayerTeam(player);
    ```

*   **Get a specific team by name:**
    ```java
    Optional<Team> team = teamManager.getTeam("TeamName");
    ```

*   **Create a new team (programmatically):**
    ```java
    Optional<Team> newTeam = teamManager.createTeam("NewTeam");
    // Check if the team was created successfully before proceeding
    newTeam.ifPresent(team -> {
        // ... further actions with the new team ...
    });
    ```

*   **Get all registered teams:**
    ```java
    Map<String, Team> allTeams = teamManager.getAllTeams();
    ```

### Team Member Management (within a `Team` object)

*   **Add an online player to the team:**
    ```java
    team.addMember(player);
    ```

*   **Add an offline player to the team (by name):**
    ```java
    team.addOfflineMember("playerName");
    ```

*   **Remove a player from the team:**
    ```java
    team.removeMember(player);
    ```

*   **Check if a player is a member:**
    ```java
    boolean isMember = team.isMember(player);
    ```

*   **Get all member names:**
    ```java
    Set<String> members = team.getMemberNames();
    ```

### Team Data Storage
Each `Team` object provides a flexible key-value store for custom plugin data. This allows different features (e.g., research progress, custom statistics) to be associated directly with a team.

*   **Store data for a team:**
    ```java
    team.setData("customKey", customValue);
    ```
    *Note: `customValue` can be any serializable Java object.* The data is automatically saved to `teamData.json` by `TeamDataManager`.

*   **Retrieve data stored for the team:**
    ```java
    CustomType data = team.getData("customKey");
    // Always handle potential null returns if the key might not exist
    if (data != null) {
        // ... use data ...
    }
    ```

### Team Color Management
Teams can have an associated color, primarily used for visual identification in-game (e.g., chat prefixes).

*   **Get the team's hex color:**
    ```java
    String hexColor = team.getColor();
    ```
    If no color is explicitly set in `config.yml`, a random color will be generated.

*   **Set the team's hex color:**
    ```java
    team.setColor("#RRGGBB");
    ```
    Changes made via this method are persistent.

## Data Persistence
Team data (members, custom data, color) is automatically loaded on plugin enable and saved on plugin disable (or server shutdown) via the `TeamDataManager`. This ensures that all team-specific progress and settings are preserved across server restarts.

## Best Practices (for Developers)

1.  **Always check for team presence:** Before performing team-specific operations, use `teamManager.getPlayerTeam(player).ifPresent(...)` or check `Optional.isPresent()` to ensure the player is in a team.
2.  **Use meaningful data keys:** When storing custom data using `team.setData()`, choose descriptive keys to avoid conflicts and improve code readability.
3.  **Document custom data structures:** If you store complex objects in team data, document their structure and purpose for future maintenance.
4.  **Handle `Optional` returns:** Always properly handle `Optional` returns from methods like `getTeam()` and `getPlayerTeam()` to prevent `NullPointerException`s.
5.  **Avoid direct file manipulation:** Rely on `TeamDataManager` and `Team` methods for all data persistence to ensure consistency and prevent data corruption.

## See Also

*   [User Guide](../docs/USER_GUIDE.md)
*   [Research Configuration Guide](../docs/RESEARCH_CONFIGURATION.md)
*   [Developer API Documentation](../docs/DEVELOPER_API.md)

---
*Generated by TechWars Plugin Documentation Assistant.*