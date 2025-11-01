package org.solocode.techwars;

import org.bukkit.ChatColor;

public enum TeamColor {
    RED("Red", ChatColor.RED),
    BLUE("Blue", ChatColor.BLUE),
    GREEN("Green", ChatColor.GREEN),
    YELLOW("Yellow", ChatColor.YELLOW),
    PURPLE("Purple", ChatColor.DARK_PURPLE);

    private final String teamName;
    private final ChatColor color;

    TeamColor(String teamName, ChatColor color) {
        this.teamName = teamName;
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }

    public ChatColor getColor() {
        return color;
    }

    // Helper method to get enum by name
    // Handles variations like "Green Team" -> "Green", "Red Team" -> "Red", etc.
    public static TeamColor fromName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        
        // Normalize the name - remove " Team" suffix if present
        String normalizedName = name.trim();
        if (normalizedName.endsWith(" Team")) {
            normalizedName = normalizedName.substring(0, normalizedName.length() - 5).trim();
        }
        
        for (TeamColor team : values()) {
            if (team.getTeamName().equalsIgnoreCase(normalizedName)) {
                return team;
            }
        }
        return null; // or throw exception if invalid
    }
}
