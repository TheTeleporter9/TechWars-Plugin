package org.solocode.techwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.solocode.techwars.TechWars;

/**
 * Listener for server config events to ensure team data stays in sync
 */
public class ConfigListener implements Listener {
    
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().toLowerCase();
        // Check if the command is a reload command
        if (cmd.equals("reload") || cmd.equals("rl") || cmd.startsWith("reload confirm") || cmd.equals("bukkit:reload")) {
            // Schedule team reload after server reload
            TechWars.getInstance().getServer().getScheduler().runTaskLater(
                TechWars.getInstance(),
                () -> TechWars.getInstance().getTeamManager().loadTeams(),
                1L
            );
        }
    }
}