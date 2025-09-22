package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.menu.listeners.InventoryListener;
import org.solocode.techwars.TechTree.ConfigReader;
import org.solocode.techwars.commands.testGui;

public final class TechWars extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getLogger().warning("Techwars plugin is online!");

        getConfig().options().copyDefaults();
        saveConfig();

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getCommand("testgui").setExecutor(new testGui());
    }

}
