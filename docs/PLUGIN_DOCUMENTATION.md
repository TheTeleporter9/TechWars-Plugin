# TechWars Plugin Documentation

## Configuration
The plugin uses a YAML configuration file (`config.yml`) for both team and developer settings.

### Example Configuration
```yaml
# Developer access configuration
Developers:
  - developer1
  - developer2

# Team configuration
Teams:
  TeamName:
    - player1
    - player2
  AnotherTeam:
    - player3
    - player4
```

### Important Notes
- Indentation is crucial in YAML configuration
- Player names are case-sensitive
- Players can be offline when added to teams
- Developers must be listed exactly as their Minecraft usernames

## Developer System

### Developer Access
Developers have access to special administrative tools through the `/dev` command. This includes:
- Team management
- Data management
- Configuration reload functionality

### Using Developer Tools
1. Add developer usernames to the config.yml under the `Developers` section
2. Developers can use `/dev` in-game to access the management menu
3. Changes made through the developer menu take effect immediately

### Developer Menu Features
- Team Management: View and modify team configurations
- Data Management: Manage team-specific data
- Config Reload: Refresh plugin configuration and team data

## Team System

### Team Configuration
Teams are defined in the config.yml file and support both online and offline players:
```yaml
Teams:
  TeamName:
    - player1    # Can be online or offline
    - player2    # Will be added when they join
```

### Team Management API
```java
// Access the team manager
TeamManager teamManager = TechWars.getInstance().getTeamManager();

// Working with teams
Optional<Team> playerTeam = teamManager.getPlayerTeam(player);
Optional<Team> team = teamManager.getTeam("TeamName");
Optional<Team> newTeam = teamManager.createTeam("NewTeam");
Map<String, Team> allTeams = teamManager.getAllTeams();
```

### Team Member Management
```java
// Adding members
team.addMember(player);            // For online players
team.addOfflineMember("playerName"); // For offline players

// Checking membership
boolean isMember = team.isMember(player);

// Getting members
Set<String> members = team.getMemberNames();
```

### Team Data Storage
Each team can store custom data:
```java
// Store data
team.setData("customKey", customValue);

// Retrieve data
CustomType data = team.getData("customKey");
```

## Configuration Updates
The plugin automatically updates in these situations:
1. Server startup/reload
2. When using the `/dev` menu's reload button
3. After server reload commands
4. After config.yml modifications

### Manual Reload
Developers can reload the configuration using:
1. The `/dev` command
2. Clicking the "Reload Config" button in the developer menu

## Best Practices

### Team Management
1. Always check team membership before performing team-specific operations
2. Use team data storage for team-specific features
3. Consider both online and offline players when managing teams
4. Use meaningful keys for team data storage

### Developer Tools
1. Limit developer access to trusted administrators
2. Use the developer menu for configuration changes
3. Document any custom data structures
4. Test changes in a safe environment first

### Error Handling
```java
// Example: Safe team operations
teamManager.getPlayerTeam(player).ifPresent(team -> {
    try {
        // Perform team operations
        TeamData data = team.getData("featureData");
        // Use the data
    } catch (Exception e) {
        player.sendMessage("§cAn error occurred: " + e.getMessage());
        plugin.getLogger().warning("Team operation failed: " + e.getMessage());
    }
});
```

## Integration Guide

### Adding New Features
1. Use the team system as a foundation for team-based features
2. Store feature data using the team data storage system
3. Add feature configuration to the developer menu if needed
4. Document the feature's data structure and usage

### Example: Team Feature Integration
```java
public class CustomFeature {
    private final TeamManager teamManager;
    
    public void executeTeamFeature(Player player) {
        teamManager.getPlayerTeam(player).ifPresent(team -> {
            FeatureData data = team.getData("featureData");
            if (data == null) {
                data = new FeatureData();
                team.setData("featureData", data);
            }
            // Implement feature logic
        });
    }
}
```

### Handling Configuration Changes
- Listen for configuration reloads if needed
- Cache and update data appropriately
- Provide clear feedback to users

## Troubleshooting

### Common Issues
1. Player not recognized as developer
   - Check exact username spelling in config.yml
   - Ensure proper YAML formatting
   - Reload configuration

2. Player not in team
   - Verify team configuration format
   - Check for case-sensitive username matches
   - Ensure team was loaded properly

3. Configuration not updating
   - Check file permissions
   - Use developer menu to reload
   - Verify YAML syntax

### Debug Tips
- Check server logs for errors
- Use the developer menu to verify configuration
- Test with online and offline players
- Verify team data persistence