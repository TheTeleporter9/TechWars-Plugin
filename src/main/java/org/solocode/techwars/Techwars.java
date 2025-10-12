package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.solocode.commands.BorderCommandNew;

public final class Techwars extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Techwars Plugin is online!");

        getCommand("border").setExecutor(new BorderCommandNew());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
