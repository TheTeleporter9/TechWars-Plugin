package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.menu.listeners.InventoryListener;
import org.solocode.techwars.commands.DevCommand;
import org.solocode.techwars.commands.ResearchCommand;
import org.solocode.techwars.commands.ResearchAdminCommand;
import org.solocode.techwars.commands.ResearchTreeCommand;
import org.solocode.techwars.commands.TechWarsCommand;
import org.solocode.techwars.listeners.ChatListener;
import org.solocode.techwars.listeners.ConfigListener;
import org.solocode.teams.TeamManager;

/**
 * Main plugin class for TechWars. This class handles plugin startup, shutdown, 
 * command registration, event listener registration, and provides access to core managers.
 */
public final class TechWars extends JavaPlugin {
    private static TechWars instance;
    private TeamManager teamManager;

    /**
     * Called when the plugin is enabled. Initializes managers, registers listeners and commands.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        Bukkit.getLogger().warning("Techwars plugin is online!");

        // Ensure config directory exists and save default config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Initialize team manager
        teamManager = new TeamManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ConfigListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        
        // Register commands
        getCommand("research").setExecutor(new ResearchCommand(this));
        getCommand("researchadmin").setExecutor(new ResearchAdminCommand(this));
        getCommand("techwars").setExecutor(new TechWarsCommand(this));
        getCommand("dev").setExecutor(new DevCommand(this));
        getCommand("researchtree").setExecutor(new ResearchTreeCommand(this));
    }

    /**
     * Gets the instance of the plugin
     * @return plugin instance
     */
    public static TechWars getInstance() {
        return instance;
    }

    /**
     * Gets the team manager
     * @return team manager instance
     */
    public TeamManager getTeamManager() {
        return teamManager;
    }

    /**
     * Called when the plugin is disabled. Saves all team data.
     */
    @Override
    public void onDisable() {
        // Save all team data before shutdown
        if (teamManager != null) {
            teamManager.saveTeamData();
        }
    }
}
