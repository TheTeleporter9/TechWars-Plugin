package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.menu.listeners.InventoryListener;
import org.solocode.techwars.commands.DevCommand;
import org.solocode.techwars.commands.testGui;
import org.solocode.techwars.listeners.ChatListener;
import org.solocode.techwars.listeners.ConfigListener;
import org.solocode.teams.TeamManager;

public final class TechWars extends JavaPlugin {
    private static TechWars instance;
    private TeamManager teamManager;

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
        getCommand("testgui").setExecutor(new testGui());
        getCommand("dev").setExecutor(new DevCommand(this));
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

    @Override
    public void onDisable() {
        // Save all team data before shutdown
        if (teamManager != null) {
            teamManager.saveTeamData();
        }
    }
}
