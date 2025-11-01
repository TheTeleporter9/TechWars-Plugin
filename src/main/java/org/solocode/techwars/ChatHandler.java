package org.solocode.techwars;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    private final Techwars plugin;

    public ChatHandler(Techwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        TeamColor teamColor = plugin.getPlayerTeam(player.getUniqueId());

        if (teamColor != null) {
            // Format: [TeamName] PlayerName: message (all with team color)
            String teamName = teamColor.getTeamName();
            String prefix = teamColor.getColor() + "[" + teamName + "] " + ChatColor.RESET;
            
            // Apply team color to the player's name and message
            event.setFormat(prefix + teamColor.getColor() + "%s" + ChatColor.RESET + ": %s");
        }
        // If player has no team, use default format (no prefix, no color)
    }
}

