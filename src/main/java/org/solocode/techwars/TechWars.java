package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.commands.TestMenuCommand;
import org.solocode.menu.listeners.InventoryListener;

/**
 * Main plugin class for TechWars. This class handles plugin startup, shutdown, 
 * command registration, event listener registration, and provides access to core managers.
 */
public final class TechWars extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("dev").setExecutor(new TestMenuCommand());

    }


}
