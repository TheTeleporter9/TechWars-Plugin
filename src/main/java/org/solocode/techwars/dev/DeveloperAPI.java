package org.solocode.techwars.dev;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.solocode.techwars.TechWars;

import java.util.List;

/**
 * Developer API for TechWars plugin.
 * Handles developer permissions and access to developer tools.
 */
public class DeveloperAPI {
    private final TechWars plugin;
    private List<String> developers;

    /**
     * Constructs a new DeveloperAPI instance.
     * @param plugin The instance of the TechWars plugin.
     */
    public DeveloperAPI(TechWars plugin) {
        this.plugin = plugin;
        loadDevelopers();
    }

    /**
     * Loads developers from config
     */
    public void loadDevelopers() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection devSection = config.getConfigurationSection("Developers");
        if (devSection != null) {
            developers = devSection.getKeys(false).stream().toList();
        } else {
            developers = config.getStringList("Developers");
        }
        plugin.getLogger().info("Loaded developers: " + developers);
    }

    /**
     * Checks if a player is a developer
     * @param player the player to check
     * @return true if player is a developer
     */
    public boolean isDeveloper(Player player) {
        return developers.contains(player.getName());
    }

    /**
     * Gets the plugin instance.
     * @return The plugin instance.
     */
    public TechWars getPlugin() {
        return plugin;
    }
}