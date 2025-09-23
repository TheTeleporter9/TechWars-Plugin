# TechWars Team System Documentation

## Overview
The TechWars plugin includes a dynamic team system that allows for team-based functionality and data storage. Teams are configured in the config.yml file and can be accessed programmatically through the TeamManager API.

## Configuration
Teams are defined in the config.yml file using the following format:
```yaml
Teams:
    TeamName:
        - player1
        - player2
        - player3
    AnotherTeam:
        - player4
        - player5
```

## API Usage

### Accessing the Team Manager
```java
TeamManager teamManager = TechWars.getInstance().getTeamManager();
```

### Working with Teams
```java
// Get a player's team
Optional<Team> playerTeam = teamManager.getPlayerTeam(player);

// Get a specific team
Optional<Team> team = teamManager.getTeam("TeamName");

// Create a new team
Optional<Team> newTeam = teamManager.createTeam("NewTeam");

// Get all teams
Map<String, Team> allTeams = teamManager.getAllTeams();
```

### Team Data Storage
Each team can store custom data using the Team class methods:
```java
// Store data
team.setData("customKey", customValue);

// Retrieve data
CustomType data = team.getData("customKey");
```

### Example: Implementing Team-Specific Features
```java
public void executeTeamFeature(Player player) {
    TeamManager teamManager = TechWars.getInstance().getTeamManager();
    
    teamManager.getPlayerTeam(player).ifPresent(team -> {
        // Load team-specific data
        TeamData data = team.getData("featureData");
        
        // Perform team-specific operations
        if (data != null) {
            // Use the data
        }
        
        // Store updated team data
        team.setData("featureData", updatedData);
    });
}
```

## Best Practices
1. Always check if a player is in a team before performing team-specific operations
2. Use meaningful keys for team data storage
3. Document any custom data structures stored in team data
4. Consider implementing data persistence for team data if needed
5. Use Optional returns for safer team-related operations

## Integration with Existing Systems
The team system is designed to work seamlessly with other plugin features. For example, the TechTree system can store progress per team by using the team data storage functionality.