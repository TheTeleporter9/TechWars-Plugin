# TechWars Developer API Documentation

## Overview
The TechWars Developer API provides tools and interfaces for server administrators and developers to manage and modify plugin functionality in-game.

## Configuration
Developers are specified in the config.yml file:
```yaml
Developers:
    - developer1
    - developer2
```
Only listed players will have access to developer tools.

## Developer Menu
Access the developer menu using the `/dev` command. The menu provides access to:
- Team Management
- Data Management
- Configuration Reload

### Adding New Developer Features
To add new features to the developer menu:

1. Create a new menu class extending `SimpleMenue`:
```java
public class CustomFeatureMenu extends SimpleMenue {
    public CustomFeatureMenu(Player player) {
        super(6, "§6Custom Feature");
        onSetItems();
    }

    @Override
    public void onSetItems() {
        // Add your menu items here
        setItem(0, createItem(Material.DIAMOND, "§bFeature 1", "§7Description"));
    }

    @Override
    public void onClick(Player player, int slot) {
        // Handle click events
        switch(slot) {
            case 0:
                // Handle Feature 1
                break;
        }
    }
}
```

2. Add a button to the DeveloperMenu:
```java
// In DeveloperMenu.onSetItems():
setItem(slot, createItem(Material.YOUR_MATERIAL, "§eYour Feature", "§7Description"));

// In DeveloperMenu.onClick():
case yourSlot:
    new CustomFeatureMenu(player).open(player);
    break;
```

### Creating Custom Data Managers
To add custom data management:

1. Create a data structure:
```java
public class CustomData {
    private final String name;
    private final Map<String, Object> data;

    public CustomData(String name) {
        this.name = name;
        this.data = new HashMap<>();
    }

    // Add getters/setters
}
```

2. Add data storage to Team:
```java
Team team = teamManager.getTeam("TeamName").orElse(null);
if (team != null) {
    CustomData data = new CustomData("featureName");
    team.setData("customFeature", data);
}
```

3. Create a menu to manage the data:
```java
public class CustomDataMenu extends SimpleMenue {
    private final Team team;

    public CustomDataMenu(Player player, Team team) {
        super(6, "§6Data Manager");
        this.team = team;
        onSetItems();
    }

    @Override
    public void onSetItems() {
        // Add items to view/modify custom data
        CustomData data = team.getData("customFeature");
        // Display data in menu
    }
}
```

## Best Practices
1. Always validate developer permissions using `DeveloperAPI.isDeveloper()`
2. Use descriptive menu titles and item descriptions
3. Implement confirmation for destructive actions
4. Keep data structures organized and documented
5. Use team-specific data storage for feature data
6. Implement proper error handling and user feedback

## Example: Adding Team Research Data
```java
// Create data structure
public class ResearchData {
    private final Set<String> unlockedTech;
    private final Map<String, Integer> researchProgress;

    public ResearchData() {
        this.unlockedTech = new HashSet<>();
        this.researchProgress = new HashMap<>();
    }

    public void unlockTech(String techId) {
        unlockedTech.add(techId);
    }

    public boolean hasTech(String techId) {
        return unlockedTech.contains(techId);
    }
}

// Store data for a team
Team team = teamManager.getTeam("TeamName").orElse(null);
if (team != null) {
    ResearchData data = new ResearchData();
    data.unlockTech("basic_tech");
    team.setData("research", data);
}

// Access data in menus/commands
ResearchData research = team.getData("research");
if (research != null && research.hasTech("basic_tech")) {
    // Allow access to technology
}
```

## Error Handling
Always provide clear feedback to developers:
```java
try {
    // Perform operation
} catch (Exception e) {
    player.sendMessage("§cError: " + e.getMessage());
    // Log error for server admin
    plugin.getLogger().severe("Developer API error: " + e.getMessage());
}
```