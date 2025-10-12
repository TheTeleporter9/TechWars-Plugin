package org.solocode.techwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.solocode.commands.BorderCommand;

public final class Techwars extends JavaPlugin {

    private BukkitRunnable updateTask;
    private ParticleWall pw;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Techwars Plugin is online!");

        pw = new ParticleWall();

        getCommand("border").setExecutor(new BorderCommand(pw));

        createUpdateLoop();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createUpdateLoop () {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                //Enter all the update loop stuff
                pw.createWall(pw.wallCenterLocations);

            }
        };
        updateTask.runTaskTimer(this, 20L, 20L);
    }
}
